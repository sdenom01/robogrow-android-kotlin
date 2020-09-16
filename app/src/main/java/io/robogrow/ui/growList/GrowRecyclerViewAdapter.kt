package io.robogrow.ui.growList

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.robogrow.R
import io.robogrow.classes.Grow


import io.robogrow.ui.growList.GrowListFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.row_grow.view.*

/**
 * [RecyclerView.Adapter] that can display a [Grow] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class GrowRecyclerViewAdapter(
    private val mValues: ArrayList<Grow?>?,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<GrowRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    private var everyOther = 0

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Grow
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_grow, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues?.get(position)
        holder.tvName.text = item?.name

        if (item?.config != null) {
            holder.tvTempMin.text = item?.config?.tempLow.toString() + "°"
            holder.tvTempMax.text = item?.config?.tempHigh.toString() + "°"
            holder.pbTemp.max = item?.config?.tempHigh?.toInt()
            holder.pbTemp.min = item?.config?.tempLow?.toInt()
            holder.pbTemp.progress = item?.current?.temp?.toInt()

            holder.tvHumidityMin.text = item?.config?.humidityLow.toString() + "%"
            holder.tvHumidityMax.text = item?.config?.humidityHigh.toString() + "%"
            holder.pbHumidity.max = item?.config?.humidityHigh?.toInt()
            holder.pbHumidity.min = item?.config?.humidityLow?.toInt()
            holder.pbHumidity.progress = item?.current?.humidity?.toInt()
        }


//        holder.mContentView.text = item.content

        if (everyOther % 2 == 0) {
            holder.mWrapper.setBackgroundColor(
                ContextCompat.getColor(
                    holder.mWrapper.context,
                    R.color.colorDisabledOther
                )
            )
        }

        everyOther++

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mWrapper: LinearLayout = mView.ll_row_wrapper
        val tvName: TextView = mView.tv_grow_name
        val tvTempMin: TextView = mView.tv_temp_min
        val tvTempMax: TextView = mView.tv_temp_max
        val pbTemp: ProgressBar = mView.pb_temp
        val tvHumidityMin: TextView = mView.tv_humidity_min
        val tvHumidityMax: TextView = mView.tv_humidity_max
        val pbHumidity: ProgressBar = mView.pb_humidity

        override fun toString(): String {
            return ""
//            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
