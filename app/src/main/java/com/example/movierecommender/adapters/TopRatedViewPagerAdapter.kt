package com.example.movierecommender.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.R
import com.example.movierecommender.models.ShowDataModel
import com.squareup.picasso.Picasso

class TopRatedViewPagerAdapter(
    private val context: Context,
    private val topRatedShowsArrayList: ArrayList<ShowDataModel>

) : RecyclerView.Adapter<TopRatedViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var topRatedShowImage: ImageView = itemView.findViewById(R.id.topRatedShowImage)
        var topRatedShowName: TextView = itemView.findViewById(R.id.topRatedShowName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {

        return ViewPagerViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.top_rated_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

        //  Get the views
        val model: ShowDataModel = topRatedShowsArrayList[position]

        holder.topRatedShowName.text = model.showTitle
        Picasso.get().load(model.showImage).into(holder.topRatedShowImage)
    }

    override fun getItemCount(): Int {
        return topRatedShowsArrayList.size
    }
}