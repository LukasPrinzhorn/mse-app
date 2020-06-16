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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


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
        private val marketDistance = itemView.market_distance

        fun bind(market: Market) {
            val marketLongitude: Double = market.coordinates?.get(0)!!
            val marketLatitude: Double = market.coordinates?.get(1)!!
            val dist: Double = distFrom(myLatitude, myLongitude, marketLatitude, marketLongitude)

            val requestOptions = RequestOptions()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)

            Glide.with(itemView.context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(market.image)
                    .into(marketImage)

            if (market.name.length > 16) {
                val text: String = market.name.substring(0, 16) + "..."
                marketName.text = text
            } else {
                marketName.text = market.name

            }
            val distString = if (dist < 1000) {
                "" + round(dist.toFloat(), 0).toInt() + "m"
            } else {
                "" + round(dist.toFloat() / 1000, 1) + " km"
            }
            marketDistance.text = distString
            val avgRatings = ArrayList<Float>()
            avgRatings.add(market.avgAmbience)
            avgRatings.add(market.avgFood)
            avgRatings.add(market.avgDrinks)
            avgRatings.add(market.avgCrowding)
            avgRatings.add(market.avgFamily)
            marketRating.text = "${market.avgOverall}"
            itemView.setOnClickListener(this)
        }

        private fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
            val earthRadius = 6371000.0 //meters
            val dLat = Math.toRadians(lat2 - lat1)
            val dLng = Math.toRadians(lng2 - lng1)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLng / 2) * sin(dLng / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return (earthRadius * c)
        }

        override fun onClick(v: View?) {
            onMarketListener.onMarketClick(adapterPosition)
        }

        private fun round(number: Float, decimal: Int): Float {
            val numberString: String = "" + number
            val splits: List<String> = numberString.split(".")
            if (splits.size == 1) {
                return number
            }
            return if (splits.size == 2) {
                val s: String = splits[0] + "." + splits[1].substring(0, decimal)
                s.toFloat()
            } else {
                0f
            }
        }
    }
}
