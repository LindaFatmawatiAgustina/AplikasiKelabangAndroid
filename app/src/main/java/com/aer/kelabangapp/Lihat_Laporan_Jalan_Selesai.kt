package com.aer.kelabangapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aer.kelabangapp.Adapter.AdapterLihatLaporanSelesai
import com.aer.kelabangapp.Model.ModelLihatLaporanJalan
import com.aer.kelabangapp.databinding.ActivityLihatLaporanJalanSelesaiBinding
import com.aer.kelabangapp.helper.SessionManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Lihat_Laporan_Jalan_Selesai : AppCompatActivity() {
    private lateinit var lihatLaporanJalanSelesaiBinding: ActivityLihatLaporanJalanSelesaiBinding
    private lateinit var adapterLihatLaporanSelesai: AdapterLihatLaporanSelesai
    private var list_laporan: ArrayList<ModelLihatLaporanJalan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lihatLaporanJalanSelesaiBinding =
            ActivityLihatLaporanJalanSelesaiBinding.inflate(layoutInflater)
        setContentView(lihatLaporanJalanSelesaiBinding.root)
        val sessionManager = SessionManager(this)
        lihatLaporan(sessionManager.getId())


    }

    fun lihatLaporan(id: Int?) {
        val url = "http://192.168.1.4/aplikasikelabang/public/api/lihatlaporan/$id"
        val queue = Volley.newRequestQueue(this)
        val request: StringRequest =
            object : StringRequest(Method.GET, url, Response.Listener { response ->
                try {
                    val laporane = ArrayList<ModelLihatLaporanJalan>()
                    val datanya = JSONObject(response).getJSONArray("data")
                    Log.e("data",datanya.toString())
                    Log.e("id",id.toString())
                    for (i in 0 until datanya.length()) {
                        val item = datanya.getJSONObject(i)
                        val laporan = ModelLihatLaporanJalan()
                        laporan.namaJalan = item.getString("nama_jalan")
                        laporan.latitude = item.getString("latitude")
                        laporan.longitude = item.getString("longitude")
                        laporan.tanggalLaporan = item.getString("tanggal_laporan")
                        laporan.fileGambar = item.getString("file_gambar")
                        laporan.fileGambar2 = item.getString("file_gambar2")
                        laporan.fileGambar3 = item.getString("file_gambar3")
                        laporan.status = item.getString("status")
                        laporane.add(laporan)
                        // Your code here
                    }
                    list_laporan.addAll(laporane)
                    adapterLihatLaporanSelesai = AdapterLihatLaporanSelesai(list_laporan,this)
                    lihatLaporanJalanSelesaiBinding.rvRiwayatLaporan.apply {

                        layoutManager = LinearLayoutManager(this@Lihat_Laporan_Jalan_Selesai)
                        adapter = adapterLihatLaporanSelesai

                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.d("API", error.toString()) }) {}

        queue.add(request)

    }
}