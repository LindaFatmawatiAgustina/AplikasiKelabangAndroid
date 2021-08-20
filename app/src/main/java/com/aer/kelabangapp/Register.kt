package com.aer.kelabangapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.aer.kelabangapp.databinding.ActivityRegisterBinding
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class Register : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var queue: RequestQueue
    private lateinit var pDialog: SweetAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        queue = Volley.newRequestQueue(this)
        registerBinding.btnRegisterReglayout.setOnClickListener {
            val name = registerBinding.namaReg.text.toString()
            val email = registerBinding.emailReg.text.toString()
            val password = registerBinding.passwordReg.text.toString()
            val alamat = registerBinding.alamatReg.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || alamat.isEmpty()) {

                SweetAlertDialog(this@Register, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf")
                    .setContentText("Isian tidak boleh kosong")
                    .show()

            } else {
                registerAkun(email, password, name, alamat)
            }


        }
        registerBinding.btnLoginReglayout.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun showDialog() {
        if (!pDialog.isShowing) pDialog.show()
    }

    private fun hideDialog() {
        if (pDialog.isShowing) pDialog.dismiss()
    }


    fun registerAkun(email: String, password: String, nama: String, alamat: String) {
        pDialog = SweetAlertDialog(this@Register, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"))
        pDialog.setCancelable(false)
        pDialog.setTitleText("Mohon Tunggu...")
        showDialog()
        val url = "http://192.168.1.4/aplikasikelabang/public/api/register"
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {

//                    if (response==("Email Sudah Digunakan")) {
//                        Log.d("emailada", response.toString())
//                        SweetAlertDialog(this@Register, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Maaf")
//                            .setContentText("Email Sudah Digunakan")
//                            .show()
////                    hideDialog();
//                    }
                    val datanya = JSONObject(response)
                    Log.d("register", datanya.toString())

                    if (datanya.getString("code") == "202") {
                        Log.d("emailada", response.toString())
                        SweetAlertDialog(this@Register, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Maaf")
                            .setContentText("Email Sudah Digunakan")
                            .show()
                        hideDialog()
                    }
                    if (datanya.getString("code") == "201") {
                        SweetAlertDialog(this@Register, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil Daftar")
                            .setContentText("Silahkan Login")
                            .show()
                        startActivity(Intent(this, Login::class.java))
                        Toast.makeText(
                            this@Register, "Berhasil Daftar, Silahkan Login",
                            Toast.LENGTH_LONG
                        ).show();
                        finish()

                    } else {
                        //Handle Jika Gagal
                        SweetAlertDialog(this@Register, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal Daftar")
                            .setContentText("Periksa Data anda")
                            .show()
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
                    params["alamat"] = alamat
                    return params
                }
            }
        queue.add(request)
    }
}