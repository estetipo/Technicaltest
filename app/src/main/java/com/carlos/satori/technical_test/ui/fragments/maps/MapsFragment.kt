package com.carlos.satori.technical_test.ui.fragments.maps

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlos.satori.technical_test.R
import com.carlos.satori.technical_test.databinding.FragmentMapsBinding
import com.carlos.satori.technical_test.service.LocationService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

private var gps_enabled = false

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val viewModel: MapsViewModel by viewModels()
    private lateinit var binding: FragmentMapsBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    lateinit var intent: Intent


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        intent = Intent(requireContext(), LocationService::class.java)
        setup()
        brodcast()
        initMap()
    }

    private fun setup() {
        binding.switch1.isChecked = LocationService.isRunning
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            setupLocation()
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (gps_enabled) {
                        mMap.isMyLocationEnabled = true
                        if (!LocationService.isRunning) {
                            requireActivity().startService(intent)
                        }
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Por favor encienda su ubicación para continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                        mMap.isMyLocationEnabled = false
                        binding.switch1.isChecked = false
                    }
                } else {
                    binding.switch1.isChecked = false
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                    Toast.makeText(
                        requireContext(),
                        "Active los permisos de ubicación",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (LocationService.isRunning) {
                    requireActivity().stopService(intent)
                }
            }
        }
    }

    private fun setupLocation() {
        val lm = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(requireActivity())
                .setMessage("Por favor encienda su ubicación para continuar")
                .setPositiveButton("Abrir configuración",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        requireContext().startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    }).setNegativeButton("Cancel", null)
                .show()
        }
    }

    fun brodcast() {
        val br = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                var latitude = p1?.getStringExtra("latitude")?.toDouble()
                var longitude = p1?.getStringExtra("longitude")?.toDouble()
                if (latitude != null && longitude != null && LocationService.isRunning) {
                    if (viewModel.currentLat != latitude && viewModel.currentLng != longitude) {
                        viewModel.currentLat = latitude
                        viewModel.currentLng = longitude
                        viewModel.addLocation(
                            lat = latitude,
                            lng = longitude,
                            context = requireContext()
                        )
                    }
                }
            }
        }
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction("android.intent.action.NEW_LOCATION")
        }
        requireActivity().registerReceiver(br, filter)
    }

    private fun initMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.f_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        viewModel.getLocations()
        viewModel.listLocations.observe(viewLifecycleOwner) {
            it?.forEach { marker ->
                mMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            marker.lat ?: 0.0,
                            marker.lng ?: 0.0
                        )
                    ).title(marker.date)
                )
            }
        }
    }
}