package com.example.app.ui.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.data.model.UserModel
import com.example.app.databinding.FragmentInfoBinding
import com.example.app.ui.login.LoginActivity
import com.example.app.ui.updatePassword.UpdateActivity
import com.example.app.utility.TinyDB
import org.json.JSONObject

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var infoView: InfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        infoView =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(InfoViewModel::class.java)

        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoutButton.setOnClickListener {
            doLogout()
        }

        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            startActivity(intent)
        }

        val tinyDBObj = TinyDB(AppContext.getContext())
        doGetUserInfo(tinyDBObj.getString("username"))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doGetUserInfo(username: String) {
        val context = AppContext.getContext()
        val queue = Volley.newRequestQueue(context)
        val tinyDBObj = TinyDB(context)
        val url = "http://143.42.66.73:9090/api/user/read.php?view=single&username=$username"
        val resp: StringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                handleJson(response)
            },
            Response.ErrorListener { error ->
                if (error.networkResponse.statusCode == 401) {
                    Toast.makeText(context, "Token expired", Toast.LENGTH_LONG).show()
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
        }
        queue.add(resp)
    }

    private fun handleJson(response: String) {
        val tinyDBObj = TinyDB(AppContext.getContext())
        val jObject = JSONObject(response)
        val jSearchData = jObject.getJSONArray("user").getJSONObject(0)
        val issuer = jSearchData.getString("username")
        val textView = binding.userName
        textView.text = issuer
        val email = jSearchData.getString("userEmail")
        val textView2 = binding.userEmail
        textView2.text = email
        val avatar = jSearchData.getString("avatar")
        handleAvatar(avatar)
        val firstname = jSearchData.getString("firstname")
        val lastname = jSearchData.getString("lastname")
        val phone = jSearchData.getString("phone")
        val balance = jSearchData.getString("balance")
        val balanceView = binding.balance
        val premiumTier = jSearchData.getString("premiumTier")
        val creditCard = jSearchData.getString("creditCard")
        val issueUser = UserModel(
            email,
            issuer,
            firstname,
            lastname,
            phone,
            creditCard,
            avatar,
            balance.toDouble(),
            premiumTier.toInt()
        )
        tinyDBObj.putObject("user", issueUser)
    }

    private fun doLogout() {
        val context = AppContext.getContext()
        val tinyDBObj = TinyDB(context)
        tinyDBObj.remove("token")
        tinyDBObj.remove("user")
        Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    private fun handleAvatar(path: String) {
        val url = "http://143.42.66.73:8080/$path"
        val imageView = binding.userImage
        imageView.load(url)
    }


}