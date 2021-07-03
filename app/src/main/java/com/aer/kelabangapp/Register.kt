package com.aer.kelabangapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aer.kelabangapp.databinding.ActivityRegisterBinding
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Register : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        queue = Volley.newRequestQueue(this)
        registerBinding.btnRegisterReglayout.setOnClickListener {
            val name = registerBinding.namaReg.text.toString()
            val email = registerBinding.emailReg.text.toString()
            val password = registerBinding.passwordReg.text.toString()

            registerAkun(email, password, name)

        }
        registerBinding.btnLoginReglayout.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    fun registerAkun(email: String, password: String, nama: String) {
        val url = "http://192.168.1.5/aplikasikelabang/public/api/register"
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val datanya = JSONObject(response)
                    Log.d("register", datanya.toString())
                    if (datanya.getString("success") == "1") {
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    } else {
                        //Handle Jika Gagal
                        Toast.makeText(this, "Gagal Daftar", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.d("API", error.toString()) }) {

                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    //Change with your post params
                    params["nama"] = nama
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }
        queue.add(request)
    }
}