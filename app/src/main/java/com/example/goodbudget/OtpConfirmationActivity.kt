package com.example.goodbudget

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class OtpConfirmationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_confirmation)

        val otpField = findViewById<EditText>(R.id.otpField)
        val verifyBtn = findViewById<Button>(R.id.verifyBtn)

        // Autofill for demo/testing
        otpField.setText("12345")

        // Retrieve from intent
        val email = intent.getStringExtra("email") ?: ""
        val newPassword = intent.getStringExtra("newPassword") ?: ""

        database = FirebaseDatabase.getInstance().getReference("users")

        verifyBtn.setOnClickListener {
            if (otpField.text.toString() == "12345") {
                // Update password in Firebase
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnap in snapshot.children) {
                            val user = userSnap.getValue(User::class.java)
                            if (user?.email == email) {
                                userSnap.ref.child("password").setValue(newPassword)
                                break
                            }
                        }
                        Toast.makeText(this@OtpConfirmationActivity, "Password updated!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@OtpConfirmationActivity, LoginActivity::class.java))
                        finish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@OtpConfirmationActivity, "Error updating password", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
