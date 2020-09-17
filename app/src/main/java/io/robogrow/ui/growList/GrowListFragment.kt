package io.robogrow.ui.growList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import io.robogrow.R
import io.robogrow.RobogrowApplication
import io.robogrow.classes.Grow
import io.robogrow.requests.grows.GetGrowAllGrowsForUserId

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [GrowListFragment.OnListFragmentInteractionListener] interface.
 */
class GrowListFragment : Fragment() {
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grow_list, container, false)

        val request = GetGrowAllGrowsForUserId(
            context!!,
            Response.Listener {
                if (view is RecyclerView) {
                    with(view) {
                        layoutManager = LinearLayoutManager(context)
                        adapter = GrowRecyclerViewAdapter(
                            it,
                            listener
                        )
                    }
                }
            })

        RobogrowApplication.queue.addToRequestQueue(request)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Grow?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            GrowListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
