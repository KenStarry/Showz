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
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.bumptech.glide.Glide
import com.example.movierecommender.R
import com.example.movierecommender.activities.ViewShowActivity
import com.example.movierecommender.models.CastModel
import com.example.movierecommender.models.ShowDataModel
import com.example.movierecommender.network.RequestSingleton
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.ArrayList

class AllShowsRecyclerAdapter(
    arrayList: ArrayList<ShowDataModel>?,
    private val context: Context

) :
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
            val options = ActivityOptions.makeSceneTransitionAnimation(
                context as Activity,
                Pair(holder.showImage, "showImageTransition"),
                Pair(holder.showTitle, "showTitleTransition")
            )

            val intent = Intent(context as Activity, ViewShowActivity::class.java).apply {
                putExtra("showImageExtra", showDataModel.showImage)
                putExtra("showTitleExtra", showDataModel.showTitle)
                putExtra("genresArrayList", showDataModel.showGenres)
            }

            queryCast()

            context.startActivity(intent, options.toBundle())
        }
    }

    private fun queryCast() {
        //  url for the cast
        val castUrl = "https://api.tvmaze.com/shows/5/cast"

        val castJsonArrayRequest =
            JsonArrayRequest(
                Request.Method.GET, castUrl, null,
                { castResponse ->

                    for (j in 0 until castResponse.length()) {

                        val castObject: JSONObject = castResponse.getJSONObject(j)

                        val pName =
                            castObject.getJSONObject("person").getString("name")
                        val pCountry =
                            castObject.getJSONObject("person").getJSONObject("country")
                                .getString("name")
                        val pBirthday =
                            castObject.getJSONObject("person").getString("birthday")
                        val pGender =
                            castObject.getJSONObject("person").getString("gender")
                        val pImage =
                            castObject.getJSONObject("person").getJSONObject("image")
                                .getString("original")
                        val characterName =
                            castObject.getJSONObject("character").getString("name")

//                        castsArrayList!!.add(
//                            CastModel(
//                                personName = pName,
//                                personBirthday = pBirthday,
//                                personCountry = pCountry,
//                                personGender = pGender,
//                                personImage = pImage,
//                                characterName = characterName
//                            )
//                        )
                    }

                },
                { castError ->

                })

        RequestSingleton.getInstance(context).addToRequestQueue(castJsonArrayRequest)
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