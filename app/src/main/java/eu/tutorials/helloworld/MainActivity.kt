package eu.tutorials.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val button = findViewById<Button>(R.id.button)
        var increment = 0
        button.setOnClickListener {
            increment++
            textView.text = increment.toString()
        }
    }
}