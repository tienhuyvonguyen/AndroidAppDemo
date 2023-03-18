package com.example.app.ui.menu

import android.R
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.data.model.Product
import org.json.JSONObject


class MenuViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is menu Fragment"

    }
    val text: LiveData<String> = _text

    fun doGetProducts() {
        val context = AppContext.getContext()
        val queue = Volley.newRequestQueue(context)
        val url = "http://143.42.66.73:9090/api/product/read.php?view=all"
        val resp : StringRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                val jObject = JSONObject(response)
                val jSearchData = jObject.getJSONArray("products")
                for (i in 0 until jSearchData.length()) {
                    val jSearch = jSearchData.getJSONObject(i)
                    val id = jSearch.getString("productID")
                    val name = jSearch.getString("name")
                    val price = jSearch.getString("price")
                    val picture = jSearch.getString("picture")
                    val stock = jSearch.getString("stock")
                    val product = Product(id, name, picture, (price.toDouble()), stock.toInt())
                    println(product.toString())
                }
                // TODO: Organize data in list of products and send to adapter

            },
            Response.ErrorListener { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        ) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        queue.add(resp)
    }

    fun doOrganize (product: Product) {
        val list = ArrayList<Product>()
        list.add(product)
        // show product in list view

    }
}