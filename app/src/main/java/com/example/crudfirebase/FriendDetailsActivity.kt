package com.example.crudfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class FriendDetailsActivity : AppCompatActivity() {
    private lateinit var tvId: TextView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("id").toString(),
                intent.getStringExtra("name").toString()
            )
        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("id").toString()
            )
        }
    }

    private fun initView() {
        tvId = findViewById(R.id.tvId)
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvId.text = intent.getStringExtra("id")
        tvName.text = intent.getStringExtra("name")
        tvEmail.text = intent.getStringExtra("email")
    }

    private fun deleteRecord(
        id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Friends").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Dados deletados!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        id: String,
        name: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etName = mDialogView.findViewById<EditText>(R.id.etName)
        val etEmail = mDialogView.findViewById<EditText>(R.id.etEmail)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etName.setText(intent.getStringExtra("name").toString())
        etEmail.setText(intent.getStringExtra("email").toString())

        mDialog.setTitle("Updating $name Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateFriendData(
                id,
                etName.text.toString(),
                etEmail.text.toString()
            )

            Toast.makeText(applicationContext, "Dados atualizados!", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvName.text = etName.text.toString()
            tvEmail.text = etEmail.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateFriendData(
        id: String,
        name: String,
        email: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Friends").child(id)
        val friendInfo = FriendModel(id, name, email)
        dbRef.setValue(friendInfo)
    }

}