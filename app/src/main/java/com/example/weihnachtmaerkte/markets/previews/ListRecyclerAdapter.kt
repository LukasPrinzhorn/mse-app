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


class ListRecyclerAdapter(private var onMarketListener: OnMarketListener, private var ratings: List<Rating>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var markets: List<Market> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item,
                        parent,
                        false
                ),
                onMarketListener,
                ratings
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
            private var ratings: List<Rating>
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

            marketName.text = market.name
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
