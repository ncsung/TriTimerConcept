package com.example.tritimer

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.coroutines.coroutineContext

class SliderHolder(constraintLayout: ConstraintLayout, context: Context) : ConstraintLayout(context) {
    init {
        var wrappedAround: Boolean = false
        val constraintLayout = constraintLayout
        val context = context
    }

    companion object sliderManager{
        var sliderArray: ArrayList<View> = arrayListOf()
    }



}