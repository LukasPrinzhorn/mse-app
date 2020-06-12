package com.example.weihnachtmaerkte.markets.previews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import com.example.weihnachtmaerkte.markets.TopSpacingItemDecoration
import com.example.weihnachtmaerkte.markets.detailedview.DetailedMarketActivity
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_preview.*


class PreviewFragment : Fragment(), PreviewRecyclerAdapter.OnMarketListener {

    private lateinit var previewAdapter: PreviewRecyclerAdapter
    private var markets: ArrayList<Market> = ArrayList()
    private var ratings: ArrayList<Rating> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(marketsBundle: ArrayList<Market>, ratingsBundle: ArrayList<Rating>) = PreviewFragment().apply {
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


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_preview, container, false)
        val arrow: View = view.findViewById<ImageView>(R.id.arrow_Up)
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

    override fun onMarketClick(position: Int) {
        val intent = Intent(this@PreviewFragment.activity, DetailedMarketActivity::class.java)
        val bundle = Bundle()
        bundle.putString("id", markets[position].id)
        bundle.putParcelableArrayList("markets", markets)
        bundle.putParcelableArrayList("ratings", ratings)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun addDataSet() {
        previewAdapter.submitList(markets)
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@PreviewFragment.activity, LinearLayoutManager.HORIZONTAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            previewAdapter = PreviewRecyclerAdapter(this@PreviewFragment, ratings)
            adapter = previewAdapter
        }
    }
}
