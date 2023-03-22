package com.example.app.ui.info

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.app.AppContext
import com.example.app.data.model.UserModel
import com.example.app.databinding.FragmentInfoBinding
import com.example.app.ui.login.LoginActivity
import com.example.app.ui.updatePassword.UpdateActivity
import com.example.app.utility.TinyDB
import com.example.app.utility.VolleyMultipartRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.set


class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var infoView: InfoViewModel

    @RequiresApi(Build.VERSION_CODES.S)
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

        val tinyDBObj = TinyDB(AppContext.getContext())
        doGetUserInfo(tinyDBObj.getString("username"))


        binding.logoutButton.setOnClickListener {
            doLogout()
        }

        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            startActivity(intent)
        }

        binding.changeAvatar.setOnClickListener() {
            doChangeAvatar()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ), 1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            chooseWayToUpload()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doChangeAvatar() {
        requestPermission()
        chooseWayToUpload()
    }


    private fun chooseWayToUpload() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose way to upload")
        builder.setPositiveButton("Camera") { dialog, which ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }
        builder.setNegativeButton("Gallery") { dialog, which ->
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.userImage.setImageBitmap(bitmap)
            Log.d("image", bitmap.toString())
        } else if (requestCode == 2) {
            val uri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            binding.userImage.load(bitmap)
            Log.d("image", bitmap.toString())
        }
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

    @SuppressLint("SetTextI18n")
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
        balanceView.text = "$balance$"
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
        val url = "http://143.42.66.73:9090/$path"
        val imageView = binding.userImage
        imageView.load(url)
    }

    private fun onUpdateAvatarSuccess(resp: String) {
        val jObject = JSONObject(resp)
        val avatarPath = jObject.getString("avatar").substring(0, 13)
        println(avatarPath)
        handleAvatar(avatarPath)
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }



}