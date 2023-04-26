package com.karamlyy.leaderboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LeaderboardActivity : AppCompatActivity() {

    private val firebaseManager = FirebaseManager.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        val backToProfile = findViewById<Button>(R.id.backToProfile)
        val exchangePoint = findViewById<Button>(R.id.exchangePoints)
        val adapter = LeaderboardAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.leaderboardRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        backToProfile.setOnClickListener {
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
        }

        /*
        exchangePoint.setOnClickListener {
            val intent = Intent(this, ExchangeActivity::class.java)
            startActivity(intent)
        }

         */

        runBlocking {
            launch {
                try {
                    val leaderboardMembers = firebaseManager.getLeaderboard()
                    adapter.setLeaderboardMembers(leaderboardMembers)
                }
                catch (e:Throwable){
                    Toast.makeText(this@LeaderboardActivity,e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}