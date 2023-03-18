package com.example.propercw

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.util.*

class DiceActivity : AppCompatActivity(), Serializable {            //serializable to improve performance

    var target : Int = 101          //default target variable
    var player_score : Int = 0
    var cpu_score : Int = 0
    var turn : Int = 0          //turn number variable

    private var mediaPlayer: MediaPlayer? = null            //sound and bgm effects
    private var mediaPlayer2: MediaPlayer? = null
    private var mediaPlayer3: MediaPlayer? = null
    private var mediaPlayer4: MediaPlayer? = null

    var player_rolls : Int = 3      //total rolls for cpu and player
    var cpu_rolls : Int = 3

    var p1_flag : Boolean = false           //flag variables for each dice image for player and cpu
    var p2_flag : Boolean = false
    var p3_flag : Boolean = false
    var p4_flag : Boolean = false
    var p5_flag : Boolean = false

    var c1_flag : Boolean = false           //flag variables for each dice image for player and cpu
    var c2_flag : Boolean = false
    var c3_flag : Boolean = false
    var c4_flag : Boolean = false
    var c5_flag : Boolean = false


    val dice_positions = arrayOf(           //array of dice position imageViews
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6
    )


    lateinit var roll_button: Button            //roll, ok and submit button initializations
    lateinit var submit_button: Button
    lateinit var ok_button: Button

    lateinit var cpu_dice1: ImageView           //cpu imageView initializations
    lateinit var cpu_dice2: ImageView
    lateinit var cpu_dice3: ImageView
    lateinit var cpu_dice4: ImageView
    lateinit var cpu_dice5: ImageView

    lateinit var turn_tv: TextView

    lateinit var player_dice1: ImageView        //player imageView initializations
    lateinit var player_dice2: ImageView
    lateinit var player_dice3: ImageView
    lateinit var player_dice4: ImageView
    lateinit var player_dice5: ImageView
    lateinit var usernametaken: String

    lateinit var cpu_reroll_tv: TextView
    lateinit var player_reroll_tv: TextView

    lateinit var player_score_tv: TextView
    lateinit var cpu_score_tv: TextView
    lateinit var target_tv: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

        if (savedInstanceState != null) {               //checks if the savedInstanceState bundle is null or not
            cpu_score = savedInstanceState.getInt("cpu_score")
            player_score = savedInstanceState.getInt("player_score")
            turn = savedInstanceState.getInt("turn")
            target = savedInstanceState.getInt("target")

            // Restore more key-value pairs as needed
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm)
        mediaPlayer?.isLooping = true           //loop the bgm

        mediaPlayer2 = MediaPlayer.create(this, R.raw.rollingdice)      //MediaPlayer sound effect initializations

        mediaPlayer3 = MediaPlayer.create(this, R.raw.click)

        mediaPlayer4 = MediaPlayer.create(this, R.raw.image_click)

        ok_button = findViewById<Button>(R.id.ok_button)
        roll_button = findViewById<Button>(R.id.roll_button)
        submit_button = findViewById<Button>(R.id.submit_button)

        player_reroll_tv = findViewById<TextView>(R.id.player_reroll_tv)
        usernametaken = intent.getStringExtra("username").toString()
        player_score_tv = findViewById<TextView>(R.id.player_score_tv)
        cpu_score_tv = findViewById<TextView>(R.id.cpu_score_tv)

        var player_tv = findViewById<TextView>(R.id.player_tv)

        player_tv.text = usernametaken

        target = intent.getIntExtra("target", 101)      //get the target variable from MainActivity

        cpu_dice1 = findViewById<ImageView>(R.id.c1)
        cpu_dice2 = findViewById<ImageView>(R.id.c2)
        cpu_dice3 = findViewById<ImageView>(R.id.c3)
        cpu_dice4 = findViewById<ImageView>(R.id.c4)
        cpu_dice5 = findViewById<ImageView>(R.id.c5)

        target_tv = findViewById<TextView>(R.id.target_tv)
        target_tv.text = "Target \n   $target"

        player_dice1 = findViewById<ImageView>(R.id.p1)
        player_dice2 = findViewById<ImageView>(R.id.p2)
        player_dice3 = findViewById<ImageView>(R.id.p3)
        player_dice4 = findViewById<ImageView>(R.id.p4)
        player_dice5 = findViewById<ImageView>(R.id.p5)

        turn_tv = findViewById<TextView>(R.id.turn_tv)

        submit_button.isEnabled = false         //turn off the submit and ok buttons before the first roll
        ok_button.isEnabled = false

