package com.example.crudfirebase

import FriendAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit

class FetchingActivity : AppCompatActivity() {

    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var friendList: ArrayList<FriendModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        friendRecyclerView = findViewById(R.id.rvFriend)
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        friendList = arrayListOf()

        getFriendsData()
    }

    private fun getFriendsData() {

        friendRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Friends")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(FriendModel::class.java)
                        friendList.add(empData!!)
                    }
                    val mAdapter = FriendAdapter(friendList)
                    friendRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FriendAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent =
                                Intent(this@FetchingActivity, FriendDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("id", friendList[position].id)
                            intent.putExtra("name", friendList[position].name)
                            intent.putExtra("email", friendList[position].email)
                            startActivity(intent)
                        }
                    })

                    friendRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                } else {
                    Toast.makeText(applicationContext, R.string.banco_vazio, Toast.LENGTH_SHORT)
                        .show()
                    TimeUnit.SECONDS.sleep(1)
                    finish()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // implementar
            }
        })
    }
}