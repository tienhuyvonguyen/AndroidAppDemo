package com.example.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ActivityRegisterBinding
import com.example.app.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val email = binding.email
        val register = binding.register

        register.setOnClickListener() {
            val testInput = regisDataChange(
                username.text.toString(),
                password.text.toString(),
                email.text.toString()
            )
            if (testInput) {
                doRegister(username.text.toString(), password.text.toString(), email.text.toString())
            }
        }
    }

    private fun doRegister(username: String, password: String, email: String) {
        val url = "http://143.42.66.73:9090/public/api/register.php"
        val queue = Volley.newRequestQueue(this@RegisterActivity)
        val req: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response: String ->
                Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(
                    this@RegisterActivity,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): Map<String, String> {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["username"] = username
                params["password"] = password
                params["email"] = email
                // at last we are
                // returning our params.
                return params
            }
        }
        queue.add(req)
    }

    private fun regisDataChange(username: String, password: String, email: String): Boolean {
        if (!isUsernameValid(username)) {
            binding.username.error = getString(R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            binding.password.error = getString(R.string.invalid_password)
        } else if (!isEmailValid(email)) {
            binding.email.error = getString(R.string.invalid_email)
        } else {
            return true
        }
        return false
    }

    private fun isUsernameValid(username: String): Boolean {
        return username.length > 3
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 6
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }
}
