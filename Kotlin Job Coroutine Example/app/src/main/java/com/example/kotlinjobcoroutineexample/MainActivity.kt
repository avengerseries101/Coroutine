package com.example.kotlinjobcoroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private val progressMax = 100
    private val progressStart = 0
    private val jobTime = 4000 // ms
    private lateinit var job: CompletableJob

    private val TAG: String = "main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_job.setOnClickListener {
            if (!::job.isInitialized) {
                initJob()
            }
            progressBar_job.startJobOrCancel(job)
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting Job"))
        }
        initJob()
    }

    private fun initJob() {
        button_job.text = "Start Job #1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "Unknown cancellation error."
                }
                Log.e(TAG, "$job was cancelled. Reason: $msg")
                showToast(msg)
            }
        }
        progressBar_job.max = progressMax
        progressBar_job.progress = progressStart
    }


    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            Log.d(TAG, "$job is already active. Cancelling...")
            resetJob()
        } else {
            button_job.text = "Cancel Job #1"
            CoroutineScope(IO + job).launch {
                Log.d(TAG, "coroutine $this is activated with job $job.")

                for (i in progressStart..progressMax) {
                    delay((jobTime / progressMax).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete!")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Main) {
            textView_jobComplete.text = text
        }
    }

    private fun showToast(textMsg: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@MainActivity, textMsg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}

