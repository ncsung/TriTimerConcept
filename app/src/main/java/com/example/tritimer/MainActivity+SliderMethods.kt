package com.example.tritimer

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

fun MainActivity.getSliderWidth(dur: Int) : Int{
    return ((MainActivity.timeLineWidth /12) * dur).toInt()
}

fun MainActivity.addSlider(constraintLayout: ConstraintLayout, width: Int, xPos: Float): View {
    val newSlider = View(this)
    val params = ConstraintLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT)
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

fun MainActivity.checkSliderOverLap(slider: View): Boolean{
    val alarmSliders: Array<ArrayList<View>> = arrayOf(
        MainActivity.alarm1Sliders,
        MainActivity.alarm2Sliders,
        MainActivity.alarm3Sliders
    )
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


fun MainActivity.removeSlider(constraintLayout: ConstraintLayout, slider: View): Boolean{
    if(constraintLayout != null){
        constraintLayout.removeView(slider)
        return true
    }
    return false
}