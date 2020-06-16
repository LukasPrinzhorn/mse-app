package com.example.weihnachtmaerkte.markets.previews


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import kotlinx.android.synthetic.main.preview_item.view.*


class PreviewRecyclerAdapter(private var onMarketListener: OnMarketListener, private var longitude: Double, private var latitude: Double) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Market> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PreviewViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.preview_item,
                        parent,
                        false
                ),
                onMarketListener,
                longitude,
                latitude
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreviewViewHolder -> {
                holder.bind(items[position])
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(marketList: List<Market>) {
        items = marketList
    }

    interface OnMarketListener {
        fun onMarketClick(position: Int)
    }

    class PreviewViewHolder
    constructor(
            itemView: View,
            private var onMarketListener: OnMarketListener,
            private var myLongitude: Double,
            private var myLatitude: Double
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val marketImage = itemView.market_image
        private val marketName = itemView.market_name
        private val marketRating = itemView.market_rating

        fun bind(market: Market) {
            var marketLongitude: Double = market.coordinates?.get(0)!!
            var marketLatitude: Double = market.coordinates?.get(1)!!
            var dist: Double = distFrom(myLatitude, myLongitude, marketLatitude, marketLongitude)
            var string: String = "" + dist + "|" + market.name

            val requestOptions = RequestOptions()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)

            Glide.with(itemView.context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(market.image)
                    .into(marketImage)

            if (market.name.length > 16) {
                val text: String = market.name.substring(0, 16) + "..."
                marketName.text = string
            } else {
                marketName.text = string

            }
            val avgRatings = ArrayList<Float>()
            avgRatings.add(market.avgAmbience)
            avgRatings.add(market.avgFood)
            avgRatings.add(market.avgDrinks)
            avgRatings.add(market.avgCrowding)
            avgRatings.add(market.avgFamily)
            marketRating.text = "${market.avgOverall}"
            itemView.setOnClickListener(this)
        }

        fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
            val earthRadius = 6371000.0 //meters
            val dLat = Math.toRadians(lat2 - lat1.toDouble())
            val dLng = Math.toRadians(lng2 - lng1.toDouble())
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1.toDouble())) * Math.cos(Math.toRadians(lat2.toDouble())) *
                    Math.sin(dLng / 2) * Math.sin(dLng / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            return (earthRadius * c).toDouble()
        }

        override fun onClick(v: View?) {
            onMarketListener.onMarketClick(adapterPosition)
        }
    }
}
