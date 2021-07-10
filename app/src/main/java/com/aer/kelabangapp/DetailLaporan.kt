package com.aer.kelabangapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aer.kelabangapp.Model.ModelLihatLaporanJalan
import com.aer.kelabangapp.databinding.ActivityDetailLaporanBinding
import com.bumptech.glide.Glide

class DetailLaporan : AppCompatActivity() {
    lateinit var detailLaporanBinding: ActivityDetailLaporanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailLaporanBinding = ActivityDetailLaporanBinding.inflate(layoutInflater)
        setContentView(detailLaporanBinding.root)
        if (intent.hasExtra("detail")) {
            val laporan: ModelLihatLaporanJalan? = intent.getParcelableExtra("detail")
            if (laporan != null) {
                Glide.with(this)
                    .load("http://192.168.43.239/aplikasikelabang/public/asset-template/img/${laporan.fileGambar}")
                    .into(detailLaporanBinding.detailGambarJalan)
                Glide.with(this)
                    .load("http://192.168.43.239/aplikasikelabang/public/asset-template/img/${laporan.fileGambar2}")
                    .into(detailLaporanBinding.detailGambarJalan2)
                Glide.with(this)
                    .load("http://192.168.43.239/aplikasikelabang/public/asset-template/img/${laporan.fileGambar3}")
                    .into(detailLaporanBinding.detailGambarJalan3)
                detailLaporanBinding.detailNamaJalan.text = laporan.namaJalan
                detailLaporanBinding.detailstatus.text = laporan.status
                detailLaporanBinding.detailLat.text = laporan.latitude
                detailLaporanBinding.detailLng.text = laporan.longitude
            }
        }
    }
}