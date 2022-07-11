package com.example.movierecommender.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.R
import com.example.movierecommender.activities.ViewShowActivity
import com.example.movierecommender.models.ShowDataModel
import com.squareup.picasso.Picasso
import java.util.ArrayList

class AllShowsRecyclerAdapter(arrayList: ArrayList<ShowDataModel>?, private val context: Context) :
    RecyclerView.Adapter<AllShowsRecyclerAdapter.ViewHolder>() {

    private val showsArrayList: ArrayList<ShowDataModel>? = arrayList

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

        //  OnClick listeners
        holder.showImage.setOnClickListener {

            //  Make an image transition
            val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity, Pair(holder.showImage, "showImageTransition"))

            val intent = Intent(context as Activity, ViewShowActivity::class.java).apply {
                putExtra("showImageExtra", showDataModel.showImage)
            }

            context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return showsArrayList!!.size
    }

    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val showTitle: TextView = itemView.findViewById(R.id.showTitle)
        val showImage: ImageView = itemView.findViewById(R.id.showImage)
        val showRating: TextView = itemView.findViewById(R.id.showRating)

    }
}