        roll_button.setOnClickListener{
            if(player_rolls == 3){          //first roll
                mediaPlayer2?.start()
                cpu_roll()
                player_roll()
                submit_button.isEnabled = true
                roll_button.text = "REROLL"

                player_reroll_tv.text = "Player Rerolls = ${player_rolls-1}"
            }

            else if(player_rolls > 0){         //rerolls

                roll_button.isEnabled = true

                enableDice()
                layout()
                roll_button.isEnabled = false
                ok_button.isEnabled = true

                player_reroll_tv.text = "Player Rerolls = ${player_rolls-1}"
            }

            else{               //if reroll hits 0
                player_reroll_tv.text = "Player Rerolls = 0"
                ok_button.isEnabled = false
                roll_button.isEnabled = false
                removeOnClick()
                nullifyOnClick()


            }
            player_rolls --




        }

        submit_button.setOnClickListener{
            mediaPlayer3?.start()
            addPlayerScore(player_dice1)        //addPlayerScore for all dice
            addPlayerScore(player_dice2)
            addPlayerScore(player_dice3)
            addPlayerScore(player_dice4)
            addPlayerScore(player_dice5)


            turn ++                     //increment turn number
            turn_tv.text = "Turn \n    ${turn+1}"       //turn+1 because the first turn is 1

            player_score_tv.text = "Score - ${player_score}"
            cpuPlays()
            checkWin()
            resetRound()
        }

