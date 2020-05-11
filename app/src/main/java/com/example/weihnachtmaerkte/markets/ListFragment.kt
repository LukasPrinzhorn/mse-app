package com.example.weihnachtmaerkte.markets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.backend.DataSource
import com.example.weihnachtmaerkte.entities.Market
import kotlinx.android.synthetic.main.fragment_preview.*



class ListFragment : Fragment(), ListRecyclerAdapter.OnMarketListener {

    private lateinit var listViewAdapter: ListRecyclerAdapter
    private var items: List<Market> = ArrayList()


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
        bundle.putLong("id", items[position].id)
        intent.putExtra("bundle",bundle)
        startActivity(intent)
    }

    private fun addDataSet() {
        items = DataSource.createMarketsDataSet()
        listViewAdapter.submitList(items)
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
