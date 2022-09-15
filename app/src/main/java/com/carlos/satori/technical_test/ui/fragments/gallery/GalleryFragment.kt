package com.carlos.satori.technical_test.ui.fragments.gallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.carlos.satori.technical_test.data.model.Images
import com.carlos.satori.technical_test.databinding.FragmentGalleryBinding
import com.carlos.satori.technical_test.ui.adapters.GalleryAdapter
import com.esafirm.imagepicker.features.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var binding: FragmentGalleryBinding
    private val CAMERA_REQUEST_CODE = 100
    private lateinit var imagePickerLauncher :ImagePickerLauncher
    private var gallery: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        imagePickerLauncher = register()
        init()
        permissions()
        binding.imageRecycler.setHasFixedSize(true)
        return binding.root
    }

    private fun permissions() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                setupImagePicker()
            } else {
                Toast.makeText(requireContext(),"Active los permisos de ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init() {
        binding.btnCamera.setOnClickListener{
            setupImagePicker()
        }
    }


    private fun setupImagePicker() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            imagePickerLauncher.launch(
                ImagePickerConfig {
                    mode = ImagePickerMode.MULTIPLE
                    returnMode = ReturnMode.NONE
                    isFolderMode = true
                    folderTitle = "Selecciona una imagen"
                    imageTitle = "Selecciona una imagen"
                    doneButtonText = "Listo"
                }
            )
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                CAMERA_REQUEST_CODE
            )
        }
    }

    private fun register(): ImagePickerLauncher {
        return registerImagePicker { images ->
            images.forEach { image ->
                    gallery.add(image.uri.toString())
                    binding.imageRecycler.adapter?.notifyDataSetChanged()
                    viewModel.upsertImage(image.uri,image.name)
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            setupImagePicker()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.imageRecycler.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.imageRecycler.adapter = GalleryAdapter(gallery)
        viewModel.listImages.observe(viewLifecycleOwner){
            gallery.clear()
            it.forEach { index->
                gallery.add(index)
            }
            binding.imageRecycler.adapter?.notifyDataSetChanged()
        }

        viewModel.getImages()

    }
}