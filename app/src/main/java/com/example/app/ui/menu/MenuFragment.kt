package com.example.app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.data.model.Product
import com.example.app.databinding.FragmentMenuBinding
import com.example.app.utility.TinyDB
import org.json.JSONObject

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var menuViewModel: MenuViewModel
    private lateinit var adapter: MenuAdapter

    private lateinit var gridView: GridView

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
                    val products = handleJson(response)
                    gridView = binding.gridMenu
                    val adapter = MenuAdapter(context, products)
                    gridView.adapter = adapter
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


    private fun handleJson(response: String): ArrayList<Product> {
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
            updateLocalProducts(productArray as ArrayList<Any>)
            return productArray
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
        return ArrayList()
    }

    private fun updateLocalProducts(products: ArrayList<Any>) {
        val tinyDB = TinyDB(context)
        tinyDB.remove("products")
        tinyDB.putListObject("products", products)
    }
}