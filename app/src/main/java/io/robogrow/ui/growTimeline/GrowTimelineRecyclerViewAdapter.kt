package io.robogrow.ui.growTimeline

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.vipulasri.timelineview.TimelineView
import io.robogrow.R

import io.robogrow.ui.growTimeline.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GrowTimelineRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<GrowTimelineRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_growtimeline_item, parent, false)
        return ViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.content
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View, viewType : Int) : RecyclerView.ViewHolder(view) {
        val mTimelineView: TimelineView = view.findViewById(R.id.timeline)
        val contentView: TextView = view.findViewById(R.id.content)

        init {
            mTimelineView.initLine(viewType)
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}