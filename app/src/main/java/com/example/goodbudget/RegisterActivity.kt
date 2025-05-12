package com.example.goodbudget

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var surnameField: EditText
    private lateinit var usernameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText

    private lateinit var radioChar: RadioButton
    private lateinit var radioSpec: RadioButton
    private lateinit var radioNum: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Bind inputs
        nameField = findViewById(R.id.nameLoginField)
        surnameField = findViewById(R.id.surnameLoginField)
        usernameField = findViewById(R.id.usernameLoginField)
        emailField = findViewById(R.id.emailLoginField)
        passwordField = findViewById(R.id.passwordLoginField)
        confirmPasswordField = findViewById(R.id.confirmPasswordLoginField)

        radioChar = findViewById(R.id.characterRadioButton)
        radioSpec = findViewById(R.id.specialRadioButton)
        radioNum = findViewById(R.id.numericalRadioButton)

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                radioChar.isChecked = text.length in 8..12
                radioSpec.isChecked = text.any { !it.isLetterOrDigit() }
                radioNum.isChecked = text.any { it.isDigit() }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<Button>(R.id.registerBtn).setOnClickListener {
            val pass = passwordField.text.toString()
            val confirm = confirmPasswordField.text.toString()

            if (pass == confirm && radioChar.isChecked && radioSpec.isChecked && radioNum.isChecked) {
                val user = User(
                    name = nameField.text.toString(),
                    surname = surnameField.text.toString(),
                    username = usernameField.text.toString(),
                    email = emailField.text.toString(),
                    password = pass
                )

                // Firebase save
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users")
                val userId = usersRef.push().key ?: ""
                usersRef.child(userId).setValue(user)

                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                Toast.makeText(this, "Password validation failed", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.loginRedirect).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
