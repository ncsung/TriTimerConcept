package com.example.tritimer

import android.graphics.Color
import android.widget.NumberPicker
import android.widget.Switch
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun MainActivity.handleAddRemoveButtons(){
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
        MainActivity.alarm1Sliders.add(addSlider(MainActivity.constraintLayout1!! , sliderWidth, 0f))
        checkSliderOverLap(MainActivity.alarm1Sliders[0])
        addButton1.hide()
        removeButton1.show()
    }
    addButton2.setOnClickListener(){
        MainActivity.alarm2Sliders.add(addSlider(MainActivity.constraintLayout2!! , sliderWidth, 0f))
        checkSliderOverLap(MainActivity.alarm2Sliders[0])
        addButton2.hide()
        removeButton2.show()
    }
    addButton3.setOnClickListener(){
        MainActivity.alarm3Sliders.add(addSlider(MainActivity.constraintLayout3!! , sliderWidth, 0f))
        checkSliderOverLap(MainActivity.alarm3Sliders[0])
        addButton3.hide()
        removeButton3.show()
    }


    removeButton1.setOnClickListener(){
        removeSlider(MainActivity.constraintLayout1!!, MainActivity.alarm1Sliders[0])
        MainActivity.alarm1Sliders.removeAt(0)
        addButton1.show()
        removeButton1.hide()
    }
    removeButton2.setOnClickListener(){
        removeSlider(MainActivity.constraintLayout2!!, MainActivity.alarm2Sliders[0])
        MainActivity.alarm2Sliders.removeAt(0)
        addButton2.show()
        removeButton2.hide()
    }
    removeButton3.setOnClickListener(){
        removeSlider(MainActivity.constraintLayout3!!, MainActivity.alarm3Sliders[0])
        MainActivity.alarm3Sliders.removeAt(0)
        addButton3.show()
        removeButton3.hide()
    }

    fun MainActivity.handleAmToggle(){
        val amSwitch = findViewById<Switch>(R.id.am_toggle)
        amSwitch.setOnClickListener {
            MainActivity.am = amSwitch.isChecked
            if(MainActivity.am){
                MainActivity.constraintLayout1!!.setBackgroundColor(Color.rgb(235,254,255))
                MainActivity.constraintLayout2!!.setBackgroundColor(Color.rgb(235,254,255))
                MainActivity.constraintLayout3!!.setBackgroundColor(Color.rgb(235,254,255))

            }else{
                MainActivity.constraintLayout1!!.setBackgroundColor(Color.rgb(238,212,191))
                MainActivity.constraintLayout2!!.setBackgroundColor(Color.rgb(238,212,191))
                MainActivity.constraintLayout3!!.setBackgroundColor(Color.rgb(238,212,191))

            }


        }
    }
}