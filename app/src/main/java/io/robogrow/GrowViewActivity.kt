package io.robogrow

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import kotlinx.android.synthetic.main.activity_grow_view.*

class GrowViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grow_view)
        setSupportActionBar(toolbar)

        val actionBar: ActionBar? = (this as? AppCompatActivity)?.supportActionBar
        actionBar?.title = intent.getStringExtra("title")
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val lineChartTemp: LineChart = findViewById(R.id.linechart_temp)
        val lineChartLight: LineChart = findViewById(R.id.linechart_light)

        val entries = ArrayList<Entry>()
        val entries2 = ArrayList<Entry>()

        entries.add(Entry(1f, 60f))
        entries.add(Entry(2f, 62f))
        entries.add(Entry(3f, 61f))
        entries.add(Entry(4f, 65f))
        entries.add(Entry(5f, 66f))
        entries2.add(Entry(1f, 45f))
        entries2.add(Entry(2f, 46f))
        entries2.add(Entry(3f, 50f))
        entries2.add(Entry(4f, 46f))
        entries2.add(Entry(5f, 48f))

        val v1 = LineDataSet(entries, "Temperature")
        val v2 = LineDataSet(entries2, "Humidity")

        v1.setDrawFilled(true)
        v1.lineWidth = 3f
        v1.valueTextColor = ContextCompat.getColor(baseContext, android.R.color.white)
        v1.fillColor = ContextCompat.getColor(baseContext, R.color.colorPrimary)
        v1.fillAlpha = ContextCompat.getColor(baseContext, R.color.colorPrimary)
        v1.color = ContextCompat.getColor(baseContext, R.color.colorPrimary)
        v1.setCircleColor(R.color.colorPrimary)

        v2.setDrawFilled(true)
        v2.lineWidth = 3f
        v2.valueTextColor = ContextCompat.getColor(baseContext, android.R.color.white)
        v2.fillColor = ContextCompat.getColor(baseContext, R.color.colorAccent)
        v2.fillAlpha = ContextCompat.getColor(baseContext, R.color.colorAccent)
        v2.color = ContextCompat.getColor(baseContext, R.color.colorAccent)
        v2.setCircleColor(R.color.colorAccent)

        lineChartTemp.data = LineData(v1, v2)
        lineChartTemp.xAxis.labelRotationAngle = 0f
        lineChartTemp.axisLeft.textColor = Color.WHITE
        lineChartTemp.axisRight.textColor = Color.WHITE
        lineChartTemp.xAxis.textColor = Color.WHITE
        lineChartTemp.legend.isEnabled = false

        val entries3 = ArrayList<Entry>()
        val entries4 = ArrayList<Entry>()

        entries3.add(Entry(1f, 6000f))
        entries3.add(Entry(2f, 6222f))
        entries3.add(Entry(3f, 6333f))
        entries3.add(Entry(4f, 6555f))
        entries3.add(Entry(5f, 6666f))
        entries4.add(Entry(1f, 8000f))
        entries4.add(Entry(2f, 14600f))
        entries4.add(Entry(3f, 12000f))
        entries4.add(Entry(4f, 10000f))
        entries4.add(Entry(5f, 12000f))

        val v3 = LineDataSet(entries3, "Infrared")
        val v4 = LineDataSet(entries4, "Lux")

        v3.setDrawFilled(true)
        v3.lineWidth = 3f
        v3.valueTextColor = ContextCompat.getColor(baseContext, android.R.color.white)
        v3.fillColor = ContextCompat.getColor(baseContext, R.color.roboRed)
        v3.fillAlpha = ContextCompat.getColor(baseContext, R.color.roboRed)
        v3.color = ContextCompat.getColor(baseContext, R.color.roboRed)
        v3.setCircleColor(R.color.roboRed)

        v4.setDrawFilled(true)
        v4.lineWidth = 3f
        v4.valueTextColor = ContextCompat.getColor(baseContext, android.R.color.white)
        v4.fillColor = ContextCompat.getColor(baseContext, R.color.roboYellow)
        v4.fillAlpha = ContextCompat.getColor(baseContext, R.color.roboYellow)
        v4.color = ContextCompat.getColor(baseContext, R.color.roboYellow)
        v4.setCircleColor(R.color.roboYellow)


        // Lazy reversal here to make yellow appear behind red
        lineChartLight.data = LineData(v4, v3)
        lineChartLight.xAxis.labelRotationAngle = 0f
        lineChartLight.axisLeft.textColor = Color.WHITE
        lineChartLight.axisRight.textColor = Color.WHITE
        lineChartLight.xAxis.textColor = Color.WHITE
        lineChartLight.legend.isEnabled = false

        lineChartLight.invalidate()
        lineChartTemp.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
