package com.carlos.satori.technical_test.ui.fragments.gallery

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlos.satori.technical_test.data.firebase.FirebaseStorage
import com.carlos.satori.technical_test.data.model.Images
import com.carlos.satori.technical_test.data.repository.room.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    private val firebaseStorage = FirebaseStorage()
    private val _listImages: MutableLiveData<List<String>> = MutableLiveData()

    val listImages = _listImages
    fun getImages() = viewModelScope.launch {
        _listImages.value = firebaseStorage.getImages()
        if (_listImages.value!!.isEmpty()){
            imagesRepository.get().collect(){
                it?.forEach {
                   _listImages.value = _listImages.value?.plus(it.url.toString())
                }
            }
        }
        imagesRepository.deleteAll()
        _listImages.value!!.forEach {
            imagesRepository.upsert(Images(url = it))
        }
    }

    fun upsertImage(route:Uri,name:String) = viewModelScope.launch {
        imagesRepository.upsert(Images(url = route.toString()))
        firebaseStorage.saveImage(image = route,name = name)
    }
}