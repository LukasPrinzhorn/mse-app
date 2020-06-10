package com.example.weihnachtmaerkte.markets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import kotlinx.android.synthetic.main.fragment_preview.*



class ListFragment : Fragment(), ListRecyclerAdapter.OnMarketListener {

    private lateinit var listViewAdapter: ListRecyclerAdapter
    private var markets: ArrayList<Market> = ArrayList()
    private var markets2: List<Market> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(marketsBundle: ArrayList<Market>) = ListFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("markets", marketsBundle)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelableArrayList<Market>("markets")?.let {
            markets = it
            Log.d("Marketgröße-list", "" + it.size)

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
        intent.putExtra("bundle",bundle)
        startActivity(intent)
    }

    private fun addDataSet() {
        //markets2 = DataSource.createMarketsDataSet()
        listViewAdapter.submitList(markets)
    }

    private fun initRecyclerView() {

        recycler_view.apply {
            layoutManager = GridLayoutManager(this@ListFragment.activity, 2, RecyclerView.VERTICAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            listViewAdapter = ListRecyclerAdapter(this@ListFragment)
            adapter = listViewAdapter
        }
    }


}
