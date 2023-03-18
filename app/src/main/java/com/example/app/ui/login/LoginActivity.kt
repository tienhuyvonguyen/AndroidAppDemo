package com.example.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.ActivityLoginBinding
import com.example.app.ui.register.RegisterActivity
import com.example.app.utility.TinyDB
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val signup = binding.register

        signup.setOnClickListener {
            register()
        }

        login.setOnClickListener {
            val testInput = loginDataChange(username.text.toString(), password.text.toString())
            if (testInput) {
                doLogin(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loginDataChange(username: String, password: String): Boolean {
        if (!isUserNameValid(username)) {
            binding.username.error = getString(R.string.invalid_username)
            Toast.makeText(applicationContext, "Invalid Username", Toast.LENGTH_SHORT).show()
        } else if (!isPasswordValid(password)) {
            binding.password.error = getString(R.string.invalid_password)
            Toast.makeText(applicationContext, "Invalid Password", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    private fun doLogin(username: String, password: String) {
        val url = "http://143.42.66.73:9090/public/api/login.php"
        val queue = Volley.newRequestQueue(this@LoginActivity)
        val req: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response: String ->
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                onLoginSuccess(response, username)
            },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(
                    this@LoginActivity,
                    "Connection Failed: $error",
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

                // at last we are
                // returning our params.
                return params
            }
        }
        queue.add(req)
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 6
    }

    private fun isUserNameValid(username: String): Boolean {
        return username.length > 3
    }

    private fun onLoginSuccess(resp: String, username: String) {
        val jsonObject = JSONObject(resp)
        val token = jsonObject.getString("token")
        val tinyDB = TinyDB(this)
        tinyDB.clear()
        tinyDB.putString("token", token)
        tinyDB.putString("username", username)
        Toast.makeText(this, "Token: $token", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}