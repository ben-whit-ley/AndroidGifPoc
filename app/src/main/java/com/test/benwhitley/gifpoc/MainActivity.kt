package com.test.benwhitley.gifpoc

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

	private lateinit var adapter: GifAdapter

	private val gifRetriever = GifRetriever()

	private val callback = object : Callback<GifResult> {
		override fun onFailure(call: Call<GifResult>?, t: Throwable?) {
			Log.e("MainActivity", "Problem calling Giphy API", t)
		}

		override fun onResponse(call: Call<GifResult>?, response: Response<GifResult>?) {
			response?.isSuccessful.let {
				val body = response?.body()
				val resultList = GifResult(body?.data ?: emptyList())
				adapter.setResults(resultList)
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val gifList:RecyclerView = findViewById(R.id.gif_list)
		val layoutManager = LinearLayoutManager(this)
		gifList.layoutManager = layoutManager

		adapter = GifAdapter(this)
		gifList.adapter = adapter

		if (isNetworkConnected()) {
			gifRetriever.getGifs(callback)
		} else {
			AlertDialog.Builder(this).setTitle("No Internet Connection")
				.setMessage("Please check your internet connection and try again")
				.setPositiveButton(android.R.string.ok) { _, _ -> }
				.setIcon(android.R.drawable.ic_dialog_alert).show()
		}
	}

	private fun isNetworkConnected(): Boolean {
		val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
		val networkInfo = connectivityManager.activeNetworkInfo //2
		return networkInfo != null && networkInfo.isConnected //3
	}
}
