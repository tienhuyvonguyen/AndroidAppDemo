package com.example.app.ui.menu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import coil.load
import com.example.app.R
import com.example.app.data.model.Cart
import com.example.app.data.model.Product
import com.example.app.utility.TinyDB

class MenuAdapter(context: Context, courseModelArrayList: ArrayList<Product>) :
    ArrayAdapter<Product?>(context, 0, courseModelArrayList as List<Product?>) {
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false)
        }
        val product = getItem(position)
        val productName = listitemView!!.findViewById<TextView>(R.id.product_name)
        val productImage = listitemView.findViewById<ImageView>(R.id.product_image)
        val productStock = listitemView.findViewById<TextView>(R.id.product_stock)
        val productPrice = listitemView.findViewById<TextView>(R.id.product_price)
        productName.text = product!!.name
        productStock.text = "Stock: " + product.stock.toString()
        productPrice.text = "Price: " + product.price.toString() + " $"
        val baseURL = "http://143.42.66.73:8080/"
        productImage.load(baseURL + product.picture)
        val addBtn = listitemView.findViewById<Button>(R.id.btn_add_to_cart)

        var cartList: ArrayList<Any> = ArrayList()
        addBtn.setOnClickListener {
            val productToAdd : Cart
            if (product.stock == 0 || product.stock < 0) {
                Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                productToAdd = Cart(
                    product.productID, product.price, 1
                )
            }
            cartList = addToCart(productToAdd)
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
        }

        return listitemView
    }

    private fun addToCart(productToAdd: Cart): ArrayList<Any>{
        val tinyDB = TinyDB(context)
        val cartList = tinyDB.getListObject("cart", Cart::class.java)
        if (cartList.isNotEmpty()) { // have product in cart
            cartList.add(productToAdd)
            syncCart(cartList)
        } else { // cart empty
            tinyDB.remove("cart")
            cartList.add(productToAdd)
            syncCart(cartList)
        }
        return cartList
    }

    private fun syncCart(cartList: ArrayList<Any>){
        val tinyDB = TinyDB(context)
        tinyDB.putListObject("cart", cartList)
    }
}