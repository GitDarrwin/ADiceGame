package com.example.propercw

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class EndActivity : AppCompatActivity() {

    companion object {                      //companion object to hold player and cpu win variables throughout all activities
        var currentPlayerWins: Int = 0
        var currentCPUWins: Int = 0
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        var mediaPlayer: MediaPlayer? = null
        var mediaPlayer2: MediaPlayer? = null

        mediaPlayer = MediaPlayer.create(this, R.raw.win)

        mediaPlayer2 = MediaPlayer.create(this, R.raw.watthehell)


        var score_tv = findViewById<TextView>(R.id.scoreboard)

        var back_button = findViewById<Button>(R.id.btn_back)

        val username = intent.getStringExtra("username")

        val player_score = intent.getIntExtra("player_score", 0)

        val cpu_score = intent.getIntExtra("cpu_score", 0)

        val win = intent.getBooleanExtra("win_flag", true)

        var username_tv = findViewById<TextView>(R.id.tv_username)

        var win_tv = findViewById<TextView>(R.id.tv_win)

        var player_score_tv = findViewById<TextView>(R.id.tv_player_score)

        var cpu_score_tv = findViewById<TextView>(R.id.tv_cpu_score)

        var image = findViewById<ImageView>(R.id.iv_trophy)

        if(win){                    //win condition
            mediaPlayer.start()
            win_tv.text = "You win!"
            win_tv.setTextColor(Color.GREEN)
            image.setImageResource(R.drawable.happy)
            currentPlayerWins++
            score_tv.text = "H : ${currentPlayerWins} / C : ${currentCPUWins}"



        }

        else{                   //lose condition
            mediaPlayer2.start()
            win_tv.text = "You lose!"
            win_tv.setTextColor(Color.RED)
            image.setImageResource(R.drawable.sad)
            currentCPUWins++
            score_tv.text = "H : ${currentPlayerWins} / C : ${currentCPUWins}"
        }

        username_tv.text = username

        player_score_tv.text = "Player score - ${player_score}"

        cpu_score_tv.text = "CPU score - ${cpu_score}"

        back_button.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}