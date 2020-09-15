package io.robogrow.ui.growView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.robogrow.R
import io.robogrow.classes.Grow
import io.robogrow.classes.GrowEvent
import io.robogrow.ui.growList.GrowListFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.row_grow_event.view.*
import java.lang.Math.round

/**
 * [RecyclerView.Adapter] that can display a [Grow] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class GrowEventRecyclerViewAdapter(
    private val mValues: List<GrowEvent>
) : RecyclerView.Adapter<GrowEventRecyclerViewAdapter.ViewHolder>() {

    private var everyOther = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_grow_event, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.tvDate.text = item.createDate.toString()
        holder.tvTemp.text = item.temp.toString() + "°"
        holder.tvHumidity.text = item.humidity.toString() + "%"
        holder.tvLux.text = round(item.lux / 1000).toString() + "k"
        holder.tvInfrared.text = round(item.infrared / 1000).toString() + "k"
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val tvDate: TextView = mView.tv_date
        val tvTemp: TextView = mView.tv_temp
        val tvHumidity: TextView = mView.tv_humidity
        val tvLux: TextView = mView.tv_lux
        val tvInfrared: TextView = mView.tv_infrared
    }
}
