package com.example.weihnachtmaerkte.markets.detailedview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import com.example.weihnachtmaerkte.markets.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_detailed_market_comments.*


class DetailedMarketCommentsFragment : Fragment(), CommentRecyclerAdapter.OnCommentListener {
    private lateinit var market: Market
    private lateinit var markets: ArrayList<Market>
    private lateinit var ratings: ArrayList<Rating>
    private lateinit var commentViewAdapter: CommentRecyclerAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_market_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.comments_go_back).setOnClickListener {
            findNavController().navigate(R.id.action_CommentsFragment_to_OverviewFragment)
        }

        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: String = bundle?.get("id") as String
        markets = bundle.getParcelableArrayList<Market>("markets") as ArrayList<Market>
        ratings = bundle.getParcelableArrayList<Rating>("ratings") as ArrayList<Rating>
        markets.forEach {
            if (it.id == id) {
                market = it
            }
        }
        setData()
        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet() {
        val itemIds: ArrayList<String>? = market.ratings
        val results = ArrayList<Rating>()
        ratings.forEach {
            if (itemIds != null && itemIds.contains(it.id) && it.text != null && !it.text.equals("") && it.title != null && !it.title.equals("")) {
                results.add(it)
            }
        }
        commentViewAdapter.submitList(results)
        Log.d("testingAdapterSize",""+results)

    }

    private fun initRecyclerView() {
        recycler_view_comment.apply {
            layoutManager = LinearLayoutManager(this@DetailedMarketCommentsFragment.activity, RecyclerView.VERTICAL, false)
            val topSpacingDecorator = TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingDecorator)
            commentViewAdapter = CommentRecyclerAdapter()
            adapter = commentViewAdapter
        }
    }

    private fun setData() {
        val textView: TextView = view?.findViewById(R.id.detailed_market_name) as TextView
        textView.text = market.name

        val imageView: ImageView = view?.findViewById(R.id.detailed_market_image) as ImageView
        val requestOptions = RequestOptions()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)

        Glide.with(imageView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(market.image)
                .into(imageView)
    }
}
