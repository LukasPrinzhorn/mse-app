package com.example.weihnachtmaerkte.markets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.backend.DataSource
import com.example.weihnachtmaerkte.entities.Market
import kotlinx.android.synthetic.main.fragment_sliding.*

class SlidingFragment : Fragment() {

    private lateinit var previewAdapter: PreviewRecyclerAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sliding, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        val data:List<Market> = DataSource.createMarketsDataSet()
        previewAdapter.submitList(data)
    }

    private fun initRecyclerView() {

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SlidingFragment.activity, LinearLayoutManager.HORIZONTAL,false)
         /*   val topSpacingDecorator =
                    TopSpacingItemDecoration(
                            30
                    )
            addItemDecoration(topSpacingDecorator)*/
            previewAdapter =
                    PreviewRecyclerAdapter()
            adapter = previewAdapter
        }
    }


}
