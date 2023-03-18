package com.example.app.ui.info

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.data.model.Product
import com.example.app.data.model.UserModel
import com.example.app.utility.TinyDB
import org.json.JSONObject
import java.time.format.DateTimeFormatter

class InfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is info Fragment"
    }
    val text: LiveData<String> = _text

    public fun doGetUserInfo(username: String) {
        val context = AppContext.getContext()
        val queue = Volley.newRequestQueue(context)
        val url = "http://143.42.66.73:9090/api/user/read.php?view=single&username=$username"
        val resp : StringRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                val jObject = JSONObject(response)
                val jSearchData = jObject.getJSONArray("user").getJSONObject(0)
                val issuer = jSearchData.getString("username")
                val email = jSearchData.getString("userEmail")
                val firstname = jSearchData.getString("firstname")
                val lastname = jSearchData.getString("lastname")
                val phone = jSearchData.getString("phone")
                val balance = jSearchData.getString("balance")
                val avatar = jSearchData.getString("avatar")
                val premiumTier = jSearchData.getString("premiumTier")
                val creditCard = jSearchData.getString("creditCard")
                val issueUser = UserModel(email, issuer, firstname, lastname, phone, creditCard, avatar, balance.toDouble(), premiumTier.toInt())
                val tinyDB = TinyDB(context)
                tinyDB.putObject("user", issueUser)
                println(issueUser.toString())
                Toast.makeText(context, "Welcome $username", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        ) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                val tinyDB = TinyDB(context)
                headers["Authorization"] = "Bearer " + tinyDB.getString("token")
                return headers
            }
        }
        queue.add(resp)
    }
}