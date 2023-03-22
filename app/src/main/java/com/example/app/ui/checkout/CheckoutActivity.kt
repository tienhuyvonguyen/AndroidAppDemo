package com.example.app.ui.checkout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.data.model.Cart
import com.example.app.data.model.Product
import com.example.app.data.model.UserModel
import com.example.app.databinding.ActivityCheckoutBinding
import com.example.app.ui.info.InfoFragment
import com.example.app.ui.login.LoginActivity
import com.example.app.utility.TinyDB

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var userBinding: InfoFragment

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val confirmButton = binding.btnConfirm
        val cancelButton = binding.back2menu
        val tinyDB = TinyDB(this)
        val cartList = tinyDB.getListObject("cart", Cart::class.java) as ArrayList<Cart>
        var totalPrice = 0.0
        for (item in cartList) {
            totalPrice += item.productQuantity * item.productPrice
        }
        binding.totalPrice.text = "$totalPrice$"
        val user = tinyDB.getObject("user", UserModel::class.java) as UserModel
        binding.fullName.text = user.firstName + " " + user.lastName
        binding.userEmail.text = user.email
        binding.phone.text = user.phone
        val userBalance = user.balance
        if (userBalance < totalPrice) {
            confirmButton.isEnabled = false
            Toast.makeText(this, "Your balance is not enough", Toast.LENGTH_LONG).show()
        }
        confirmButton.setOnClickListener() {
            confirm(cartList)
            tinyDB.remove("cart")
        }
        cancelButton.setOnClickListener() {
            cancel()
        }
    }


    private fun confirm(cartList: ArrayList<Cart>) {
        try {
            val tinyDB = TinyDB(this)
            val products =
                tinyDB.getListObject("products", Product::class.java) as ArrayList<Product>
            for (item in cartList) {
                for (product in products) {
                    if (item.productID == product.productID) {
                        product.stock -= item.productQuantity
                        doUpdateProduct(item.productID.toInt(), product.stock)
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    private fun cancel() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun doUpdateProduct(id: Int, stock: Int) {
        val context = AppContext.getContext()
        val queue = Volley.newRequestQueue(context)
        val tinyDBObj = TinyDB(context)
        val url = "http://143.42.66.73:9090/api/product/update.php"
        val resp: StringRequest = object : StringRequest(
            Method.PUT, url,
            Response.Listener { response ->
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(context, "Thanks for your purchased", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                if (error.networkResponse.statusCode == 401) {
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
                params["id"] = id.toString()
                params["stock"] = stock.toString()

                // at last we are
                // returning our params.
                return params
            }
        }
        queue.add(resp)
    }
}
