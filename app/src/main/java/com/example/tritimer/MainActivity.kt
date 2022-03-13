package com.example.tritimer

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.tritimer.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    companion object{
        val hoursTextViewList: ArrayList<TextView> = ArrayList()
        val hourArrPositions: ArrayList<Float> = ArrayList()
        var touchx: Float = 0f
        var touchy: Float = 0f
        var dx: Float = 0f
        var dy: Float = 0f
        val hours = 12
        var timeLineWidth: Float = -1f
        lateinit var binding : ActivityMainBinding
        var constraintLayout1: ConstraintLayout?? = null
        var constraintLayout2: ConstraintLayout?? = null
        var constraintLayout3: ConstraintLayout?? = null


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val timeLine = findViewById<LinearLayout>(R.id.hour_layout_id)
        addHours(timeLine)

        MainActivity.constraintLayout1 = findViewById(R.id.slider_holder1)
        MainActivity.constraintLayout2= findViewById(R.id.slider_holder2)
        MainActivity.constraintLayout3= findViewById(R.id.slider_holder3)

        timeLine.viewTreeObserver.addOnGlobalLayoutListener {
            Log.i("Width", timeLine.width.toString())
            addWidthsToArray(timeLine.width.toFloat())
            timeLineWidth = timeLine.width.toFloat()
        }

        for(i in hourArrPositions){
            Log.i("ArryWidths", i.toString())
        }

        connectAlarmButtons()

    }

    fun connectAlarmButtons(){

        val fButton1 : FloatingActionButton = findViewById(R.id.add_button1)
        val fButton2 : FloatingActionButton = findViewById(R.id.add_button2)
        val fButton3 : FloatingActionButton = findViewById(R.id.add_button3)

        fButton1.setOnClickListener(){
            addSlider(constraintLayout1!! , 500, 0f)
        }
        fButton2.setOnClickListener(){
            addSlider(constraintLayout2!! , 100, 0f)
        }
        fButton3.setOnClickListener(){
            addSlider(constraintLayout3!! , 100, 0f)
        }
    }

    fun updateTime(slider:View){
        val minMaxArr = findMinMaxHour(slider)
        for(i in 0 until hours){
            if(i >= minMaxArr[0] && i <= minMaxArr[1])
            {
                hoursTextViewList[i].setTextColor(Color.GREEN)
            }
            else{
                hoursTextViewList[i].setTextColor(Color.WHITE)
            }
        }
    }

    private fun findMinMaxHour(slider:View): Array<Int>{
        val arr: Array<Int> = arrayOf(0,-1)
        var  minFound = false
        var  maxFound = false
        var firstSmallerValueTime = 0
        for(i in 0 until hours){
            if(slider.x + slider.width >= hourArrPositions[(hours-1)-i]){
                arr[1] = (hours)-i
                break
            }
        }
        if(slider.x > hourArrPositions[1]) {
            for (i in 0 until hours) {
                if (slider.x <= hourArrPositions[i]) {
                    arr[0] = i
                    break
                }
            }
        }

        return arr
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
            newHourView.setTextColor(Color.WHITE)
            newHourView.setTypeface(Typeface.DEFAULT_BOLD)
            newHourView.id = i
            newHourView.setPadding(16,0,16,0)
            hoursTextViewList.add(newHourView)
            hourLayout.addView(newHourView)
        }
    }

    fun addWidthsToArray(width:Float){
        val widthOfHour: Float = width / hours.toFloat()
        for(i in 0 until hours){
            hourArrPositions.add(widthOfHour *  i)
        }
    }

//    private fun convertDPtoPX(dp:Int): Float{
//        return dp * (this.getResources().getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)
//    }
//
//    private fun convertPXtoDP(px:Int): Int{
//        return (px / (this.getResources()
//            .getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)).toInt()
//    }


}






