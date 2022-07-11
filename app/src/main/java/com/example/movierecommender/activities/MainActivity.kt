package com.example.movierecommender.activities

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.movierecommender.R
import com.example.movierecommender.adapters.AllShowsRecyclerAdapter
import com.example.movierecommender.models.ShowDataModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    //  url for querying our data
    private val url = "https://api.tvmaze.com/shows"

    private var showsModelArrayList: ArrayList<ShowDataModel>? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var fadeAnimation = Fade()
//        var decor: View = window.decorView
//
//        fadeAnimation.excludeTarget(android.R.id.statusBarBackground, true)
//        fadeAnimation.excludeTarget(android.R.id.navigationBarBackground, true)
//
//        window.enterTransition = fadeAnimation
//        window.exitTransition = fadeAnimation

        showsModelArrayList = ArrayList()
        recyclerView = findViewById(R.id.allShowsRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        queryData()
    }

    fun queryData() {

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                for (i in 0 until response.length()) {

                    val responseJSONObject: JSONObject = response.getJSONObject(i)
                    val showName: String = responseJSONObject.getString("name")
                    val showRating: String =
                        responseJSONObject.getJSONObject("rating").getString("average")

                    val showImage: String =
                        responseJSONObject.getJSONObject("image").getString("original")

                    val showId: String = responseJSONObject.getString("id")

                    showsModelArrayList!!.add(
                        ShowDataModel(
                            showImage = showImage,
                            showTitle = showName,
                            showRating = showRating,
                            showId = showId
                        )
                    )
                }

                progressBar?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE
                buildRecyclerView()

            },
            { error ->

                Toast.makeText(this, "$error occured", Toast.LENGTH_LONG).show()
            })

        requestQueue.add(jsonArrayRequest)
    }

    private fun buildRecyclerView() {

        val allShowsRecyclerAdapter = AllShowsRecyclerAdapter(showsModelArrayList, this)
        val gridLayoutManager = GridLayoutManager(this, 2)

        recyclerView!!.layoutManager = gridLayoutManager
        recyclerView!!.adapter = allShowsRecyclerAdapter

    }
}