package com.example.weihnachtmaerkte.markets.previews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import kotlinx.android.synthetic.main.preview_item.view.*


class ListRecyclerAdapter(private var onMarketListener: OnMarketListener, private var ratings: List<Rating>, private var longitude: Double, private var latitude: Double) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var markets: List<Market> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item,
                        parent,
                        false
                ),
                onMarketListener,
                ratings,
                longitude,
                latitude
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
                holder.bind(markets[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return markets.size
    }

    interface OnMarketListener {
        fun onMarketClick(position: Int)
    }

    fun submitList(marketList: List<Market>) {
        markets = marketList
    }

    class ListViewHolder
    constructor(
            itemView: View,
            private var onMarketListener: OnMarketListener,
            private var ratings: List<Rating>,
            private var myLongitude: Double,
            private var myLatitude: Double
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val marketImage = itemView.market_image
        private val marketName = itemView.market_name
        private val marketRating = itemView.market_rating

        fun bind(market: Market) {
            val requestOptions = RequestOptions()
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)

            Glide.with(itemView.context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(market.image)
                    .into(marketImage)

            var marketLongitude: Double = market.coordinates?.get(0)!!
            var marketLatitude: Double = market.coordinates?.get(1)!!
            var dist: Double = distFrom(myLatitude, myLongitude, marketLatitude, marketLongitude)
            var string: String = "" + dist + "|" + market.name
            marketName.text = string
            val ratingIds: ArrayList<String>? = market.ratings
            val results = ArrayList<Rating>()
            if (ratingIds != null) {
                ratings.forEach {
                    if (ratingIds.contains(it.id)) {
                        results.add(it)
                    }
                }
            }
            marketRating.text = calculateAverageRating(results)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onMarketListener.onMarketClick(adapterPosition)
        }

        private fun calculateAverageRating(list: List<Rating>): String {
            var counter = 0
            var result = 0f
            list.forEach {
                result += (it.ambience + it.crowding + it.drinks + it.family + it.food) / 5.0f
                counter++
            }
            return if (counter != 0) {
                result = (result / counter)
                "" + round(result, 1)
            } else {
                "0"
            }
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
