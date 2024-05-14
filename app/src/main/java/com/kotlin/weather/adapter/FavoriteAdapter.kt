package com.kotlin.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.weather.databinding.RecyclerFavoriteRowBinding
import com.kotlin.weather.model.Favorite

class FavoriteAdapter(private val favoriteList: ArrayList<Favorite>,
                        private val deleteItemClicked : OnItemClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {


    //Silme işlemi için interface foksiyonu
    interface OnItemClickListener {
        fun deleteItem(favorite: Favorite,position: Int?,holder: ViewHolder)
    }

    class ViewHolder(val binding : RecyclerFavoriteRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerFavoriteRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.locationNameFavorite.text = favoriteList[position].location

        //Silme ikonuna basınca OnItemClickListener interface'e bildirdik
        holder.binding.deleteLocationFavorite.setOnClickListener {
            val favoriteItem = favoriteList[position]
            deleteItemClicked.deleteItem(favoriteItem,holder.adapterPosition,holder)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }
}