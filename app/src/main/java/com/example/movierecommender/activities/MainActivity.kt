package com.example.movierecommender.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    val url = "https://api.tvmaze.com/shows"
    var showsModelArrayList: ArrayList<ShowDataModel>? = null
    var recyclerView: RecyclerView? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

                    showsModelArrayList!!.add(
                        ShowDataModel(
                            showImage = showImage,
                            showTitle = showName,
                            showRating = showRating
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