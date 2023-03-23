package com.example.app.ui.login

import android.R
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import coil.load
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.MainActivity
import com.example.app.databinding.ActivityLoginBinding
import com.example.app.ui.register.RegisterActivity
import com.example.app.utility.TinyDB
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val signup = binding.register
        binding.appIcon.load("http://143.42.66.73:9090/uploads/save/1679506290693.png")
        // create a CancellationSignal variable and assign a value null to it
        signup.setOnClickListener {
            register()
        }
        login.setOnClickListener {
            val testInput = loginDataChange(username.text.toString(), password.text.toString())
            if (testInput) {
                doLogin(username.text.toString(), password.text.toString())
            }
        }
        val btnBioAuthen = binding.startAuthentication
        btnBioAuthen.isEnabled = false
        btnBioAuthen.setOnClickListener {
            //start authentication
        }
    }

    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun loginDataChange(username: String, password: String): Boolean {
        if (!isUserNameValid(username)) {
            binding.username.error = getString(com.example.app.R.string.invalid_username)
            Toast.makeText(applicationContext, "Invalid Username", Toast.LENGTH_SHORT).show()
        } else if (!isPasswordValid(password)) {
            binding.password.error = getString(com.example.app.R.string.invalid_password)
            Toast.makeText(applicationContext, "Invalid Password", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    private fun doLogin(username: String, password: String) {
        val context = AppContext.getContext()
        val url = "http://143.42.66.73:9090/public/api/login.php"
        val queue = Volley.newRequestQueue(this@LoginActivity)
        val req: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response: String ->
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                onLoginSuccess(response, username)
            },
            Response.ErrorListener { error ->
                if (error.networkResponse.statusCode == 400) {
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show()
                }
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
        return password.length > 8
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    //TODO: Biometric Authentication


}