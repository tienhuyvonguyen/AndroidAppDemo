package com.example.app.ui.menu

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.data.model.Product
import com.example.app.databinding.FragmentMenuBinding
import org.json.JSONObject

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        menuViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(MenuViewModel::class.java)

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        doGetProducts()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doGetProducts() {
        try {
            val context = AppContext.getContext()
            val queue = Volley.newRequestQueue(context)
            val url = "http://143.42.66.73:9090/api/product/read.php?view=all"
            val resp: StringRequest = object : StringRequest(
                Request.Method.GET, url,
                Response.Listener { response ->
                    handleJson(response)
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
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleJson(response: String): Array<Product> {
        try {
            val jObject = JSONObject(response)
            val productArray = ArrayList<Product>()
            val jSearchData = jObject.getJSONArray("products")
            for (i in 0 until jSearchData.length()) {
                val jSearch = jSearchData.getJSONObject(i)
                val id = jSearch.getString("productID")
                val name = jSearch.getString("name")
                val price = jSearch.getString("price")
                val picture = jSearch.getString("picture")
                val stock = jSearch.getString("stock")
                val product = Product(id, name, picture, (price.toDouble()), stock.toInt())
                productArray.add(product)
            }
            return productArray.toTypedArray()
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
        return emptyArray()
    }

//    private fun createUIForProducts(products: Array<Product>) {
//        val context = AppContext.getContext()
//        val inflater = LayoutInflater.from(context)
//        val container = binding.menuContainer
//        for (product in products) {
//            val view = inflater.inflate(R.layout.product_item, container, false)
//            val name = view.findViewById<TextView>(R.id.productName)
//            val price = view.findViewById<TextView>(R.id.productPrice)
//            val stock = view.findViewById<TextView>(R.id.productStock)
//            name.text = product.name
//            price.text = product.price.toString()
//            stock.text = product.stock.toString()
//            container.addView(view)
//        }
//    }
}