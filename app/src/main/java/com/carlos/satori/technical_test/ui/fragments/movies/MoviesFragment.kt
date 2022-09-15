package com.carlos.satori.technical_test.ui.fragments.movies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.carlos.satori.technical_test.data.model.movies.Results
import com.carlos.satori.technical_test.databinding.FragmentMoviesBinding
import com.carlos.satori.technical_test.ui.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var binding: FragmentMoviesBinding
    private var movies: MutableList<Results> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        viewModel.getMovies()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.imageRecycler.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        binding.imageRecycler.adapter = MoviesAdapter(movies)
        viewModel.movies.observe(viewLifecycleOwner){
            movies.clear()
            if(it!=null){
                it.forEach { index->
                    movies.add(index)
                }
                binding.imageRecycler.adapter?.notifyDataSetChanged()
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

}