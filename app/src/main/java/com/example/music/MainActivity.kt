package com.example.music

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create(this,R.raw.music)
        mp.isLooping = true
        mp.setVolume(0.5f,0.5f)
        totalTime = mp.duration

        //Volume Bar
        volumeBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if(fromUser) {
                            var volumeBar = progress / 100.0f
                            mp.setVolume(volumeBar,volumeBar)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                }
        )

        // Position Bar
        posBar.max = totalTime
        posBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if(fromUser)
                        {
                            mp.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                }
        )

        //Thread
        Thread(Runnable {
            while(mp!=null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e : InterruptedException) {

                }
            }
        }).start()
    }

    @SuppressLint("Handler Leak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            //Update Postition Bar
            posBar.progress = msg.what

            //Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = remainingTime
        }
    }

    private fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60
        timeLabel = "$min:"
        if(sec<10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun playBtnClick(view: View) {
        if(mp.isPlaying)
        {
            //Stop
            mp.pause()
            playBtnn.setBackgroundResource(R.drawable.play)
        } else {
            // Start
            mp.start()
            playBtnn.setBackgroundResource(R.drawable.stop)
        }
    }
}