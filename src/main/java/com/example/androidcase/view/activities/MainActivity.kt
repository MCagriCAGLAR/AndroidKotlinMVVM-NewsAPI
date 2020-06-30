package com.example.androidprotelcase.view.activities

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidprotelcase.R
import com.example.androidprotelcase.model.NewsDetails
import com.example.androidprotelcase.view.adapters.NewsListAdapter
import com.example.androidprotelcase.viewmodel.NewsListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_datepicker.*
import kotlinx.android.synthetic.main.recycler_news_row.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private lateinit var newsViewModel: NewsListViewModel
    private var adapter: NewsListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var items: ArrayList<NewsDetails>? = ArrayList()
    private var year: Int? = 0
    private var month: Int? = 0
    private var day: Int? = 0
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        newsViewModel = ViewModelProviders.of(this).get(NewsListViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(dashboardToolbar)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.format(Date().time - (60 * 60 * 24 * 10 * 1000))

        newsViewModel.mFrom = "${date}T00:00:00Z"
        newsViewModel.mTo = "${date}T23:59:59Z"
        Log.d("MCC", "mFrom: ${newsViewModel.mFrom} - mTo: ${newsViewModel.mTo}")
        newsViewModel.getNewsData()
        newsViewModel.newsList.observe(this, Observer { data ->
            items!!.addAll(data)
            adapter!!.notifyDataSetChanged()
        })

        rwNews.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        rwNews.layoutManager = layoutManager
        adapter = NewsListAdapter(this, items!!)
        rwNews.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menuFilters -> {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.dialog_datepicker)
                dialog.setCancelable(true)
                dialog.show()

                val butonApply = dialog.findViewById<Button>(R.id.butonApply)
                val calendarView = dialog.findViewById<CalendarView>(R.id.calendarView)
                calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener{
                    override fun onSelectedDayChange(p0: CalendarView, p1: Int, p2: Int, p3: Int) {
                        val timestamp = Timestamp(p0.date)
                        val sdf = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
                        val date = sdf.format(timestamp).toString()
                        year = p1
                        month = p2
                        day = p3
                        Log.d("MCC", "date: $date")
                        Log.d("MCC", "year: $p1 - month: $p2 - day: $p3")
                    }
                })

                butonApply.setOnClickListener {
                    displayDialog()
                    Log.d("MCC", "year: $year - month: $month - day: $day")
                    newsViewModel.mFrom = "$year-${month!!+1}-${day}T00:00:00Z"
                    newsViewModel.mTo = "$year-${month!!+1}-${day}T23:59:00Z"
                    Log.d("MCC", "mFrom: ${newsViewModel.mFrom} - mTo: ${newsViewModel.mTo}")
                    newsViewModel.getNewsData()
                    newsViewModel.newsList.observe(this, Observer { data ->
                        items!!.clear()
                        items!!.addAll(data)
                        adapter!!.notifyDataSetChanged()
                        dialog.dismiss()
                        if (progressDialog != null && progressDialog!!.isShowing)
                            progressDialog!!.dismiss()
                    })
                }

                return true
            }
        }
        return false
    }

    fun displayDialog(){
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please waiting...")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

    }

}