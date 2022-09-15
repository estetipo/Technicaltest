package com.carlos.satori.technical_test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlos.satori.technical_test.R
import com.carlos.satori.technical_test.api.ApiConstants
import com.carlos.satori.technical_test.data.model.movies.Results
import com.carlos.satori.technical_test.databinding.ItemGalleryBinding
import com.carlos.satori.technical_test.databinding.ItemMovieBinding

class MoviesAdapter(private var mList: List<Results>) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val vBind = ItemMovieBinding.bind(itemView)
    }
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        Glide.with(holder.vBind.movieBanner.context)
            .load(ApiConstants.imagePath+item.posterPath)
            .centerCrop()
            .into(holder.vBind.movieBanner)
        holder.vBind.movieText.text = item.title
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }
}