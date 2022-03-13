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

    }


    lateinit var binding : ActivityMainBinding
//    lateinit var scaleGestureDetector: ScaleGestureDetector



    // alarm vars
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var picker : MaterialTimePicker

    var am: Boolean = true

    //


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val timeLine = findViewById<LinearLayout>(R.id.hour_layout_id)

        addHours(timeLine)
        createNotificationChannel()

        constraintLayout1 = findViewById(R.id.slider_holder1)
        constraintLayout2= findViewById(R.id.slider_holder2)
        constraintLayout3= findViewById(R.id.slider_holder3)

        val alarmTV1: TextView = findViewById(R.id.alarm1_tv)
        val alarmTV2: TextView = findViewById(R.id.alarm2_tv)
        val alarmTV3: TextView = findViewById(R.id.alarm3_tv)

        alarmTextViews.add(alarmTV1)
        alarmTextViews.add(alarmTV2)
        alarmTextViews.add(alarmTV3)

        val amSwitch = findViewById<Switch>(R.id.am_toggle)

        amSwitch.setOnClickListener {
            am = amSwitch.isChecked
            if(am){
                constraintLayout1!!.setBackgroundColor(Color.rgb(235,254,255))
                constraintLayout2!!.setBackgroundColor(Color.rgb(235,254,255))
                constraintLayout3!!.setBackgroundColor(Color.rgb(235,254,255))

            }else{
                constraintLayout1!!.setBackgroundColor(Color.rgb(238,212,191))
                constraintLayout2!!.setBackgroundColor(Color.rgb(238,212,191))
                constraintLayout3!!.setBackgroundColor(Color.rgb(238,212,191))

            }


        }

        timeLine.viewTreeObserver.addOnGlobalLayoutListener {
            Log.i("Width", timeLine.width.toString())
            timeLineWidth = timeLine.width.toFloat()
        }

        handleAddRemoveButtons()

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

    fun handleAddRemoveButtons(){
        val addButton1 : FloatingActionButton = findViewById(R.id.add_button1)
        val addButton2 : FloatingActionButton = findViewById(R.id.add_button2)
        val addButton3 : FloatingActionButton = findViewById(R.id.add_button3)

        val removeButton1 : FloatingActionButton = findViewById(R.id.remove_button1)
        val removeButton2 : FloatingActionButton = findViewById(R.id.remove_button2)
        val removeButton3 : FloatingActionButton = findViewById(R.id.remove_button3)
        removeButton1.hide()
        removeButton2.hide()
        removeButton3.hide()

        val durPicker: NumberPicker = findViewById(R.id.duration_picker)

        var sliderWidth = 90

        durPicker.minValue = 1
        durPicker.maxValue = 4


        durPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            sliderWidth = getSliderWidth(newVal)
        }

        addButton1.setOnClickListener(){
            alarm1Sliders.add(addSlider(constraintLayout1!! , sliderWidth, 0f))
            checkSliderOverLap(alarm1Sliders[0])
            addButton1.hide()
            removeButton1.show()
        }
        addButton2.setOnClickListener(){
            alarm2Sliders.add(addSlider(constraintLayout2!! , sliderWidth, 0f))
            checkSliderOverLap(alarm2Sliders[0])
            addButton2.hide()
            removeButton2.show()
        }
        addButton3.setOnClickListener(){
            alarm3Sliders.add(addSlider(constraintLayout3!! , sliderWidth, 0f))
            checkSliderOverLap(alarm3Sliders[0])
            addButton3.hide()
            removeButton3.show()
        }


        removeButton1.setOnClickListener(){
            removeSlider(constraintLayout1!!, alarm1Sliders[0])
            alarm1Sliders.removeAt(0)
            addButton1.show()
            removeButton1.hide()
        }
        removeButton2.setOnClickListener(){
            removeSlider(constraintLayout2!!, alarm2Sliders[0])
            alarm2Sliders.removeAt(0)
            addButton2.show()
            removeButton2.hide()
        }
        removeButton3.setOnClickListener(){
            removeSlider(constraintLayout3!!, alarm3Sliders[0])
            alarm3Sliders.removeAt(0)
            addButton3.show()
            removeButton3.hide()
        }
    }

    fun updateTime(slider:View){
        for(i in 0 until hours){
            if(i == getHour(slider.x).toInt() - 1 || i == getHour(slider.x + slider.width).toInt() - 1)
            {
                hoursTextViewList[i].setTextColor(Color.rgb(37,161,142))
            }
            else{
                hoursTextViewList[i].setTextColor(Color.rgb(244,145,89))
            }

        }
        updateTextView()
    }



    fun resetTimelineColor(){
        for(i in 0 until hours){
            hoursTextViewList[i].setTextColor(Color.rgb(244,145,89))
        }
    }

    fun getSliderWidth(dur: Int) : Int{
        return ((timeLineWidth/12) * dur).toInt()
    }

    fun getHour(position: Float):Double{
        if(position <= 0f){
            return 1.0
        }
        if(position >= timeLineWidth){
            return 12.0
        }
        val hour = position / (timeLineWidth / 12) +1
        return hour.toDouble()
    }


    fun getMinute(position: Float):Double{
        val minute =  (position / ( timeLineWidth / 720)) % 60
        return minute.toDouble()
    }

    fun getAlarmTime(position: Float) : String = String.format("%02d:%02d",getHour(position).toInt(), getMinute(position).toInt())

    private fun updateTextView(){
        for(i in alarm1Sliders){
            alarmTextViews[0].text = "Next Alarm: ${getAlarmTime(i.x)} - ${getAlarmTime(i.x +i.width)}"
        }
        for(i in alarm2Sliders){
            alarmTextViews[1].text =  "Next Alarm: ${getAlarmTime(i.x)} - ${getAlarmTime(i.x +i.width)}"
        }
        for(i in alarm3Sliders){
            alarmTextViews[2].text =  "Next Alarm: ${getAlarmTime(i.x)} - ${getAlarmTime(i.x +i.width)}"
        }
    }


    fun addHours(hourLayout: LinearLayout){
        for(i in 1..hours ){
            val newHourView = TextView(this)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,0,15,0)
//            newHourView.layoutParams = params
            newHourView.text = i.toString()
            newHourView.textSize = 24f
            newHourView.gravity = Gravity.CENTER
            newHourView.setTextColor(Color.rgb(244,145,89))
            newHourView.setTypeface(Typeface.DEFAULT_BOLD)
            newHourView.id = i
            newHourView.setPadding(16,0,16,0)
            hoursTextViewList.add(newHourView)
            hourLayout.addView(newHourView)
        }
    }

    fun addSlider(constraintLayout: ConstraintLayout , width: Int, xPos: Float): View{
        val newSlider = View(this)
        val params = ConstraintLayout.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT)
        newSlider.layoutParams = params
        val idGen = View.generateViewId()
        newSlider.id = idGen
        newSlider.setBackgroundColor(getColor(R.color.primaryGreen))
        newSlider.x = xPos
        val constraintParams = newSlider.layoutParams as ConstraintLayout.LayoutParams
        constraintParams.startToStart = constraintLayout.id
        constraintParams.topToTop = constraintLayout.id
        constraintParams.bottomToBottom = constraintLayout.id
        newSlider.requestLayout()
        constraintLayout.addView(newSlider)
        newSlider.setOnTouchListener(this)
        
