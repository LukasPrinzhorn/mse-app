package com.example.weihnachtmaerkte.markets.detailedview

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market


class DetailedMarketFragment : Fragment() {
    private lateinit var market: Market
    private lateinit var markets: ArrayList<Market>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = activity?.intent?.getBundleExtra("bundle")
        val id: String = bundle?.get("id") as String
        markets = bundle.getParcelableArrayList<Market>("markets") as ArrayList<Market>
        markets.forEach {
            if (it.id == id) {
                market = it
            }
        }

        view.findViewById<ImageView>(R.id.fab_detailed_edit).setOnClickListener {
            findNavController().navigate(R.id.action_OverviewFragment_to_RateFragment)
        }
        view.findViewById<TextView>(R.id.button_more_comments).setOnClickListener {
            findNavController().navigate(R.id.action_RateFragment_to_CommentsFragment)
        }
        view.findViewById<RatingBar>(R.id.ratingBarIndicatorAmbience).rating = market.avgAmbience
        view.findViewById<RatingBar>(R.id.ratingBarIndicatorFood).rating = market.avgFood
        view.findViewById<RatingBar>(R.id.ratingBarIndicatorDrinks).rating = market.avgDrinks
        view.findViewById<RatingBar>(R.id.ratingBarIndicatorCrowding).rating = market.avgCrowding
        view.findViewById<RatingBar>(R.id.ratingBarIndicatorFamily).rating = market.avgFamily

        view.findViewById<ImageView>(R.id.detailed_go_back).setOnClickListener {
            activity?.finish()
        }
        setData()
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
        val requestOptions = RequestOptions()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)

        Glide.with(imageView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(market.image)
                .into(imageView)
    }
}
