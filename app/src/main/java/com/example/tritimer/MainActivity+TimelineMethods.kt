package com.example.tritimer

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

fun MainActivity.addHours(hourLayout: LinearLayout){
    for(i in 1..MainActivity.hours){
        val newHourView = TextView(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0,0,15,0)
//            newHourView.layoutParams = params
        newHourView.text = i.toString()
        newHourView.textSize = 24f
        newHourView.gravity = Gravity.CENTER
        newHourView.setTextColor(Color.rgb(244,145,89))
        newHourView.setTypeface(Typeface.DEFAULT_BOLD)
        newHourView.id = i
        newHourView.setPadding(16,0,16,0)
        MainActivity.hoursTextViewList.add(newHourView)
        hourLayout.addView(newHourView)
    }
}

fun MainActivity.updateTime(slider: View){
    for(i in 0 until MainActivity.hours){
        if(i == getHour(slider.x).toInt() - 1 || i == getHour(slider.x + slider.width).toInt() - 1)
        {
            MainActivity.hoursTextViewList[i].setTextColor(Color.rgb(37,161,142))
        }
        else{
            MainActivity.hoursTextViewList[i].setTextColor(Color.rgb(244,145,89))
        }

    }
    updateTextView()
}

fun MainActivity.resetTimelineColor(){
    for(i in 0 until MainActivity.hours){
        MainActivity.hoursTextViewList[i].setTextColor(Color.rgb(244,145,89))
    }
}

fun MainActivity.updateTextView(){
    for(i in MainActivity.alarm1Sliders){
        MainActivity.alarmTextViews[0].text = "Next Alarm: ${getAlarmTime(i.x)} - ${getAlarmTime(i.x +i.width)}"
    }
    for(i in MainActivity.alarm2Sliders){
        MainActivity.alarmTextViews[1].text =  "Next Alarm: ${getAlarmTime(i.x)} - ${getAlarmTime(i.x +i.width)}"
    }
    for(i in MainActivity.alarm3Sliders){
        MainActivity.alarmTextViews[2].text =  "Next Alarm: ${getAlarmTime(i.x)} - ${getAlarmTime(i.x +i.width)}"
    }
}


