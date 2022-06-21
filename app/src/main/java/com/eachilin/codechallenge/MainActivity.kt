package com.eachilin.codechallenge

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eachilin.codechallenge.databinding.ActivityMainBinding
import com.eachilin.codechallenge.seatgeekapi.SeatGeek
import com.eachilin.database.EventLike
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.seatgeek.com/2/"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var eventInformation = ArrayList<Event>()
    private var adapter = EventAdapter(eventInformation)
    private lateinit var rvEvents :RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvEvents = binding.rvEvents
        rvEvents.adapter = adapter
        rvEvents.layoutManager = LinearLayoutManager(this)



        fetchData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_home_menu, menu)
        val search : MenuItem? = menu?.findItem(R.id.nav_search)
        val cancel :MenuItem? = menu?.findItem(R.id.nav_cancel)

        val searchView = search?.actionView as SearchView
        searchView.queryHint= "Search"

        // cancel should not be visible until icon is clicked
        cancel?.isEnabled = false
        cancel?.isVisible= false

        cancel?.setOnMenuItemClickListener {
            // reset top nav navbar to previous state
            searchView.setQuery("", false)
            searchView.clearFocus()
            searchView.isIconified = true
            searchView.setBackgroundResource(0)
            cancel.isEnabled = false
            cancel.isVisible = false
            supportActionBar!!.setDisplayShowHomeEnabled(true)

            return@setOnMenuItemClickListener true
        }



        searchView.setOnSearchClickListener {
            searchView.background = getDrawable(R.drawable.search_bar_background)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
            cancel?.isEnabled = true
            cancel?.isVisible= true
        }




        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.e(TAG,"SELECTED")

                return false
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onQueryTextChange(searchQuery: String?): Boolean {

                // remove title and display cancel button

                filter(searchQuery!!.lowercase())
                return  true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(searchQuery: String?) {
        val searchQueryEvents = ArrayList<Event>()

        if(searchQuery!!.isNotEmpty()){
            for(item in eventInformation){
                //lowercase text to match with user input
                val text = item.title.lowercase()
                if(text.contains(searchQuery)){
                    searchQueryEvents.add(item)
                }
            }
            if(searchQueryEvents.isEmpty()){
                Toast.makeText(this, "No Information can be found", Toast.LENGTH_SHORT).show()
            }
            rvEvents.adapter = EventAdapter(searchQueryEvents)

        }


        if(searchQuery.isEmpty()){
            rvEvents.adapter = EventAdapter(eventInformation)
        }

        adapter.notifyDataSetChanged()



    }

    private fun fetchData() {
        val search = "swift"
        val client = getString(R.string.seat_geek_api)
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val seatGeekService = retrofit.create(SeatGeek::class.java)
        seatGeekService.getSeatGeekInfo(client, search)
            .enqueue(object : Callback<EventResult>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<EventResult>, response: Response<EventResult>) {
                    Log.i(TAG, response.body().toString())

                    eventInformation.addAll(response.body()!!.events)
                    adapter.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<EventResult>, t: Throwable) {
                    Log.e(TAG, "unable to fetch data")
                }

            })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        // refresh layout if user press the devices back button
        adapter.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRestart() {
        super.onRestart()
        // refresh layout if user press the devices back button
        adapter.notifyDataSetChanged()
    }
}