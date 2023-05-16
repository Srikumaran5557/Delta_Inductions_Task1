package com.example.cc1

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class SecondActivity : ComponentActivity() {
    var score1 = 300
    var b1 = 0
    var c = ""
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var buttons: Array<Button>
    lateinit var imvheart :Array<ImageView>
    lateinit var display: TextView
    lateinit var tvf :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        display = findViewById(R.id.entry)
        val b = intent.getStringExtra("USER").toString()
        var lencheck = b.length
        val gotclue = intent.getStringExtra("CLUE").toString()
        val cluebt = findViewById<Button>(R.id.btinfo)
        var i = 0
        val reset = findViewById<Button>(R.id.resetbt2)
        val check = findViewById<Button>(R.id.clrbt2)
        buttons = Array<Button>(16) { i ->
            findViewById<Button>(
                resources.getIdentifier(
                    "bt${i + 1}",
                    "id",
                    "com.example.cc1"
                )
            )
        }
        for (but in buttons) {
            but.setOnClickListener() {
                checkclick(but)
            }
        }
        imvheart= arrayOf(findViewById(R.id.iv1),findViewById(R.id.iv2),findViewById(R.id.iv3))
        display.text = "_ ".repeat(lencheck)
        shufflekeypad(b.toString())
        check.setOnClickListener {
            display.text = c
            val color = R.color.buttonclick
            val color1 = R.color.black
            reset()
            shufflekeypad(b.toString())
            if (c == b) {
                mediaPlayer = MediaPlayer.create(this, R.raw.won)
                mediaPlayer.start()
                val intent = Intent(this@SecondActivity, MainActivity::class.java)
                intent.putExtra("Score", score1)
                resultopener(score1.toString(), gotclue, b)
                c = ""

            } else {
                c = ""
                display.text = c
                imvheart[i].setColorFilter(R.color.grey)
                score1 = score1 - 100
                val liferem = 2 - i
                Toast.makeText(
                    this@SecondActivity,
                    "Incorrect $liferem lives remaining",
                    Toast.LENGTH_SHORT
                ).show()
                i = i + 1
                if (i==3){
                    mediaPlayer = MediaPlayer.create(this, R.raw.lost)
                    mediaPlayer.start()
                    resultopener(score1.toString(),gotclue,b)
                }
            }
        }
        reset.setOnClickListener {
            reset()
            c = ""
            display.text = "_ ".repeat(lencheck)
            Toast.makeText(this@SecondActivity, "Cleared", Toast.LENGTH_SHORT).show()
        }
        cluebt.setOnClickListener {
            mediaPlayer = MediaPlayer.create(this, R.raw.cluesound)
            mediaPlayer.start()
            var builder = AlertDialog.Builder(this@SecondActivity)
            var clueOpen = View.inflate(this, R.layout.layout_customdialog, null)
            var clueTextView = clueOpen.findViewById<TextView>(R.id.clue)
            var close: Button = clueOpen.findViewById(R.id.btc)
            clueTextView.text = gotclue.toString()
            builder.setView(clueOpen)
            var clueDialog = builder.create()
            clueDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            clueDialog.show()
            clueDialog.setCanceledOnTouchOutside(false)
            close.setOnClickListener {
                clueDialog.dismiss()
            }
        }
    }

    private fun checkclick(key: Button) {
        key.setClickable(false)
        val a = key.getText()
        val b = a.toString()
        c = c + b
        display.text = c
        val color1 = R.color.white
        key.setTextColor(ContextCompat.getColor(this, color1))
        key.setBackgroundResource(R.drawable.pressedbt)
    }
    private fun reset() {
        buttons = Array<Button>(16) { i ->
            findViewById<Button>(
                resources.getIdentifier(
                    "bt${i + 1}",
                    "id",
                    "com.example.cc1"
                )
            )
        }
        for (but in buttons) {
            val color1 = R.color.black
            but.setBackgroundResource(R.drawable.round_button)
            but.setTextColor(ContextCompat.getColor(this, color1))
            but.setClickable(true)
        }
    }
    private fun resultopener(e: String, f: String, d: String): Int {
        var clueOpen = View.inflate(this, R.layout.result, null)
        var clue = clueOpen.findViewById<TextView>(R.id.clue)
        var home: Button = clueOpen.findViewById(R.id.home)
        val playagain: Button = clueOpen.findViewById(R.id.playagain)
        clue.text = "Your Score: $e"
        datastore(e)
        var i = 0
        var builder = AlertDialog.Builder(this@SecondActivity)
        builder.setView(clueOpen)
        var clueDialog = builder.create()
        clueDialog.dismiss()
        clueDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        clueDialog.show()
        clueDialog.setCanceledOnTouchOutside(false)
        val intent=Intent(this@SecondActivity,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        home.setOnClickListener {
            startActivity(intent)
            clueDialog.dismiss()
        }
        playagain.setOnClickListener {
            clueDialog.dismiss()
            this@SecondActivity.finish()
            val intent = Intent(this@SecondActivity, SecondActivity::class.java)
            intent.putExtra("USER", d.uppercase())
            intent.putExtra("CLUE", f.uppercase())
            startActivity(intent)
        }
        return 0 }
    private fun shufflekeypad(b: String) {
        val a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val k = b.length
        var string = ""
        val randomValues = List(16 - k) { Random.nextInt(0, 26) }
        for (i in randomValues) {
            string = string + a[i].toString()
        }
        var j = 0
        string = string + b
        val list1 = string.toList()
        val list2 = list1.shuffled()
        for (i in 0 until 16) {
            buttons[i].setText(list2[i].toString())
        }
    }
    private fun datastore(e: String) {

        val sharedPref = getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        var exist:Int=sharedPref.getInt("bestscore",0)
        if(e.toInt()>exist){
            editor.putInt("bestscore",e.toInt())
            editor.apply()}
    }
    override fun onBackPressed() {
        moveTaskToBack(false)
    }
}