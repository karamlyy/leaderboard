package com.karamlyy.leaderboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private val firebaseManager = FirebaseManager.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailField = findViewById<EditText>(R.id.emailText)
        val passwordField = findViewById<EditText>(R.id.passwordText)
        val button = findViewById<Button>(R.id.loginButton)


        button.setOnClickListener {
            runBlocking {
                launch {
                    try {
                        firebaseManager.login(emailField.text.toString(), passwordField.text.toString())

                        val user = firebaseManager.getUser()

                        val intent : Intent = when(user.userType){
                            UserType.ADMIN -> Intent(this@MainActivity, AdminHomeActivity::class.java)
                            UserType.STUDENT -> Intent(this@MainActivity, StudentHomeActivity::class.java)
                        }


                        Toast.makeText(this@MainActivity,"loggining...",Toast.LENGTH_SHORT).show()
                        startActivity(intent);

                    }catch (e:Throwable){
                        Toast.makeText(this@MainActivity,"wrong password or wrong email",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}