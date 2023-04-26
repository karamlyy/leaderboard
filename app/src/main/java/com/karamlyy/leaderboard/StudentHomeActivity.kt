package com.karamlyy.leaderboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

class StudentHomeActivity : AppCompatActivity() {

    private val firebaseManager = FirebaseManager.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)


        val studentNameView = findViewById<TextView>(R.id.studentName)
        val totalPointView = findViewById<TextView>(R.id.totalPoint)
        val leaderboardButton = findViewById<Button>(R.id.leaderboardButton)
        val logoutButton = findViewById<Button>(R.id.backToLogin)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val ratingsAdapter = RatingsAdapter()
        recyclerView.adapter = ratingsAdapter

        val user = firebaseManager.getUser()
        studentNameView.text = user.username
        runBlocking {
            launch {
                try {
                    totalPointView.text = "Your total point: ${firebaseManager.getTotalPoint()}"
                }
                catch (e:Throwable){
                    Toast.makeText(this@StudentHomeActivity,e.message, Toast.LENGTH_LONG).show()
                }
            }
            launch {
                try {
                    val ratings = firebaseManager.getPoints()
                    ratingsAdapter.setRatings(ratings)
                }
                catch (e:Throwable){
                    Toast.makeText(this@StudentHomeActivity,e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}