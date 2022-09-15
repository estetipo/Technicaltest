package com.carlos.satori.technical_test.ui.fragments.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlos.satori.technical_test.data.model.movies.Results
import com.carlos.satori.technical_test.data.repository.online.MoviesRepository
import com.carlos.satori.technical_test.data.repository.room.MoviesRepositoryRoom
import com.carlos.satori.technical_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val moviesRepositoryRoom: MoviesRepositoryRoom
) : ViewModel() {
    private val _movies: MutableLiveData<List<Results>> = MutableLiveData()
    val movies = _movies
    fun getMovies() = viewModelScope.launch{
        moviesRepository.getMovies().collect(){
            when(it){
                is NetworkResult.Error ->{
                    moviesRepositoryRoom.get().collect(){
                        _movies.value = listOf()
                        it?.forEach {
                            _movies.value = _movies.value?.plus(it)
                        }
                    }
                }
                is NetworkResult.Loading ->{
                }
                is NetworkResult.Success ->{
                    _movies.value = it.data?.results
                    it.data?.let { it1 ->
                        moviesRepositoryRoom.deleteAll()
                        it.data.results.forEach{
                            moviesRepositoryRoom.upsert(it)
                        }
                    }
                }
            }
        }
    }
}