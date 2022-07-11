package com.example.movierecommender.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.movierecommender.R
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ViewShowActivity : AppCompatActivity() {

    private var viewShowImage: ImageView? = null
    private var viewShowTitle: TextView? = null

    private val url = "https://api.tvmaze.com/shows"
    private var showImageExtra: String? = null
    private var showTitleExtra: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_show)

        val intent = intent
        showImageExtra = intent.getStringExtra("showImageExtra")
        showTitleExtra = intent.getStringExtra("showTitleExtra")

        initVariables()

    }

    private fun queryData(id: Int) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            {response ->

                //  use the id from the intent
                val responseJsonObject: JSONObject = response.getJSONObject(id - 1)

                val showImage = responseJsonObject.getJSONObject("image").getString("original")

                //  Set the data in the XML
                Picasso.get().load(showImage).into(viewShowImage)

            },
            {error ->

                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            })

        requestQueue.add(jsonArrayRequest)
    }

    private fun initVariables() {

        viewShowImage = findViewById(R.id.viewShowImage)
        viewShowTitle = findViewById(R.id.viewShowTitle)

        Picasso.get().load(showImageExtra).into(viewShowImage)
        viewShowTitle?.text = showTitleExtra
    }
}