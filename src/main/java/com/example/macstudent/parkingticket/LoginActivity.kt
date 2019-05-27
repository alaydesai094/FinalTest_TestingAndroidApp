package com.example.macstudent.parkingticket

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

import com.example.macstudent.parkingticket.db.AppDataBase
import com.example.macstudent.parkingticket.model.Ticket
import com.example.macstudent.parkingticket.model.User
import com.example.macstudent.parkingticket.util.Utils

/**
 * All code in this project is courtesy of Franco J.A. Marques.
 */
class LoginActivity : AppCompatActivity() {
    private var edtEmail: EditText? = null
    private var edtPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnSignUp: Button? = null
    private var chkRememberMe: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById<View>(R.id.edtEmail) as EditText
        edtPassword = findViewById<View>(R.id.edtPassword) as EditText
        chkRememberMe = findViewById<View>(R.id.chkRememberMe) as CheckBox
        btnLogin = findViewById<View>(R.id.btnLogin) as Button
        btnSignUp = findViewById<View>(R.id.btnSingUp) as Button

        val user = intent.getParcelableExtra<User>("user")

        if (user != null) {
            edtEmail!!.setText(user.email)
            edtPassword!!.setText(user.password)
        } else {
            checkSavedPreferences()
        }

        btnLogin!!.setOnClickListener {
            var user = userAuthentication()

            (applicationContext as MyApplication).setUser(user);

            if (user != null) {
                updateSavedPreferences()
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }
        }

        btnSignUp!!.setOnClickListener { startActivity(Intent(this@LoginActivity, SignUPActivity::class.java)) }
    }


    /**
     * Check if the 'Remember Me' option is selected and fetch the email and password from
     * the preferences object.
     */
    private fun checkSavedPreferences() {
        // create shared preferences object.
        val preferences = getSharedPreferences("parking-ticket-prefs", Context.MODE_PRIVATE)

        // get saved values from shared preferences.
        val email = preferences.getString("user-email", null)
        val password = preferences.getString("user-password", null)

        // set values to the edit text
        if (email != null && password != null) {
            edtEmail!!.setText(email)
            edtPassword!!.setText(password)

            chkRememberMe!!.isChecked = true
        } else {
            chkRememberMe!!.isChecked = false
        }
    }


    private fun updateSavedPreferences() {
        // create shared preferences object.
        val preferences = getSharedPreferences("parking-ticket-prefs", Context.MODE_PRIVATE)

        // get editor object.
        val editor = preferences.edit()

        if (chkRememberMe!!.isChecked) {
            // save value to shared preferences using editor object.
            editor.putString("user-email", edtEmail!!.text.toString())
            editor.putString("user-password", edtPassword!!.text.toString())
        } else {
            // remove values from shared preferences.
            editor.remove("user-email")
            editor.remove("user-password")
        }

        // save changes permanently into shared preferences.
        editor.apply()
    }


    /**
     * Perform the user authentication process.
     */
    private fun userAuthentication(): User? {
        // check for blank or invalid inputs
        if (Utils.isEmpty(edtEmail) || !Utils.isValidEmail(edtEmail!!.text.toString())) {
            edtEmail!!.error = "Please enter a valid email."
            return null
        }

        if (Utils.isEmpty(edtPassword)) {
            edtPassword!!.error = "Please enter your password."
            return null
        }

        // check user credentials in the database
        val database = AppDataBase.getAppDataBase(this)
        val user = database.userDao().findByEmail(edtEmail!!.text.toString())

        if (user == null) {
            Toast.makeText(this, "User not found.", Toast.LENGTH_LONG).show()
            return null
        }

        if (user.password != edtPassword!!.text.toString()) {
            Toast.makeText(this, "Invalid password.", Toast.LENGTH_LONG).show()
            return null
        }

        return user
    }

}
