package com.aer.kelabangapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.aer.kelabangapp.databinding.ActivityLoginBinding
import com.aer.kelabangapp.helper.SessionManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Login : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var queue: RequestQueue
    private lateinit var session: SessionManager
    private lateinit var pDialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        session = SessionManager(this)
        queue = Volley.newRequestQueue(this)
        loginBinding.btnLogin.setOnClickListener {
            val email = loginBinding.emailLog.text.toString()
            val pass = loginBinding.passwordLog.text.toString()
            if (email.trim().isEmpty() || pass.trim().isEmpty()) {
                pDialog = SweetAlertDialog(this@Login, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Isi Semua Form")
                    .setContentText("Mohon Periksa username/password anda")
                    .setConfirmClickListener {
                        hideDialog()
                    }
                showDialog()
            }else{
                loginAkun(email, pass)

            }

        }
        loginBinding.daftar.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

    }

    private fun showDialog() {
        if (!pDialog.isShowing) pDialog.show()
    }

    private fun hideDialog() {
        if (pDialog.isShowing) pDialog.dismiss()
    }

    fun loginAkun(email: String, password: String) {
        pDialog = SweetAlertDialog(this@Login, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"))
        pDialog.setCancelable(false)
        pDialog.setTitleText("Mohon Tunggu...")
        showDialog()
        val url = "http://192.168.1.4/aplikasikelabang/public/api/login"
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val datanya = JSONObject(response)
                    hideDialog()
                    if (datanya.getString("message").equals("success")) {
                        val id = datanya.getInt("id")
                        val nama = datanya.getString("nama")
                        session.saveLogin(id, nama, true)
                        SweetAlertDialog(this@Login, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Login Berhasil")
                            .setConfirmClickListener {
                            }.show()
                        hideDialog()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        //Handle Jika Gagal
                        pDialog = SweetAlertDialog(this@Login, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Login gagal")
                            .setContentText("Mohon Periksa username/password anda")
                            .setConfirmClickListener {
                                hideDialog()
                            }
                        showDialog()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error -> Log.d("API", error.toString()) }) {

                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    //Change with your post params
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }
        queue.add(request)
    }
}