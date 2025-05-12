package com.example.goodbudget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.emailLoginField)
        passwordField = findViewById(R.id.passwordLoginField)

        databaseRef = FirebaseDatabase.getInstance().getReference("users")

        findViewById<Button>(R.id.loginBtn).setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var loginSuccess = false
                    for (userSnap in snapshot.children) {
                        val user = userSnap.getValue(User::class.java)
                        if (user?.email == email && user.password == password) {
                            loginSuccess = true
                            break
                        }
                    }

                    if (loginSuccess) {
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    } else {
                        emailField.setBackgroundColor(Color.RED)
                        passwordField.setBackgroundColor(Color.RED)
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        findViewById<TextView>(R.id.forgotPasswordLink).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}