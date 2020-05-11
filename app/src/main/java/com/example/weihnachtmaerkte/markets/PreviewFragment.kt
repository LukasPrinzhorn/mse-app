package com.example.weihnachtmaerkte.markets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.backend.DataSource
import com.example.weihnachtmaerkte.entities.Market
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_preview.*


class PreviewFragment : Fragment(), PreviewRecyclerAdapter.OnMarketListener{

    private lateinit var previewAdapter: PreviewRecyclerAdapter
    private var items: List<Market> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_preview, container, false)
        var arrow: View = view.findViewById<ImageView>(R.id.arrowUp)
        arrow.setOnClickListener {
            val layout: SlidingUpPanelLayout? = activity?.findViewById(R.id.slider)
            if (layout != null) {
                layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        items = DataSource.createMarketsDataSet()
        previewAdapter.submitList(items)
    }

    private fun initRecyclerView() {

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@PreviewFragment.activity, LinearLayoutManager.HORIZONTAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            previewAdapter = PreviewRecyclerAdapter(this@PreviewFragment)
            adapter = previewAdapter
        }
    }

    override fun onMarketClick(position: Int) {
        var intent : Intent = Intent(this@PreviewFragment.activity, DetailedMarketActivity::class.java)
        var bundle : Bundle = Bundle()
        bundle.putLong("id", items[position].id)
        intent.putExtra("bundle",bundle)
        startActivity(intent)
    }


}
