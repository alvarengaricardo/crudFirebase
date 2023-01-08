package com.example.crudfirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class InsertionActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var btnSave: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)

        btnSave = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Friends")

        btnSave.setOnClickListener {
            saveData()
            TimeUnit.SECONDS.sleep(1)
            name.text.clear()
            email.text.clear()
            name.requestFocus()
        }
    }

    private fun saveData() {

        //getting values
        var name = name.text.toString()
        var email = email.text.toString()
        var flag = true

        if (name.isEmpty()) flag = false
        if (email.isEmpty()) flag = false

        val id = dbRef.push().key!!
        val friend = FriendModel(id, name, email)

        if (flag) {
            dbRef.child(id).setValue(friend)
                .addOnCompleteListener {
                    Toast.makeText(this, R.string.dados_inseridos, Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, R.string.preencher, Toast.LENGTH_LONG).show()
        }
    }
}