package com.example.tritimer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.MotionEvent.ACTION_MOVE
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintSet
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getSystemService
import com.example.tritimer.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Float.max
import java.lang.Float.min
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnTouchListener {

    companion object{
        //array lists
        val hoursTextViewList: ArrayList<TextView> = ArrayList()
        val hourArrPositions: ArrayList<Float> = ArrayList()

        val alarm1Sliders: ArrayList<View> = ArrayList()
        val alarm2Sliders: ArrayList<View> = ArrayList()
        val alarm3Sliders: ArrayList<View> = ArrayList()

        val alarmTextViews: ArrayList<TextView> = ArrayList()

        //user touch data
        var touchx: Float = 0f
        var touchy: Float = 0f
        var dx: Float = 0f
        var dy: Float = 0f
        val hours = 12
        var timeLineWidth: Float = -1f

        //Constrainlayouts that hold the alarm sliders
        var constraintLayout1: ConstraintLayout?? = null
        var constraintLayout2: ConstraintLayout?? = null
        var constraintLayout3: ConstraintLayout?? = null

        lateinit var auth: FirebaseAuth
        const val EMAIL: String = "template@email.com"
        const val NAME : String = "Jack"
        const val PASS : String = "12345"
        val database = Firebase.database
         lateinit var calendar: Calendar
         lateinit var alarmManager: AlarmManager
         lateinit var pendingIntent: PendingIntent
         lateinit var picker : MaterialTimePicker
        var am: Boolean = true
    }


    lateinit var binding : ActivityMainBinding
//    lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Instantiate Compainion Vars*/
        val timeLine = findViewById<LinearLayout>(R.id.hour_layout_id)



        constraintLayout1 = findViewById(R.id.slider_holder1)
        constraintLayout2= findViewById(R.id.slider_holder2)
        constraintLayout3= findViewById(R.id.slider_holder3)

        val alarmTV1: TextView = findViewById(R.id.alarm1_tv)
        val alarmTV2: TextView = findViewById(R.id.alarm2_tv)
        val alarmTV3: TextView = findViewById(R.id.alarm3_tv)

        alarmTextViews.add(alarmTV1)
        alarmTextViews.add(alarmTV2)
        alarmTextViews.add(alarmTV3)

        /* Populate the Time line */
        addHours(timeLine)
        /* Create notifications for alarm */
        createNotificationChannel()

        /* Measure Timeline width */
        timeLine.viewTreeObserver.addOnGlobalLayoutListener {
            Log.i("Width", timeLine.width.toString())
            timeLineWidth = timeLine.width.toFloat()
        }

        /* Call Button Listeners */
        handleAddRemoveButtons()

    }

    override fun onTouch(view: View?, p1: MotionEvent?): Boolean {
        if (view != null && p1 != null) {
            val action = p1.action
            return when (action) {
                MotionEvent.ACTION_UP -> {
                    resetTimelineColor()
                    slideTimePicker(view)
                    setAlarm()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    dx = p1.x - touchx

                    if(!checkSliderOverLap(view)) {
                        if (view.x + view.width <= timeLineWidth && view.x >= 0) {
                            view.x = view.x + dx
                            touchx = p1.x
                        }
                    }

                    if(view.x <= 0){
                        view.x = 0f
                    }

                    if(view.x + view.width >= timeLineWidth){
                        view.x = timeLineWidth -  view.width
                    }
                    updateTime(view)
                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    touchx = p1.x
                    cancelAlarm()
                    true
                }
                else -> true
            }
        }
        return false
    }








    //****************  FIREBASE    ******************

    fun read(){
        database.reference.child("data").child("tests").addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot)
            {
                val fetchedVal = snapshot.value.toString()
                Log.i("TAG", "this was found ${fetchedVal}")
            }

            override fun onCancelled(error: DatabaseError)
            {
                TODO("Not yet implemented")
            }
        })
    }

    fun write(){
        database.getReference("data").child("tests").setValue("This is from Android", DatabaseReference.CompletionListener { error, ref ->

            if(error == null){
                Toast.makeText(this, "Wrote successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Wrote wrong", Toast.LENGTH_LONG).show()
            }
        } )
    }


    //****************  Pinch Gesture    ******************

//    private fun handleSliderClip(constraintLayout: ConstraintLayout, slider:View)
////    private fun handleSliderClip()
//    {
//        if (getSliderClipWidth(slider) > 0) {
//            addSlider(constraintLayout , slider.width, 0f)
//            Toast.makeText(this, "${constraintLayout.x}", Toast.LENGTH_SHORT).show()
//        }
//
//    }

//    private fun getSliderClipWidth(slider:View): Float{
////        Toast.makeText(this, "width : $timeLineWidth", Toast.LENGTH_SHORT).show()
//        if(timeLineWidth > -1){
//            if (slider.x+slider.width > timeLineWidth)
////                Toast.makeText(this, "Out of bounds", Toast.LENGTH_SHORT).show()
//                return slider.x+slider.width - timeLineWidth
//        }
//        return -1f
//    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        scaleGestureDetector.onTouchEvent(event)
//        return true
//    }

    //    private inner class ScaleListener(slider: View): ScaleGestureDetector.SimpleOnScaleGestureListener()
//    {
//        val slider = slider
//        var scaleFactor = 1.0f
//
//        override fun onScale(detector: ScaleGestureDetector?): Boolean {
//            scaleFactor *= detector!!.scaleFactor
//            scaleFactor = max(0.1f, min(scaleFactor, 2.0f))
//            return true
//        }
//    }





}






