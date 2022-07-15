package com.example.movierecommender.activities

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
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
    private var toolbar: Toolbar? = null
    private var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        showsModelArrayList = ArrayList()
        topRatedArrayList = ArrayList()
        recyclerView = findViewById(R.id.allShowsRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        topRatedProgressBar = findViewById(R.id.topRatedProgressBar)
        viewPager2 = findViewById(R.id.topRatedViewPager)
        indicators = findViewById(R.id.circleIndicator)
        bottomNav = findViewById(R.id.bottomNav)

        //  Set default selected bottom nav item
        bottomNav?.menu?.findItem(R.id.bottomHome)?.isChecked = true
        bottomNav?.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.bottomSettings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)

                    it.isChecked = true
                }

                R.id.bottomFavourites -> {
                    Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()

                    it.isChecked = true
                }
            }

            return@setOnItemSelectedListener false
        }


        queryData()
    }

    private fun queryData() {

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

                buildViewPager2(showsModelArrayList!!)
                buildRecyclerView(showsModelArrayList!!)

            },
            { error ->

                Toast.makeText(this, "$error occured", Toast.LENGTH_LONG).show()
            })

        RequestSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
    }

    private fun buildRecyclerView(showsArrayList: ArrayList<ShowDataModel>) {

        val allShowsRecyclerArrayList = ArrayList<ShowDataModel>()

        for (i in 0 until showsArrayList.size) {
            allShowsRecyclerArrayList.add(showsArrayList[i])
        }

        progressBar?.visibility = View.GONE
        recyclerView?.visibility = View.VISIBLE

        val allShowsRecyclerAdapter = AllShowsRecyclerAdapter(allShowsRecyclerArrayList, this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = allShowsRecyclerAdapter

    }

    private fun buildViewPager2(showsArrayList: ArrayList<ShowDataModel>) {

        val topArrayList = ArrayList<ShowDataModel>()

        for (i in  0 until showsArrayList.size) {

            if (showsArrayList[i].showRating != "null") {

                if (showsArrayList[i].showRating.toDouble() > 8.8) {

                    showsArrayList[i].let { topArrayList.add(it) }
                }
            }

        }

        Toast.makeText(applicationContext, showsArrayList.size.toString(), Toast.LENGTH_SHORT).show()


        topRatedProgressBar?.visibility = View.GONE
        viewPager2?.visibility = View.VISIBLE
        viewPagerAdapter = TopRatedViewPagerAdapter(this, topArrayList)
        viewPager2?.adapter = viewPagerAdapter

        indicators?.setViewPager(viewPager2)
    }

    //  On Creating the menu items
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater: MenuInflater = menuInflater

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.darkModeMenu -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        return super.onOptionsItemSelected(item)
    }


}