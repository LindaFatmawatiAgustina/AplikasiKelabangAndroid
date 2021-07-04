package com.aer.kelabangapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aer.kelabangapp.DetailLaporan
import com.aer.kelabangapp.Model.ModelLihatLaporanJalan
import com.aer.kelabangapp.databinding.ItemLaporanBinding
import com.bumptech.glide.Glide

/**
 * Created by Dhimas Panji Sastra on
 * Copyright (c)  . All rights reserved.
 * Last modified $file.lastModified
 * Made With ❤ for U
 */
class AdapterLihatLaporanSelesai(
    private val dataset: List<ModelLihatLaporanJalan>,
    private val context: Context
) : RecyclerView.Adapter<AdapterLihatLaporanSelesai.ViewHolder>() {
    class ViewHolder(view: ItemLaporanBinding) : RecyclerView.ViewHolder(view.root) {
        var binding: ItemLaporanBinding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLaporanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.listLatitude.text = dataset[position].latitude
        holder.binding.listLongitude.text = dataset[position].longitude
        holder.binding.listNamaJalan.text = dataset[position].namaJalan
        holder.binding.tanggalLaporan.text = dataset[position].tanggalLaporan
        holder.binding.Status.text = dataset[position].status
        Glide.with(context).load("http://192.168.1.5/aplikasikelabang/public/asset-template/img/${dataset[position].fileGambar}").into(holder.binding.ivJalan)

        holder.itemView.setOnClickListener {
            val i = Intent(context,DetailLaporan::class.java)
            i.putExtra("detail",dataset[position])
            context.startActivity(i)
        }
    }
}