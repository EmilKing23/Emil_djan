package com.example.accelerationtestapp

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var sensorManager: SensorManager
    private var magnetic = FloatArray(9)
    private var gravity = FloatArray(9)

    private var accsr = FloatArray(3)
    private var magf = FloatArray(3)
    private var values = FloatArray(3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvSensor = findViewById<TextView>(R.id.tvSensor)
        val lRotations = findViewById<LinearLayout>(R.id.lRotations)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensor2 =  sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sensorListener = object : SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
               when (event?.sensor?.type){
                   Sensor.TYPE_ACCELEROMETER-> accsr = event.values.clone()
                   Sensor.TYPE_MAGNETIC_FIELD-> magf = event.values.clone()
               }

                SensorManager.getRotationMatrix(gravity, magnetic, accsr, magf)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(gravity,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    outGravity
                )
                SensorManager.getOrientation(outGravity, values)
                val degree =  values[2] * 57.2958f
                val rotate = 270 + degree
                lRotations.rotation = rotate
                val rData = 90 + degree
                val color = if (rData.toInt() == 0 ){
                    Color.GREEN
                } else{
                        Color.RED
                }
                lRotations.setBackgroundColor(color)
                tvSensor.text = rData.toInt().toString()
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

        }
        sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(sensorListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL)

    }
}