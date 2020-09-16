package io.robogrow.ui.growView

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import io.robogrow.classes.LightValueFormatter
import io.robogrow.R
import io.robogrow.RobogrowApplication
import io.robogrow.classes.Grow
import io.robogrow.classes.GrowEvent
import io.robogrow.classes.TimeOfDayFormatter
import io.robogrow.requests.AuthenticatedErrorListener
import io.robogrow.requests.grows.GetGrowById
import io.robogrow.utils.AppUtils

import kotlinx.android.synthetic.main.activity_grow_view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GrowViewActivity : AppCompatActivity() {

    lateinit var grow: Grow

    private lateinit var lineChartTemp: LineChart
    private lateinit var lineChartLight: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grow_view)
        setSupportActionBar(toolbar)

        val tvTemp = findViewById<TextView>(R.id.tv_temp)
        val tvHumidity = findViewById<TextView>(R.id.tv_humidity)
        val tvInfrared = findViewById<TextView>(R.id.tv_infrared)
        val tvLux = findViewById<TextView>(R.id.tv_lux)

        val rvGrowEvents = findViewById<RecyclerView>(R.id.rv_events)

        lineChartTemp = findViewById(R.id.linechart_temp)
        lineChartLight = findViewById(R.id.linechart_light)

        val actionBar: ActionBar? = (this as? AppCompatActivity)?.supportActionBar
        actionBar?.title = intent.getStringExtra("title")
        actionBar?.setDisplayHomeAsUpEnabled(true)

        var _id = intent.getStringExtra("id")

        val request = GetGrowById(_id, this@GrowViewActivity, Response.Listener {
            grow = it!!

            var current: GrowEvent = grow.events[grow.events.size - 1]

            // Set current values
            tvTemp.text = current.temp.toString() + "°"
            tvHumidity.text = current.humidity.toString() + "%"
            tvInfrared.text = current.infrared.toString()
            tvLux.text = current.lux.toString()

            // Create line chart DataSets
            fillGraphs(grow.events)

            var reducedEvents = ArrayList<GrowEvent>()
            // TODO("This is duplicated code and should at least be moved to a method")
            grow.events.forEachIndexed { index, it ->
                if (index % 10 == 0) {
                    reducedEvents.add(it)
                }
            }

            with(rvGrowEvents) {
                layoutManager = LinearLayoutManager(context)
                adapter = GrowEventRecyclerViewAdapter(
                    reducedEvents
                )
            }
        }, Response.ErrorListener {

        })

        RobogrowApplication.queue.addToRequestQueue(request)
    }

    fun fillGraphs(events: ArrayList<GrowEvent>) {

        val entries = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()
        val entries3 = ArrayList<Entry>()
        val entries4 = ArrayList<Entry>()
        val dates = ArrayList<Date>()

        events.forEachIndexed { index, it ->
            if (index % 10 == 0) {
                entries.add(Entry(index.toFloat(), it.temp))
                entries2.add(Entry(index.toFloat(), it.humidity))
                entries3.add(Entry(index.toFloat(), (it.infrared / 1000)))
                entries4.add(Entry(index.toFloat(), (it.lux / 1000)))

                dates.add(it.createDate)
            }
        }

        val v1 = LineDataSet(entries, "Temperature")
        val v2 = LineDataSet(entries2, "Humidity")
        val v3 = LineDataSet(entries3, "Infrared")
        val v4 = LineDataSet(entries4, "Lux")

        setupDataSet(v1, R.color.colorPrimary)
        setupDataSet(v2, R.color.colorAccent)
        setupDataSet(v3, R.color.roboRed)
        setupDataSet(v4, R.color.roboYellow)

        lineChartTemp.data = LineData(v1, v2)
        lineChartTemp.xAxis.labelRotationAngle = 0f
        lineChartTemp.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChartTemp.axisLeft.setDrawLabels(false)
        lineChartTemp.axisRight.textColor = Color.WHITE
        lineChartTemp.xAxis.textColor = Color.WHITE
        lineChartTemp.xAxis.valueFormatter = TimeOfDayFormatter(dates)
        lineChartTemp.legend.isEnabled = false
        lineChartTemp.description.text = "Temperature / Humidity"
        lineChartTemp.description.textColor = android.R.color.white

        // Lazy reversal here to make yellow appear behind red
        lineChartLight.data = LineData(v4, v3)
        lineChartLight.xAxis.labelRotationAngle = 0f
        lineChartLight.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChartLight.axisLeft.setDrawLabels(false)
        lineChartLight.axisRight.textColor = Color.WHITE
        lineChartLight.xAxis.textColor = Color.WHITE
        lineChartLight.xAxis.valueFormatter = TimeOfDayFormatter(dates)
        lineChartLight.legend.isEnabled = false
        lineChartLight.description.text = "Infrared / Lux"
        lineChartLight.description.textColor = android.R.color.white
        lineChartLight.axisRight.valueFormatter = LightValueFormatter()

        lineChartLight.invalidate()
        lineChartTemp.invalidate()
    }

    fun setupDataSet(set: LineDataSet, color: Int) {
        set.setDrawFilled(true)
        set.lineWidth = 3f
        set.valueTextColor = ContextCompat.getColor(baseContext, android.R.color.white)
        set.fillColor = ContextCompat.getColor(
            baseContext,
            color
        )
        set.fillAlpha = ContextCompat.getColor(
            baseContext,
            color
        )
        set.color = ContextCompat.getColor(
            baseContext,
            color
        )
        set.setCircleColor(color)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
