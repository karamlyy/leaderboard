package com.karamlyy.leaderboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        val adapter = LeaderboardAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.leaderboardRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter


        val currentUser = firebaseManager.getUser().userType


        backToProfile.setOnClickListener {
            val intent = if(currentUser.toString() == "ADMIN") {
                Intent(this, AdminHomeActivity::class.java)
            } else {
                Intent(this, StudentHomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }



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

