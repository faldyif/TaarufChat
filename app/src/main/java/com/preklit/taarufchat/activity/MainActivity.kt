package com.preklit.taarufchat.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.preklit.taarufchat.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123
    private val TAG = "MainActivity"
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check firebase authentication
        if (auth.currentUser != null) {
            // already signed in
            initLoggedIn()
        } else {
            // not signed in
            initGuest()
        }
    }

    private fun initGuest() {
        logoutButton.isEnabled = false
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().build(),
                RC_SIGN_IN)
    }

    private fun initLoggedIn() {
        Toast.makeText(this, "Sudah Login", Toast.LENGTH_LONG).show()
        logoutButton.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener {
                        initGuest()
                    }
        }

        // Go to next activity
        finish()
        val intent = Intent(this, SelectGenderActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> if (resultCode == Activity.RESULT_OK) {
                setUser()
                initLoggedIn()
            }
        }
    }

    private fun setUser() {
        val user = HashMap<String, Any>()
        user["uid"] = auth.currentUser!!.uid
        user["name"] = auth.currentUser!!.displayName!!

        db.collection("users").document(user["uid"]?.toString()!!)
                .set(user, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "Success updating user") }
                .addOnFailureListener { Log.w(TAG, "Failed updating user $Unit") }
    }
}
