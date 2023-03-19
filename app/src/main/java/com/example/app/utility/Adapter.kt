package com.example.app.utility

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.app.R
import com.example.app.data.model.Product

class Adapter(context: Context, courseModelArrayList: ArrayList<Product>) :
    ArrayAdapter<Product?>(context, 0, courseModelArrayList as List<Product?>) {
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false)
        }
        val product = getItem(position)
        //        //TODO: set the text and image of the courseModel to the textview and imageview respectively.
        val productName = listitemView!!.findViewById<TextView>(R.id.product_name)
        val productImage = listitemView.findViewById<ImageView>(R.id.product_image)
        productName.text = product!!.name
        val baseURL = "http://143.42.66.73:8080/"
        productImage.load(baseURL + product.picture)
        //        use coil to load image from url to imageview in card_item.xml layout file
        return listitemView
    }
}