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
import java.util.ArrayList

class AllShowsRecyclerAdapter(arrayList: ArrayList<ShowDataModel>?, context: Context) :
    RecyclerView.Adapter<AllShowsRecyclerAdapter.ViewHolder>() {

    val showsArrayList: ArrayList<ShowDataModel>? = arrayList
    val context: Context = context


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val showTitle: TextView = itemView.findViewById(R.id.showTitle)
        val showImage: ImageView = itemView.findViewById(R.id.showImage)
        val showRating: TextView = itemView.findViewById(R.id.showRating)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.show_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //  working with the model class
        val showDataModel: ShowDataModel = showsArrayList!![position]
        holder.showTitle.text = showDataModel.showTitle
        holder.showRating.text = showDataModel.showRating
        Picasso.get().load(showDataModel.showImage).into(holder.showImage)
    }

    override fun getItemCount(): Int {
        return showsArrayList!!.size
    }
}