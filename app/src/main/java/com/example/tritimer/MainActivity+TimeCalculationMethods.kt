package com.example.tritimer

fun MainActivity.getHour(position: Float):Double{
    if(position <= 0f){
        return 1.0
    }
    if(position >= MainActivity.timeLineWidth){
        return 12.0
    }
    val hour = position / (MainActivity.timeLineWidth / 12) +1
    return hour.toDouble()
}

fun MainActivity.getMinute(position: Float):Double{
    val minute =  (position / ( MainActivity.timeLineWidth / 720)) % 60
    return minute.toDouble()
}

fun MainActivity.getAlarmTime(position: Float) : String = String.format("%02d:%02d",getHour(position).toInt(), getMinute(position).toInt())
