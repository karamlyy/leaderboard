package com.karamlyy.leaderboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdminHomeActivity : AppCompatActivity() {
    private val firebaseManager = FirebaseManager.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)


        val adminNameField = findViewById<TextView>(R.id.adminNameText)
        val studentEmailField = findViewById<EditText>(R.id.studentEmailText)
        val pointField = findViewById<EditText>(R.id.pointText)
        val feedbackField = findViewById<EditText>(R.id.feedbackText)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        adminNameField.text = firebaseManager.getUser().username
        confirmButton.setOnClickListener {
            runBlocking {
                launch {
                    try {
                        if(feedbackField.length() < 30){

                            firebaseManager.submit(

                                studentEmailField.text.toString(),
                                pointField.text.toString().toInt(),
                                feedbackField.text.toString()
                            )
                            Toast.makeText(this@AdminHomeActivity, "submitted", Toast.LENGTH_LONG).show()
                            studentEmailField.setText("")
                            pointField.setText("")
                            feedbackField.setText("")

                        }
                        else{
                            throw IllegalArgumentException("Please enter less than 30 symbols")
                        }
                    } catch (e: Throwable) {
                        Toast.makeText(this@AdminHomeActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }


}