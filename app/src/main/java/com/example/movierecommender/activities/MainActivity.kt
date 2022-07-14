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
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.movierecommender.R
import com.example.movierecommender.adapters.AllShowsRecyclerAdapter
import com.example.movierecommender.adapters.TopRatedViewPagerAdapter
import com.example.movierecommender.models.ShowDataModel
import com.example.movierecommender.network.RequestSingleton
import me.relex.circleindicator.CircleIndicator3
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    //  url for querying our data
    private val url = "https://api.tvmaze.com/shows"
    private var viewPagerAdapter: TopRatedViewPagerAdapter? = null

    private var showsModelArrayList: ArrayList<ShowDataModel>? = null
    private var topRatedArrayList: ArrayList<ShowDataModel>? = null

    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var topRatedProgressBar: ProgressBar? = null
    private var viewPager2: ViewPager2? = null
    private var indicators: CircleIndicator3? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showsModelArrayList = ArrayList()
        topRatedArrayList = ArrayList()
        recyclerView = findViewById(R.id.allShowsRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        topRatedProgressBar = findViewById(R.id.topRatedProgressBar)
        viewPager2 = findViewById(R.id.topRatedViewPager)
        indicators = findViewById(R.id.circleIndicator)

        queryData()
    }

    fun queryData() {

//        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val requestQueue = RequestSingleton.getInstance(this.applicationContext).requestQueue

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
                    val showGenresArray = responseJSONObject.getJSONArray("genres")
                    val genresArrayList = ArrayList<String>()

                    for (j in 0 until showGenresArray.length()) {
                        genresArrayList.add(showGenresArray[j] as String)
                    }
                    //  Add to the top rated arraylist
                    if (showRating != "null") {

                        if (showRating.toDouble() > 8.8) {
                            topRatedArrayList!!.add(
                                ShowDataModel(
                                    showImage = showImage,
                                    showTitle = showName,
                                    showRating = showRating,
                                    showId = showId,
                                    genresArrayList
                                )
                            )
                        }
                    }
                }

                for (i in 0 until 50) {

                    val responseJSONObject: JSONObject = response.getJSONObject(i)
                    val showName: String = responseJSONObject.getString("name")
                    val showRating: String =
                        responseJSONObject.getJSONObject("rating").getString("average")

                    val showImage: String =
                        responseJSONObject.getJSONObject("image").getString("original")

                    val showId: String = responseJSONObject.getString("id")
                    val showGenresArray = responseJSONObject.getJSONArray("genres")
                    val genresArrayList = ArrayList<String>()

                    for (j in 0 until showGenresArray.length()) {
                        genresArrayList.add(showGenresArray[j] as String)
                    }

                    showsModelArrayList!!.add(
                        ShowDataModel(
                            showImage = showImage,
                            showTitle = showName,
                            showRating = showRating,
                            showId = showId,
                            genresArrayList
                        )
                    )

                }

                buildViewPager2()
                buildRecyclerView()

            },
            { error ->

                Toast.makeText(this, "$error occured", Toast.LENGTH_LONG).show()
            })

        RequestSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
//        requestQueue.add(jsonArrayRequest)
    }

    private fun buildRecyclerView() {

        progressBar?.visibility = View.GONE
        recyclerView?.visibility = View.VISIBLE

        val allShowsRecyclerAdapter = AllShowsRecyclerAdapter(showsModelArrayList, this)
        val gridLayoutManager = GridLayoutManager(this, 2)

        recyclerView!!.layoutManager = gridLayoutManager
        recyclerView!!.adapter = allShowsRecyclerAdapter

    }

    private fun buildViewPager2() {

        topRatedProgressBar?.visibility = View.GONE
        viewPager2?.visibility = View.VISIBLE
        viewPagerAdapter = TopRatedViewPagerAdapter(this, topRatedArrayList!!)
        viewPager2?.adapter = viewPagerAdapter

        indicators?.setViewPager(viewPager2)
    }
}