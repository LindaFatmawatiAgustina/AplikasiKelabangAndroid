package com.aer.kelabangapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.aer.kelabangapp.databinding.ActivityTambahLaporanJalanBinding
import com.aer.kelabangapp.helper.SessionManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


class Tambah_laporan_jalan : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var tambahLaporanJalanBinding: ActivityTambahLaporanJalanBinding
    private lateinit var session: SessionManager
    //    private var UriPhoto: Uri? = null
    private var BitPhoto: Bitmap? = null
    var latnow = 0.0
    var longnow = 0.0
    private var StringImage: String = ""
    private var StringImage2: String = ""
    private var StringImage3: String = ""
    private var Nama_Jalan: String = ""
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var pDialog: SweetAlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        tambahLaporanJalanBinding = ActivityTambahLaporanJalanBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(tambahLaporanJalanBinding.root)
        session = SessionManager(this)

        updateLokasi()
        tambahLaporanJalanBinding.btUploadLokasi.setOnClickListener {
            uploadLokasi()
        }
        tambahLaporanJalanBinding.btUploadJalan.setOnClickListener {
            pickImage()
        }
        tambahLaporanJalanBinding.btUploadJalan2.setOnClickListener { pickImage2() }
        tambahLaporanJalanBinding.btUploadJalan3.setOnClickListener { pickImage3() }
        tambahLaporanJalanBinding.simpanLaporan.setOnClickListener {
            try {
                if (Nama_Jalan.trim().isEmpty() || StringImage.trim().isEmpty()) {
                    pDialog =
                        SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Isi minimal 1 foto dan pilih lokasi")
                            .setConfirmClickListener {
                                hideDialog()
                            }
                    showDialog()
                } else if (latnow.isNaN() || longnow.isNaN()) {
                    pDialog =
                        SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Periksa Lokasi Anda")
                            .setConfirmClickListener {
                                hideDialog()
                            }
                    showDialog()
                } else {
                    tambahLaporan(
                        session.getId().toString(),
                        Nama_Jalan,
                        latnow,
                        longnow,
                        StringImage,
                        StringImage2,
                        StringImage3
                    )

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showDialog() {
        if (!pDialog.isShowing) pDialog.show()
    }

    private fun hideDialog() {
        if (pDialog.isShowing) pDialog.dismiss()
    }

    private fun tambahLaporan(
        id: String,
        nama_jalan: String,
        lat: Double,
        lng: Double,
        gambar: String,
        gambar2: String,
        gambar3: String
    ) {
        pDialog = SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"))
        pDialog.setCancelable(false)
        pDialog.setTitleText("Mohon Tunggu...")
        val url = "http://192.168.43.239/aplikasikelabang/public/api/tambahlaporan/$id"
        showDialog()
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    hideDialog()
                    val datanya = JSONObject(response)
                    Log.d("register", datanya.toString())
                    if (datanya.getString("message").equals("success")) {
                        SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil Menambahkan Laporan")
                            .setConfirmClickListener {
                                hideDialog()
                                finish()
                            }.show()

                    } else {
                        SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Gagal Menambahkan Laporan")
                            .setContentText("Coba Lagi")
                            .setConfirmClickListener {
                                hideDialog()
                            }.show()
                    }


                } catch (e: java.lang.Exception) {
                    SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal Menambahkan Laporan")
                        .setContentText("Coba Lagi")
                        .setConfirmClickListener {
                            hideDialog()
                        }.show()
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                SweetAlertDialog(this@Tambah_laporan_jalan, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal Menambahkan Laporan")
                    .setContentText("Coba Lagi")
                    .setConfirmClickListener {
                        hideDialog()
                    }.show()
                Log.d("API", error.toString())
            }) {

                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    //Change with your post params
                    if (StringImage2.trim().isNotEmpty()){
                        params["file_gambar2"] = gambar2
                    }
                    if (StringImage3.trim().isNotEmpty()){
                        params["file_gambar3"] = gambar3
                    }
                    params["nama_jalan"] = nama_jalan
                    params["latitude"] = lat.toString()
                    params["longitude"] = lng.toString()
                    params["file_gambar"] = gambar
                    Log.d("Tambah Post", params.toString())
                    return params

                }
            }
        request.retryPolicy = DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(200)
    private fun pickImage() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val a = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(12, 6)
                .getIntent(this)
            startActivityForResult(a, 1)
        } else {
            EasyPermissions.requestPermissions(
                this, "Membutuhkan akses kamera",
                200, *perms
            )
        }
    }

    @AfterPermissionGranted(200)
    private fun pickImage2() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val a = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(12, 6)
                .getIntent(this)
            startActivityForResult(a, 3)
        } else {
            EasyPermissions.requestPermissions(
                this, "Membutuhkan akses kamera",
                200, *perms
            )
        }
    }

    @AfterPermissionGranted(200)
    private fun pickImage3() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val a = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(12, 6)
                .getIntent(this)
            startActivityForResult(a, 4)
        } else {
            EasyPermissions.requestPermissions(
                this, "Membutuhkan akses kamera",
                200, *perms
            )
        }
    }

    private fun uploadLokasi() {
        val i = PlacePicker.IntentBuilder()
            .setLatLong(latnow, longnow)// Initial Latitude and Longitude the Map will load into
            // Show Coordinates in the Activity
            .setMapZoom(17.0f) // Map Zoom Level. Default: 14.0
            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
            .setMarkerImageImageColor(R.color.colorPrimary)

            .setMapType(MapType.NORMAL)

            .build(this@Tambah_laporan_jalan)
        startActivityForResult(i, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val    UriPhoto = result.uri
                    if (UriPhoto != null) {
                        try {
                            val inputStream: InputStream? =
                                contentResolver.openInputStream(UriPhoto!!)
                            BitPhoto = BitmapFactory.decodeStream(inputStream)
                            StringImage = imgToString(BitPhoto!!)
                            tambahLaporanJalanBinding.gambarJalan.setImageURI(UriPhoto)
                            Log.d("TAMBAH-IMAGE", StringImage)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            2 -> if (resultCode == Activity.RESULT_OK && data != null) {
                val addressData: AddressData? = data.getParcelableExtra(Constants.ADDRESS_INTENT)
                Log.d("jalan", addressData?.addressList!![0].getAddressLine(0).toString())
                latnow = addressData.latitude
                longnow = addressData.longitude
                Nama_Jalan = addressData.addressList!![0].getAddressLine(0).toString()
                tambahLaporanJalanBinding.setLokasi.setText(
                    addressData.addressList!![0].getAddressLine(
                        0
                    )
                )
            }
            3 -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val   UriPhoto = result.uri
                    if (UriPhoto != null) {
                        try {
                            val inputStream: InputStream? =
                                contentResolver.openInputStream(UriPhoto!!)
                            BitPhoto = BitmapFactory.decodeStream(inputStream)
                            StringImage2 = imgToString(BitPhoto!!)
                            tambahLaporanJalanBinding.gambarJalan2.setImageURI(UriPhoto)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            4 -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val    UriPhoto = result.uri
                    if (UriPhoto != null) {
                        try {
                            val inputStream: InputStream? =
                                contentResolver.openInputStream(UriPhoto!!)
                            BitPhoto = BitmapFactory.decodeStream(inputStream)
                            StringImage3 = imgToString(BitPhoto!!)
                            tambahLaporanJalanBinding.gambarJalan3.setImageURI(UriPhoto)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun imgToString(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
        val imageByte: ByteArray = outputStream.toByteArray()
        return Base64.encodeToString(imageByte, Base64.DEFAULT)
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latnow = mLastLocation.latitude
            longnow = mLastLocation.longitude
        }
    }

    private fun updateLokasi() {
        buildLocationRequest()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(
            this
        ) { location: Location? ->
            if (location != null) {
                // Do it all with location
                // Display in Toast
//                locationnya = Location(location)
                latnow = location.latitude
                longnow = location.longitude
                Log.d(
                    "My Current location",
                    "Lat : " + latnow + " Long : " + longnow
                )
                val geo = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>
                try {
                    addresses =
                        geo.getFromLocation(location.latitude, location.longitude, 1)
                    val address: String = addresses[0]
                        .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    binding.setLokasi.setText(address)
                    // Only if available else return NULL
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("ERROR", e.message!!)
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
}