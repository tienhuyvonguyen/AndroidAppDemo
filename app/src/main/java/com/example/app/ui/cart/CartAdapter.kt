package com.example.app.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.app.R
import com.example.app.data.model.Cart
import com.example.app.utility.TinyDB

class CartAdapter(context: Context, courseModelArrayList: ArrayList<Any>) :
    ArrayAdapter<Any?>(context, 0, courseModelArrayList as List<Any?>) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.fragment_cart, parent, false)
        }
        val cart = getItem(position)
        val userCastList = TinyDB(context).getListObject("cart", Cart::class.java)

        return listitemView!!
    }
}