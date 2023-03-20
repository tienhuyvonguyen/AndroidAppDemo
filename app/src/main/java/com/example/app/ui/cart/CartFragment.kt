package com.example.app.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.AppContext
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.data.model.Cart
import com.example.app.databinding.FragmentCartBinding
import com.example.app.ui.checkout.CheckoutActivity
import com.example.app.utility.TinyDB
import kotlinx.coroutines.NonDisposableHandle.parent


class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cartViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                CartViewModel::class.java
            )

        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cartList = TinyDB(AppContext.getContext()).getListObject("cart", Cart::class.java)
        if (cartList.isNotEmpty()) {
            val listView = binding.cartList
            val arrayAdapter = ArrayAdapter(this.requireContext(), R.layout.item_view, R.id.itemTextView, cartList)
            listView.adapter = arrayAdapter
        } else {
            ArrayAdapter(this.requireContext(), R.layout.item_view, R.id.itemTextView, listOf("Cart is empty"))
        }

        val checkoutButton = binding.btnCheckout
        checkoutButton.setOnClickListener {
            checkout()
        }

        val clearCartBtn = binding.btnClearCart
        clearCartBtn.setOnClickListener {
            TinyDB(AppContext.getContext()).remove("cart")
            val intent = Intent(this.context, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this.context, "Cart cleared", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkout() {
        startActivity(Intent(this.context, CheckoutActivity::class.java))
    }


}