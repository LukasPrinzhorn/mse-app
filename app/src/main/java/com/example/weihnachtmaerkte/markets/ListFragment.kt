package com.example.weihnachtmaerkte.markets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.backend.DataSource
import com.example.weihnachtmaerkte.entities.Market
import kotlinx.android.synthetic.main.fragment_preview.*


/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {

    private lateinit var listViewAdapter: ListRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        val data: List<Market> = DataSource.createMarketsDataSet()
        listViewAdapter.submitList(data)
    }

    private fun initRecyclerView() {

        recycler_view.apply {
            layoutManager = GridLayoutManager(this@ListFragment.activity, 2, RecyclerView.VERTICAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            listViewAdapter = ListRecyclerAdapter()
            adapter = listViewAdapter
        }
    }

}
