package com.example.weihnachtmaerkte.markets.detailedview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.backend.DataSource
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import com.example.weihnachtmaerkte.markets.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_detailed_market_comments.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailedMarketCommentsFragment : Fragment(), CommentRecyclerAdapter.OnCommentListener {
    private lateinit var market: Market
    private lateinit var markets: ArrayList<Market>

    private lateinit var commentViewAdapter: CommentRecyclerAdapter
    private var items: List<Rating> = ArrayList()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_detailed_market_comments, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.comments_go_back).setOnClickListener {
            findNavController().navigate(R.id.action_Third3Fragment_to_First2Fragment)
        }

        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: Long = bundle?.get("id") as Long
        markets = bundle.getParcelableArrayList<Market>("markets") as ArrayList<Market>


//        val markets: List<Market> = com.example.weihnachtmaerkte.backend.DataSource.createMarketsDataSet()
        markets.forEach {
            if (it.id == id) {
                market = it
            }
        }
        setData()
        initRecyclerView()
        addDataSet()
    }

    override fun onCommentClick(position: Int) {
    }

    private fun addDataSet() {
        var itemIds:ArrayList<Long> = market.ratings!!
        var ratings:List<Rating> = DataSource.createRatingDataSet()
        var results = ArrayList<Rating>()
        ratings.forEach{
            if (itemIds.contains(it.id)){
                results.add(it)
            }
        }
        commentViewAdapter.submitList(results)
    }

    private fun initRecyclerView() {

        recycler_view_comment.apply {
            layoutManager = LinearLayoutManager(this@DetailedMarketCommentsFragment.activity, RecyclerView.VERTICAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            commentViewAdapter = CommentRecyclerAdapter(this@DetailedMarketCommentsFragment)
            adapter = commentViewAdapter
        }
    }

    private fun setData() {
        val textView: TextView = view?.findViewById(R.id.detailed_market_name) as TextView
        textView.text = market.name


        val imageView: ImageView = view?.findViewById(R.id.detailed_market_image) as ImageView

        if (market.image.startsWith("@")) {
            if (market.image == "@drawable/museumsquartier") {
                imageView.setImageResource(R.mipmap.museumsquartier)
            }
            if (market.image == "@drawable/spittelberg") {
                imageView.setImageResource(R.mipmap.spittelberg)
            }
            if (market.image == "@drawable/wiener_weihnachtstraum") {
                imageView.setImageResource(R.mipmap.wiener_weihnachtstraum)
            }
            if (market.image == "@drawable/zwidemu") {
                imageView.setImageResource(R.mipmap.zwidemu)
            }
            if (market.image == "@drawable/karlsplatz") {
                imageView.setImageResource(R.mipmap.karlsplatz)
            }
        }/* else {
            //load real picture
        }*/
    }
}
