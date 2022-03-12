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


class MainActivity : AppCompatActivity(), View.OnTouchListener {

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

    fun addSlider(constraintLayout: ConstraintLayout , width: Int, xPos: Float){
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
    }


    private fun handleSliderClip(constraintLayout: ConstraintLayout, slider:View)
//    private fun handleSliderClip()
    {
        if (getSliderClipWidth(slider) > 0) {
            addSlider(constraintLayout , slider.width, 0f)
            Toast.makeText(this, "${constraintLayout.x}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getSliderClipWidth(slider:View): Float{
//        Toast.makeText(this, "width : $timeLineWidth", Toast.LENGTH_SHORT).show()
        if(timeLineWidth > -1){
            if (slider.x+slider.width > timeLineWidth)
//                Toast.makeText(this, "Out of bounds", Toast.LENGTH_SHORT).show()
                return slider.x+slider.width - timeLineWidth
        }
        return -1f
    }

//    private fun convertDPtoPX(dp:Int): Float{
//        return dp * (this.getResources().getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)
//    }
//
//    private fun convertPXtoDP(px:Int): Int{
//        return (px / (this.getResources()
//            .getDisplayMetrics().densityDpi as Float / DisplayMetrics.DENSITY_DEFAULT)).toInt()
//    }

    override fun onTouch(view: View?, p1: MotionEvent?): Boolean {
        if (view != null && p1 != null) {
            val action = p1.action
            return when (action) {
                MotionEvent.ACTION_UP -> {
                    getSliderClipWidth(view)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    dx = p1.x - touchx
                    view.x = view.x + dx
                    touchx = p1.x
                    updateTime(view)
                    handleSliderClip(constraintLayout1!! ,view)

                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    touchx = p1.x
                    true
                }
                else -> true
            }
        }
        return false
    }

}