//        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(newSlider))
        return newSlider
    }

    fun checkSliderOverLap(slider: View): Boolean{
        val alarmSliders: Array<ArrayList<View>> = arrayOf(alarm1Sliders,alarm2Sliders,alarm3Sliders)
        for(i in 0..alarmSliders.size-1){
            if(!alarmSliders[i].isEmpty() && alarmSliders[i] != slider)
            {
                for(j in 0..alarmSliders.size-1){
                    if (!alarmSliders[j].isEmpty() && slider != alarmSliders[j] ){
                        if (slider.x > alarmSliders[j].get(0).x &&
                            slider.x < alarmSliders[j].get(0).x + alarmSliders[j].get(0).width
                        ) {
                            slider.x = alarmSliders[j].get(0).x + alarmSliders[j].get(0).width+1
                            Toast.makeText(this, "Cannot Overlap Alarms", Toast.LENGTH_SHORT).show()
                            return true
                        } else if ((slider.x + slider.width) > alarmSliders[j].get(0).x &&
                            (slider.x + slider.width) < alarmSliders[j].get(0).x + alarmSliders[j].get(0).width
                        ) {
                            slider.x =  alarmSliders[j].get(0).x - slider.x + slider.width
                            Toast.makeText(this, "Cannot Overlap Alarms", Toast.LENGTH_SHORT).show()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }


    fun removeSlider(constraintLayout: ConstraintLayout, slider: View): Boolean{
        if(constraintLayout != null){
            constraintLayout.removeView(slider)
            return true
        }
        return false
    }

    // *********************************Functions for the alarm Notification**************************************

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)

        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "alarm cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )
//        Toast.makeText(this, "alarm set success", Toast.LENGTH_SHORT).show()

    }

    private fun slideTimePicker(view: View) {

            var setHour = getHour(view.x).toInt()
            var setMinute = getMinute(view.x).toInt()

            if(!am){
                setHour += 12
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = setHour
            calendar[Calendar.MINUTE] = setMinute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
             Toast.makeText(this, "${setHour}:${setMinute
             }", Toast.LENGTH_SHORT).show()
    }

    // create channel for notification.
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "alarm"
            val desc = "channel for alarm"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("notficationChannel", name, importance)
            channel.description = desc
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


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






