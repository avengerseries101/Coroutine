package com.example.kotlincoroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button: Button = findViewById(R.id.btn_click)
        textView = findViewById(R.id.tv_text)

        button.setOnClickListener {
            /*  Types of Coroutine scope :-
                IO - input output, or network request, local database interaction
                Main - doing work on the main thread, interacting with the UI
                Default - for any heavy competition work, like filter a large list, pick things out in a range in different ways
            */
            CoroutineScope(IO).launch {
                dataRequest()
            }
        }
    }

    private fun setNewTextData(input: String) {
        val newText = textView.text.toString() + "\n$input"
        textView.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewTextData(input)
        }
    }

    private suspend fun dataRequest() {
        val data1 = getFirstSampleData()
        Log.d("@@@@@", "Debug : data1 : $data1")
        setTextOnMainThread(data1)

        val data2 = getSecondSampleData()
        Log.d("@@@@@", "Debug : data2 : $data2")
        setTextOnMainThread(data2)
    }

    /*  by putting suspend keyword we mean that it can be used in coroutine now i.e called in coroutine and executed in the background.
        delay(1000)   - delay the coroutine
        Thread.sleep(1000)  - whole thread will sleep, if it carries no of coroutines then all of them will sleep
    */

    private suspend fun getFirstSampleData(): String {
        logThread("getFirstSampleData")
        delay(1000)
        return "First Data"
    }

    private suspend fun getSecondSampleData(): String {
        logThread("getSecondSampleData")
        delay(1000)
        return "Second Data"
    }

    private fun logThread(methodName: String) {
        Log.d("@@@@@", "Debug : $methodName : ${Thread.currentThread().name}")
    }
}
