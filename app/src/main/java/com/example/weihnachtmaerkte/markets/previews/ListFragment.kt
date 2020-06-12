package com.example.weihnachtmaerkte.markets.previews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import com.example.weihnachtmaerkte.markets.TopSpacingItemDecoration
import com.example.weihnachtmaerkte.markets.detailedview.DetailedMarketActivity
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_preview.*
import java.util.*
import kotlin.collections.ArrayList


class ListFragment : Fragment(), ListRecyclerAdapter.OnMarketListener {

    private lateinit var listViewAdapter: ListRecyclerAdapter
    private var markets: ArrayList<Market> = ArrayList()
    private var ratings: ArrayList<Rating> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(marketsBundle: ArrayList<Market>, ratingsBundle: ArrayList<Rating>) = ListFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("markets", marketsBundle)
                putParcelableArrayList("ratings", ratingsBundle)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelableArrayList<Market>("markets")?.let {
            markets = it
        }
        arguments?.getParcelableArrayList<Rating>("ratings")?.let {
            ratings = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        addDataSet()
    }

    override fun onMarketClick(position: Int) {
        val intent = Intent(this@ListFragment.activity, DetailedMarketActivity::class.java)
        val bundle = Bundle()
        bundle.putString("id", markets[position].id)
        bundle.putParcelableArrayList("markets", markets)
        bundle.putParcelableArrayList("ratings", ratings)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun addDataSet() {
        listViewAdapter.submitList(markets)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = GridLayoutManager(this@ListFragment.activity, 2, RecyclerView.VERTICAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            listViewAdapter = ListRecyclerAdapter(this@ListFragment, ratings)
            adapter = listViewAdapter
        }
    }

    fun reorderMarketsByPosition(referencePosition: LatLng) {
        if (markets != null && markets.size != 0) {
            Collections.sort(markets, PositionComparator(referencePosition))
            /*for (Market m : markets) {
                Log.i("Markets", m.getName());
            }*/
        }
    }

    private class PositionComparator internal constructor(private val referencePosition: LatLng) : Comparator<Market?> {
        override fun compare(o1: Market?, o2: Market?): Int {
            assert(o1 != null && o2 != null)
            val distToMarket1 = Math.hypot(referencePosition.longitude - o1!!.coordinates!![1], referencePosition.latitude - o1.coordinates!![0])
            val distToMarket2 = Math.hypot(referencePosition.longitude - o2!!.coordinates!![1], referencePosition.latitude - o2.coordinates!![0])
            //Log.i("Distance", o1.getName() + ": " + distToMarket1);
            //Log.i("Distance", o2.getName() + ": " + distToMarket2);
            return java.lang.Double.compare(distToMarket1, distToMarket2)
        }

    }

}
