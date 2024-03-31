package com.android.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CountdownReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action
            when (action) {
                "COUNTDOWN_COMPLETE" -> handleCountdownComplete(context, it)
                "PRIORITY_ACTION" -> priortyHandler(context, it)

            }
        }
    }

    private fun handleCountdownComplete(context: Context?, intent: Intent) {
        var startTime = intent.getStringExtra("getTime")
        Toast.makeText(context, "Timer completed $startTime ", Toast.LENGTH_SHORT).show()




    }

    private fun priortyHandler(context: Context?, intent: Intent) {
        val startTime = intent.getStringExtra("getTime")
        val key = intent.getStringExtra("key")
        if(key == "priority"){

            Runtime.getRuntime().exec(arrayOf("/system/bin/sh","-c","log Timer Started:$startTime"))
            Toast.makeText(context, "Priority Timer Completed ", Toast.LENGTH_SHORT).show()

        }


    }



}
