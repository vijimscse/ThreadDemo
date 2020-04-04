package com.learn.threaddemo

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity.javaClass.simpleName
    }

    private lateinit var handlerThread: HandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        handlerThread = HandlerThread("").apply {
//            start()
//            customHandler = CustomHandler(looper)
//        }
//
//        customHandler?.obtainMessage()?.also { msg ->
//            msg.arg1 = 100
//            customHandler?.sendMessage(msg)
//        }
//
//        val customThread = CustomThread()
//        customThread.start()


        val looper = LooperThread();
        looper.start()

        text.post(Runnable {
            LooperThread2(looper.mHandler).start()
        })
    }

    class LooperThread : Thread("Viji thread") {
        lateinit var mHandler: Handler
        override fun run() {
            Looper.prepare()
            mHandler = object : Handler() {
                override fun handleMessage(msg: Message?) {
                    Log.d(TAG, "Looper name " + looper.thread.name)
                    Log.d(TAG, "Inside handle message " + msg?.what)
                    // text.text = "Hello"
                    looper.thread.interrupt()

                }
            }
            Looper.loop()
        }
    }

    class LooperThread2(private val handler: Handler?) : Thread() {

        override fun run() {
            handler?.sendEmptyMessage(100)
        }
    }
}
