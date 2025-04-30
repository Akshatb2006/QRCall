package com.example.qrcall.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcall.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")

        binding.btnSignup.setOnClickListener {
            val username = binding.userName.text.toString()
            val userPassword = binding.signupPassword.text.toString()
            if(username.isNotEmpty() && userPassword.isNotEmpty()) {
                signupuser(username, userPassword)
            }else{
                Toast.makeText(this,"All fields are mandatory",Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupText.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        }

    }

    private fun signupuser(username: String, userPassword: String) {
        databaseReference.orderByChild("userName").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = userdata(id, username, userPassword)
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(this@signup, "Signup Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@signup, Login::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@signup, "User Already Exists", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@signup, "Database Error", Toast.LENGTH_SHORT).show()
                }
            })
    }
}