package com.karamlyy.leaderboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val logoutButton = findViewById<ImageButton>(R.id.logoutButton)
        val leaderboardButton = findViewById<Button>(R.id.leaderboardButton)

        leaderboardButton.setOnClickListener {
            //val intent = Intent(this, SingleLeaderboardActivity::class.java)
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
            //finish()
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
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

                            runOnUiThread {
                                AlertDialog.Builder(this@AdminHomeActivity)
                                    .setTitle("Submission Successful")
                                    .setMessage("Your feedback has been submitted.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show()
                            }

                            studentEmailField.setText("")
                            pointField.setText("")
                            feedbackField.setText("")

                        } else {
                            throw IllegalArgumentException("Please enter less than 30 symbols")
                        }
                    } catch (e: Throwable) {
                        runOnUiThread {
                            Toast.makeText(this@AdminHomeActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }


    }


}