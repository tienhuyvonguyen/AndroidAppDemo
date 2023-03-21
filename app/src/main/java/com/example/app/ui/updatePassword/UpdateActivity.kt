package com.example.app.ui.updatePassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.R
import com.example.app.databinding.ActivityUpdatepasswordBinding
import com.example.app.ui.login.LoginActivity
import com.example.app.utility.TinyDB

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatepasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updatepassword)
        binding = ActivityUpdatepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnUpdate = binding.update
        val oldPassword = binding.cpasswd
        val newPassword = binding.password
        val confirmPassword = binding.cpassword
        val tinyObj = TinyDB(applicationContext)
        val username = tinyObj.getString("username")
        btnUpdate.setOnClickListener {
            val testInput = testInput(
                oldPassword.text.toString(),
                newPassword.text.toString(),
                confirmPassword.text.toString()
            )
            if (testInput) {
                doUpdatePassword(username, newPassword.text.toString())
            } else {
                Toast.makeText(applicationContext, "Invalid Password", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }

    private fun isConfirmPasswordValid(password: String, cpassword: String): Boolean {
        return password == cpassword
    }

    private fun testInput(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        if (!isPasswordValid(oldPassword) || !isPasswordValid(newPassword) || !isPasswordValid(
                confirmPassword
            ) || !isConfirmPasswordValid(newPassword, confirmPassword)
        ) {
            return false
        }
        return true
    }

    private fun doUpdatePassword(username: String, newPassword: String) {
        val context = AppContext.getContext()
        val queue = Volley.newRequestQueue(context)
        val tinyDBObj = TinyDB(context)
        val url = "http://143.42.66.73:9090/api/user/update.php"
        val resp: StringRequest = object : StringRequest(
            Method.PUT, url,
            Response.Listener { response ->
                Toast.makeText(context, "Update Success", Toast.LENGTH_LONG).show()
                cleanUp()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                if (error.networkResponse.statusCode == 401) {
                    Toast.makeText(context, "Token expired", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show()
                }
            }
        ) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer " + tinyDBObj.getString("token")
                return headers
            }

            override fun getParams(): Map<String, String> {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["username"] = username
                params["password"] = newPassword

                // at last we are
                // returning our params.
                return params
            }
        }
        queue.add(resp)
    }

    private fun cleanUp() {
        val context = AppContext.getContext()
        val tinyDBObj = TinyDB(context)
        tinyDBObj.remove("token")
        tinyDBObj.remove("user")
    }
}