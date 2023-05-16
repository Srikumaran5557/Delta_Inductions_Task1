package com.example.cc1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.cc1.ui.theme.Cc1Theme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = findViewById<Button>(R.id.button)
        val b =findViewById<EditText>(R.id.etname)
        val c = findViewById<EditText>(R.id.etname2)
        val hs =findViewById<TextView>(R.id.highscore)
        val k =loaddata()
        hs.text=" HIGH SCORE: $k"
        a.setOnClickListener {
            val d =b.text.toString()
            val e =c.text.toString()
            val len =d.length
            val k=0
            if (d.isNullOrEmpty()){
                Toast.makeText(this@MainActivity, "Enter The Word", Toast.LENGTH_SHORT).show()
            }
            else if (e.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, "Enter The Clue", Toast.LENGTH_SHORT).show()
            }
            else if (len<16) {
                b.text.clear()
                c.text.clear()
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("USER", d.uppercase().replace(" ","") )
                intent.putExtra("CLUE", e.split(" ").joinToString(" ") )
                startActivity(intent)
            }
            else{
                Toast.makeText(this@MainActivity, "Enter word less than 16", Toast.LENGTH_SHORT).show()
                b.text.clear()
            }
        }

    }
    private fun loaddata(): Int {
        val prefs = getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val a=prefs.getInt("bestscore", 0)
        return a

    }
}