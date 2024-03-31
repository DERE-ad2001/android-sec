package com.android.timer

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {
    private lateinit var minutesEditText: EditText
    private lateinit var startButton: Button
    private lateinit var timeView: TextView
    private var timer: CountDownTimer? = null
    private var alertDialog: AlertDialog? = null
    private lateinit var receiver: CountdownReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        minutesEditText = findViewById(R.id.minutesEditText)
        startButton = findViewById(R.id.startButton)
        timeView = findViewById(R.id.timeView)

        receiver = CountdownReceiver()
        registerReceiver(receiver, IntentFilter("COUNTDOWN_COMPLETE"))
        registerReceiver(receiver, IntentFilter("PRIORITY_ACTION"))

        startButton.setOnClickListener {
            showStartDialog()
        }
    }

    private fun showStartDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Start Timer")
        alertDialogBuilder.setMessage("Choose the type of timer:")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.HORIZONTAL

        val timerButton = Button(this)
        timerButton.text = "Timer"
        timerButton.setOnClickListener {
            val inputMinutes = minutesEditText.text.toString().toLongOrNull() ?: 0
            val durationInMillis = inputMinutes * 60 * 1000L
            startTimer(durationInMillis, inputMinutes)
            alertDialog?.dismiss()
        }

        val priorityTimerButton = Button(this)
        priorityTimerButton.text = "Priority Timer"
        priorityTimerButton.setOnClickListener {
            showDisabledFeatureToast()
            alertDialog?.dismiss()
        }

        layout.addView(timerButton)
        layout.addView(priorityTimerButton)
        alertDialogBuilder.setView(layout)

        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }

    private fun startTimer(duration: Long, inputMinutes: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeView.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                timeView.text = "00:00"
                Finished(inputMinutes)
            }
        }.start()
    }

    private fun Finished(inputMinutes: Long) {
        Intent("COUNTDOWN_COMPLETE").also { intent ->
            intent.putExtra("getTime", inputMinutes.toString())
            sendBroadcast(intent)
        }
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun showDisabledFeatureToast() {
        Toast.makeText(this, "This feature has been disabled due to security implications", Toast.LENGTH_SHORT).show()
    }
}
