package com.preklit.taarufchat.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.preklit.taarufchat.R
import kotlinx.android.synthetic.main.activity_select_gender.*
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class SelectGenderActivity : AppCompatActivity() {

    private val TAG = "SelectGenderActivity"
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_gender)

        initComponent()
    }

    private fun initComponent() {
        buttonIkhwan.setOnClickListener {
            setGender("I")
        }
        buttonAkhwat.setOnClickListener {
            setGender("A")
        }

        logoutButton.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener {
                        var intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
        }

    }

    private fun setGender(s: String) {
        val user = HashMap<String, Any>()
        user["gender"] = s

        db.collection("users").document(auth.uid!!)
                .set(user, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}
