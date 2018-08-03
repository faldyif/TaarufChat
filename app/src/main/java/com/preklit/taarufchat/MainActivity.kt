package com.preklit.taarufchat

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

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
        logout_button.isEnabled = false
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance().createSignInIntentBuilder().build(),
                RC_SIGN_IN)
    }

    fun initLoggedIn() {
        Toast.makeText(this, "Sudah Login", Toast.LENGTH_LONG).show()
        logout_button.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener {
                        initGuest()
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> if (resultCode == Activity.RESULT_OK) initLoggedIn()
        }
    }
}
