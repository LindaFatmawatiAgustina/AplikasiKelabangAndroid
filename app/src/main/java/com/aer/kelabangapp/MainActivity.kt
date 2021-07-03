package com.aer.kelabangapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.aer.kelabangapp.databinding.ActivityMainBinding
import com.aer.kelabangapp.helper.SessionManager

class MainActivity : AppCompatActivity() {


    private lateinit var session: SessionManager
    lateinit var mainActivitybinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivitybinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivitybinding.root)
        session = SessionManager(this)


        mainActivitybinding.username.text = session.getNama()
        mainActivitybinding.tambahlaporan.setOnClickListener {
            startActivity(Intent(this, Tambah_laporan_jalan::class.java))
        }
        mainActivitybinding.laporanselesai.setOnClickListener {
            startActivity(Intent(this, Lihat_Laporan_Jalan_Selesai::class.java))
        }
        mainActivitybinding.info.setOnClickListener {
            startActivity(Intent(this, Info::class.java))
        }
        mainActivitybinding.keluar.setOnClickListener {
            session.logout()
            SweetAlertDialog(this@MainActivity, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Berhasil Keluar")
                .setConfirmClickListener {
                    startActivity(Intent(this@MainActivity,Login::class.java))
                    finish()
                }.show()
        }

    }
}