package com.example.app.ui.checkout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.data.model.Cart
import com.example.app.data.model.UserModel
import com.example.app.databinding.ActivityCheckoutBinding
import com.example.app.utility.TinyDB

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

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
        binding.totalPrice.text = totalPrice.toString()
        val user = tinyDB.getObject("user", UserModel::class.java) as UserModel
        binding.fullName.text = user.firstName + " " + user.lastName
        binding.userEmail.text = user.email
        binding.phone.text = user.phone

        confirmButton.setOnClickListener() {
            confirm(cartList)
        }
        cancelButton.setOnClickListener() {
            cancel()
        }
    }


    private fun confirm(cartList : ArrayList<Cart>) {
        val tinyDB = TinyDB(this)
        // TODO: update product in db
        for (item in cartList) {

        }
    }

    private fun cancel() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}