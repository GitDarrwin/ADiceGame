// link to video : https://www.youtube.com/watch?v=qzr9BbBAUzg

package com.example.propercw

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

private var mediaPlayer: MediaPlayer? = null            //the sound effect



class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.coin)
        val start_button = findViewById<Button>(R.id.btn_start)         //start button initialization


        val et_name = findViewById<EditText>(R.id.et_name)
        val et_target = findViewById<EditText>(R.id.et_target)

        val btn_about = findViewById<Button>(R.id.btn_about)

        btn_about.setOnClickListener{

            //open the about popup with the button click
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Message")
            builder.setMessage("I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.")
            builder.setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
//open the about activity with the button click
//            var intent = Intent(this, AboutActivity::class.java)
//            startActivity(intent)
        }

        start_button.setOnClickListener{
            if(et_name.text.toString().isEmpty()){      //pop-up validation if name isn't entered in the field
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()

            } else {

                val intent = Intent(this, DiceActivity::class.java)     //diceactivity activity creation
                intent.putExtra("username", et_name.text.toString())            //send username variable in the intent
                if(!et_target.text.toString().isEmpty()){ //if the target textfield has content, send it through the intent
                    if(!isEditTextOnlyNumbers(et_target)){
                        Toast.makeText(this,"Please enter only numbers", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        intent.putExtra("target", et_target.text.toString().toInt())
                        startActivity(intent)
                        finish()
                    }
                    }
                else{
                    startActivity(intent)
                    finish()
                }
                }

        }

    }

    fun isEditTextOnlyNumbers(editText: EditText): Boolean {
        val input = editText.text.toString()
        val pattern = "\\d+".toRegex() // regex pattern to match only digits

        return pattern.matches(input)
    }

    override fun onStart() {            //start the music onStart()
        super.onStart()
        mediaPlayer?.start()
    }

    override fun onStop() {         //pause it onPause()
        super.onStop()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {      //completely remove the sound it with onDestroy
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}