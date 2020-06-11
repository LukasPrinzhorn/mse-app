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
import kotlinx.android.synthetic.main.fragment_preview.*


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
        bundle.putLong("id", markets[position].id)
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
}
