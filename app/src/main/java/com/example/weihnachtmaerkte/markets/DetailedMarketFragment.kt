package com.example.weihnachtmaerkte.markets

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import kotlinx.android.synthetic.main.fragment_detailed_market.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailedMarketFragment : Fragment(), CommentRecyclerAdapter.OnCommentListener {
    private lateinit var market: Market
    private lateinit var commentViewAdapter: CommentRecyclerAdapter
    private var items: List<Rating> = ArrayList()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_detailed_market, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.fab_detailed_edit).setOnClickListener {
            findNavController().navigate(R.id.action_First2Fragment_to_Second2Fragment)
        }

        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: Long = bundle?.get("id") as Long


        val markets: List<Market> = com.example.weihnachtmaerkte.backend.DataSource.createMarketsDataSet()
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
        Toast.makeText(this@DetailedMarketFragment.activity, "hiiii", Toast.LENGTH_SHORT).show()
    }

    private fun addDataSet() {
        items = market.ratings
        commentViewAdapter.submitList(items)
    }

    private fun initRecyclerView() {

        recycler_view_comment.apply {
            layoutManager = LinearLayoutManager(this@DetailedMarketFragment.activity, RecyclerView.VERTICAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            commentViewAdapter = CommentRecyclerAdapter(this@DetailedMarketFragment)
            adapter = commentViewAdapter
        }
    }

    private fun setData() {
        var textView: TextView = view?.findViewById(R.id.detailed_market_name) as TextView
        textView.text = market.name
        textView = view?.findViewById(R.id.detailed_market_address) as TextView
        textView.text = market.address
        textView = view?.findViewById(R.id.detailed_market_date) as TextView
        textView.text = market.dates
        textView = view?.findViewById(R.id.detailed_market_time) as TextView
        textView.text = market.openingHours
        textView = view?.findViewById(R.id.detailed_market_homepage) as TextView
        textView.text = market.weblink
        textView.movementMethod = LinkMovementMethod.getInstance()

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
