package com.example.tritimer

import android.content.Context
import android.graphics.Color
import android.graphics.ColorSpace
import android.graphics.Typeface
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.coroutines.coroutineContext

class SliderHolder(constraintLayout: ConstraintLayout, context: Context) : ConstraintLayout(context), View.OnTouchListener {
    init {
        var wrappedAround: Boolean = false
        val constraintLayout = constraintLayout
        val context = context
    }

    companion object sliderManager{
        var sliderArray: ArrayList<View> = arrayListOf()
    }

    fun addSlider(constraintLayout: ConstraintLayout , width: Int, xPos: Float){
        val newSlider = View(context)
        val params = ConstraintLayout.LayoutParams(width,ViewGroup.LayoutParams.MATCH_PARENT)
        newSlider.layoutParams = params
        val idGen = View.generateViewId()
        newSlider.id = idGen
        newSlider.setBackgroundColor()

        newSlider.x = xPos

        val constraintParams = newSlider.layoutParams as ConstraintLayout.LayoutParams

        constraintParams.startToStart = constraintLayout.id
        constraintParams.topToTop = constraintLayout.id
        constraintParams.bottomToBottom = constraintLayout.id

        newSlider.requestLayout()

        constraintLayout.addView(newSlider)
        sliderArray.add(newSlider)

        newSlider.setOnTouchListener(this)
    }

    private fun handleSliderClip(constraintLayout: ConstraintLayout, slider:View)
//    private fun handleSliderClip()
    {
        if (getSliderClipWidth(slider) > 0) {
            addSlider(constraintLayout , slider.width, 0f)
        }
    }

    private fun getSliderClipWidth(slider:View): Float{
        if(MainActivity.timeLineWidth > -1){
            if (slider.x+slider.width > MainActivity.timeLineWidth)
                return slider.x+slider.width - MainActivity.timeLineWidth
        }
        return -1f
    }

    override fun onTouch(view: View?, p1: MotionEvent?): Boolean {
        if (view != null && p1 != null) {
            val action = p1.action
            return when (action) {
                MotionEvent.ACTION_UP -> {
                    getSliderClipWidth(view)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    MainActivity.dx = p1.x - MainActivity.touchx
                    view.x = view.x + MainActivity.dx
                    MainActivity.touchx = p1.x
                    MainActivity.updateTime(view)
                    handleSliderClip(MainActivity.constraintLayout1!! ,view)

                    true
                }
                MotionEvent.ACTION_DOWN -> {
                    MainActivity.touchx = p1.x
                    true
                }
                else -> true
            }
        }
        return false
    }




}