        ok_button.setOnClickListener{
            rerollSpecific()                //rereroll the specific imageviews clicked
            ok_button.isEnabled = false

            nullifyOnClick()
            removeOnClick()
            roll_button.isEnabled = true

            if(player_rolls == 0){
                roll_button.isEnabled = false

            }
        }

    }

    override fun onStart() {
        super.onStart()
        mediaPlayer?.start()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    override fun onSaveInstanceState(outState: Bundle) { //save the important variables in a bundle when activity is destroyed while moving landscape
        super.onSaveInstanceState(outState)
        outState.putInt("cpu_score", cpu_score)
        outState.putInt("player_score", player_score)
        outState.putInt("turn", turn)
        outState.putInt("target", target)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {  //load the important variables from a bundle when activity is created
        super.onRestoreInstanceState(savedInstanceState)
        resetRound()
        cpu_score = savedInstanceState.getInt("cpu_score")
        player_score = savedInstanceState.getInt("player_score")
        turn = savedInstanceState.getInt("turn")
        target = savedInstanceState.getInt("target")

        turn_tv.text = "Turn \n    ${turn+1}"
        target_tv.text = "Target \n   $target"
        player_score_tv.text = "Score - ${player_score}"
        cpu_score_tv.text = "Score - $cpu_score"

    }

    fun nullifyOnClick(){   //remove the onclick listeners for the player dice
        player_dice1.setOnClickListener(null)
        player_dice2.setOnClickListener(null)
        player_dice3.setOnClickListener(null)
        player_dice4.setOnClickListener(null)
        player_dice5.setOnClickListener(null)
    }

    fun removeOnClick(){                    //remove the flag for each variable that turned true if the imageView was clicked
        p1_flag = false
        player_dice1.setBackgroundResource(android.R.color.transparent)

        p2_flag = false
        player_dice2.setBackgroundResource(android.R.color.transparent)

        p3_flag = false
        player_dice3.setBackgroundResource(android.R.color.transparent)

        p4_flag = false
        player_dice4.setBackgroundResource(android.R.color.transparent)

        p5_flag = false
        player_dice5.setBackgroundResource(android.R.color.transparent)

    }

    fun checkWin(){                     //function to check if player or CPU won
        if(player_score > target && player_score > cpu_score){              //player wins
            var win : Boolean = true
            val intent = Intent(this, EndActivity::class.java)
            intent.putExtra("username", usernametaken)
            intent.putExtra("win_flag", win)
            intent.putExtra("player_score", player_score)
            intent.putExtra("cpu_score", cpu_score)
            startActivity(intent)
        }

        else if(cpu_score > target && player_score < cpu_score){            //cpu wins
            var win : Boolean = false
            val intent = Intent(this, EndActivity::class.java)
            intent.putExtra("username", usernametaken)
            intent.putExtra("win_flag", win)
            intent.putExtra("player_score", player_score)
            intent.putExtra("cpu_score", cpu_score)
            startActivity(intent)
        }

        else if (player_score > target && cpu_score > target && player_score == cpu_score){         //draw
            player_rolls = 1
            cpu_rolls = 1
        }
    }

    fun layout(){               //create a border
        player_dice1.setBackgroundResource(android.R.drawable.btn_default)

        player_dice2.setBackgroundResource(android.R.drawable.btn_default)

        player_dice3.setBackgroundResource(android.R.drawable.btn_default)

        player_dice4.setBackgroundResource(android.R.drawable.btn_default)

        player_dice5.setBackgroundResource(android.R.drawable.btn_default)
    }

    fun rerollSpecific(){                   //reroll the selected dice images
        var selectedArray = arrayOf(p1_flag, p2_flag, p3_flag, p4_flag, p5_flag)

        for (i in selectedArray.indices){
            if(selectedArray[i] == p1_flag && selectedArray[i] == true){
                specific_roll(player_dice1)
            }

            if(selectedArray[i] == p2_flag && selectedArray[i] == true){
                specific_roll(player_dice2)
            }

            if(selectedArray[i] == p3_flag && selectedArray[i] == true){
                specific_roll(player_dice3)
            }

            if(selectedArray[i] == p4_flag && selectedArray[i] == true){
                specific_roll(player_dice4)
            }

            if(selectedArray[i] == p5_flag && selectedArray[i] == true){
                specific_roll(player_dice5)
            }
        }
    }

    fun rerollDice(image: ImageView){
        roll_die(image)
    }

    fun enableDice(){               //change the border of the dice images when clicked

        val selectedDrawable = ContextCompat.getDrawable(this, R.drawable.selected_border)

        player_dice1.setOnClickListener{
            if(player_dice1.background.equals(selectedDrawable)){
                p1_flag = false
                player_dice1.setBackgroundResource(android.R.drawable.btn_default)

            }
            else if(!player_dice1.background.equals(selectedDrawable)){
                mediaPlayer4?.start()
                p1_flag = true
                player_dice1.setBackgroundResource(R.drawable.selected_border)
            }
    }

        player_dice2.setOnClickListener{
            if(player_dice2.background.equals(selectedDrawable)){
                p2_flag = false
                player_dice2.setBackgroundResource(android.R.drawable.btn_default)

            }
            else{
                mediaPlayer4?.start()
                p2_flag = true
                player_dice2.setBackgroundResource(R.drawable.selected_border)
            }
        }

        player_dice3.setOnClickListener{
            if(player_dice3.background.equals(selectedDrawable)){
                p3_flag = false
                player_dice3.setBackgroundResource(android.R.drawable.btn_default)

            }
            else{
                mediaPlayer4?.start()
                p3_flag = true
                player_dice3.setBackgroundResource(R.drawable.selected_border)
            }
        }

        player_dice4.setOnClickListener{
            if(player_dice4.background.equals(selectedDrawable)){
                p4_flag = false
                player_dice4.setBackgroundResource(android.R.drawable.btn_default)

            }
            else{
                mediaPlayer4?.start()
                p4_flag = true
                player_dice4.setBackgroundResource(R.drawable.selected_border)
            }
        }

        player_dice5.setOnClickListener{
            if(player_dice5.background.equals(selectedDrawable)){
                p5_flag = false
                player_dice5.setBackgroundResource(android.R.drawable.btn_default)

            }
            else{
                mediaPlayer4?.start()
                p5_flag = true
                player_dice5.setBackgroundResource(R.drawable.selected_border)
            }
        }
}

    fun cpuPlays(){
        var tempCPUScore : Int = tempScore(cpu_dice1) + tempScore(cpu_dice2) + tempScore(cpu_dice3)         //cpu strategy:
        + tempScore(cpu_dice4)  + tempScore(cpu_dice5)                                                      //counts the total of the existing dice combination of the CPU and Player
                                                                                                            //determines whether to reroll on random occasion if the CPU has a lower total than the player
        var tempPlayerScore : Int = tempScore(player_dice1) + tempScore(player_dice2) + tempScore(player_dice3)
        + tempScore(player_dice4)  + tempScore(player_dice5)

        //cpu checks if current dice combo is larger than the players dice combo.

        if (tempPlayerScore < tempCPUScore){
            addCpuScore(cpu_dice1)
            addCpuScore(cpu_dice2)
            addCpuScore(cpu_dice3)
            addCpuScore(cpu_dice4)
            addCpuScore(cpu_dice5)

            cpu_score_tv.text = "Score - $cpu_score"
        }

        else {                          //random chance of rerolling a random number on the cpu dice list
            val random = Random()
            var tempDiceRerollArray =
                mutableListOf(cpu_dice1, cpu_dice2, cpu_dice3, cpu_dice4, cpu_dice5)

            while(cpu_rolls > 0) {
                var randomValue = random.nextInt(2)
                while(randomValue == 1 && tempDiceRerollArray.size >= 1) {
                    var randomNumber = random.nextInt(tempDiceRerollArray.size)
                    specific_roll(tempDiceRerollArray[randomNumber])
                    tempDiceRerollArray.removeAt(randomNumber)
                    randomValue = random.nextInt(2)
                }
                cpu_rolls--
            }


            addCpuScore(cpu_dice1)
            addCpuScore(cpu_dice2)
            addCpuScore(cpu_dice3)
            addCpuScore(cpu_dice4)
            addCpuScore(cpu_dice5)

            cpu_score_tv.text = "Score - $cpu_score"
        }






    }


    fun resetRound(){               //resets the player and cpu rolls, and enable and disable the
                                    //roll and submit buttons simultaneously

        cpu_rolls = 3
        player_rolls = 3

//        var allArray = arrayOf(player_dice1, player_dice2, player_dice3, player_dice4, player_dice5, cpu_dice1, cpu_dice2, cpu_dice3, cpu_dice4, cpu_dice5)
//
//        for(i in allArray.indices){
//            allArray[i].setImageResource(R.drawable.dice)
//        }

        roll_button.isEnabled = true
        roll_button.text = "ROLL"

        submit_button.isEnabled = false





    }

    fun returnDiceValue(image: ImageView) : Int{               //function created to return the numerical value corresponding to the imageViews
        if(image.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.dice_1)?.constantState){
            return 1
        }

        if(image.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.dice_2)?.constantState){
            return 2
        }

        if(image.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.dice_3)?.constantState){
            return 3
        }

        if(image.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.dice_4)?.constantState){
            return 4
        }

        if(image.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.dice_5)?.constantState){
            return 5
        }

        return -1
    }

    fun addPlayerScore(image: ImageView) {                  //add playerScore

        when (image.drawable.constantState) {
            ContextCompat.getDrawable(this, R.drawable.dice_1)?.constantState -> {
                player_score += 1
            }

            ContextCompat.getDrawable(this, R.drawable.dice_2)?.constantState -> {
                player_score += 2
            }

            ContextCompat.getDrawable(this, R.drawable.dice_3)?.constantState -> {
                player_score += 3
            }

            ContextCompat.getDrawable(this, R.drawable.dice_4)?.constantState -> {
                player_score += 4
            }

            ContextCompat.getDrawable(this, R.drawable.dice_5)?.constantState -> {
                player_score += 5
            }

            ContextCompat.getDrawable(this, R.drawable.dice_6)?.constantState -> {
                player_score += 6
            }
        }
        //two different functions since the score parameters are val and not var
    }

    fun addCpuScore(image: ImageView) {             //add CPU score

        when (image.drawable.constantState) {
            ContextCompat.getDrawable(this, R.drawable.dice_1)?.constantState -> {
                cpu_score += 1
            }

            ContextCompat.getDrawable(this, R.drawable.dice_2)?.constantState -> {
                cpu_score += 2
            }

            ContextCompat.getDrawable(this, R.drawable.dice_3)?.constantState -> {
                cpu_score += 3
            }

            ContextCompat.getDrawable(this, R.drawable.dice_4)?.constantState -> {
                cpu_score += 4
            }

            ContextCompat.getDrawable(this, R.drawable.dice_5)?.constantState -> {
                cpu_score += 5
            }

            ContextCompat.getDrawable(this, R.drawable.dice_6)?.constantState -> {
                cpu_score += 6
            }
        }
        //two different functions since the score parameters are val and not var
    }

    fun tempScore(image: ImageView) : Int {             //tempScore
        var score : Int = 0
        when (image.drawable) {
            ContextCompat.getDrawable(this, R.drawable.dice_1) -> {
                score += 1
            }

            ContextCompat.getDrawable(this, R.drawable.dice_2) -> {
                score += 2
            }

            ContextCompat.getDrawable(this, R.drawable.dice_3) -> {
                score += 3
            }

            ContextCompat.getDrawable(this, R.drawable.dice_4) -> {
                score += 4
            }

            ContextCompat.getDrawable(this, R.drawable.dice_5) -> {
                score += 5
            }

            ContextCompat.getDrawable(this, R.drawable.dice_6) -> {
                score += 6
            }
        }

        return score
        //two different functions since the score parameters are val and not var
    }

    fun roll_die(image: ImageView){                 //roll dice function
            val random = Random()
            val randomNumber = random.nextInt(6)

            image.setImageResource(dice_positions[randomNumber])
    }


    fun player_roll(){
        roll_die(player_dice1)
        roll_die(player_dice2)
        roll_die(player_dice3)
        roll_die(player_dice4)
        roll_die(player_dice5)

    }

    fun specific_roll(image: ImageView){
        roll_die(image)
    }

    fun cpu_roll(){
        roll_die(cpu_dice1)
        roll_die(cpu_dice2)
        roll_die(cpu_dice3)
        roll_die(cpu_dice4)
        roll_die(cpu_dice5)
    }



    fun addScore(dice: Int){
        when(dice){
            1 -> player_score++
            2 -> player_score+=2
            3 -> player_score+=3
            4 -> player_score+=4
            5 -> player_score+=5
            6 -> player_score+=6
        }
    }



}

