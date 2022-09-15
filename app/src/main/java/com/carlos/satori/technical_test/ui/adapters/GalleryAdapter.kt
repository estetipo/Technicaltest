package com.carlos.satori.technical_test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlos.satori.technical_test.R
import com.carlos.satori.technical_test.data.model.Images
import com.carlos.satori.technical_test.databinding.ItemGalleryBinding

class GalleryAdapter(private var mList: List<String>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val vBind = ItemGalleryBinding.bind(itemView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]
        Glide.with(holder.vBind.imageGallery.context)
            .load(itemViewModel.toUri())
            .centerCrop()
            .into(holder.vBind.imageGallery)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }
}