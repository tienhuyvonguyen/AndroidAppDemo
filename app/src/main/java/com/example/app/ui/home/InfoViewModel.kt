package com.example.app.ui.home

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

class InfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is info Fragment"
    }
    val text: LiveData<String> = _text

    private fun doGetProduct(username: String, password: String, email: String) {
        val context = AppContext.getContext()
        val url = "http://143.42.66.73:9090/api/product/read.php?view=all"
        val queue = Volley.newRequestQueue(context)
        val req: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response: String ->
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Response is $response", Toast.LENGTH_SHORT).show()
                organizeData(response)
            },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(
                    context,
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
    // TODO: handle the resp and show the data in the recycler view
    private fun organizeData(Response: String) {
    }
}