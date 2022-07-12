package com.example.movierecommender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.R
import java.util.ArrayList

class GenresRecyclerAdapter(
    private val context: Context,
    private val genresArrayList: ArrayList<String>

) : RecyclerView.Adapter<GenresRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.genre_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.genreTitle.text = genresArrayList[position]
    }

    override fun getItemCount(): Int {
        return genresArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val genreTitle: TextView = itemView.findViewById(R.id.genreTitle)

    }
}