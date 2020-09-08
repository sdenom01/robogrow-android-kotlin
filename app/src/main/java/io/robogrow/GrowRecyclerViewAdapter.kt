package io.robogrow

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat


import io.robogrow.GrowListFragment.OnListFragmentInteractionListener
import io.robogrow.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_grow_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class GrowRecyclerViewAdapter(
    private val mValues: List<DummyItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<GrowRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    private var everyOther = 0

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_grow_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = "Grow-" + item.id
//        holder.mContentView.text = item.content

        if (everyOther % 2 == 0) {
            holder.mWrapper.setBackgroundColor(ContextCompat.getColor(holder.mWrapper.context, R.color.colorDisabledOther))
        }

        everyOther++

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mWrapper: LinearLayout = mView.ll_row_wrapper
        val mIdView: TextView = mView.tv_grow_name
//        val mContentView: TextView = mView.content

        override fun toString(): String {
            return ""
//            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
