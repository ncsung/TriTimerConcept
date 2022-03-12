package com.example.tritimer

import android.content.Context
import android.view.View
import java.util.*

// An Activity
class Activity(context: Context, val activity_name: String, val start: Float, val end: Float, val duration: Float) : View(context) {
    val slider: View = View(context)

}