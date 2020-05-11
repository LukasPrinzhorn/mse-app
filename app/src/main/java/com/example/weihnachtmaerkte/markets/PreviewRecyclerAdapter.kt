package com.example.weihnachtmaerkte.markets


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.preview_item.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Market
import com.example.weihnachtmaerkte.entities.Rating
import kotlin.collections.ArrayList


class PreviewRecyclerAdapter(onMarketListener: OnMarketListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Market> = ArrayList()
    private var onMarketListener: OnMarketListener = onMarketListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PreviewViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.preview_item,
                        parent,
                        false
                ),
                onMarketListener
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

    public interface OnMarketListener {
        fun onMarketClick(position: Int)
    }

    class PreviewViewHolder
    constructor(
            itemView: View,
            onMarketListener: OnMarketListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val market_image = itemView.market_image
        val market_name = itemView.market_name
        val market_rating = itemView.market_rating
        private var onMarketListener: OnMarketListener = onMarketListener

        fun bind(market: Market) {
            //only for testing purpose
            if (market.image.startsWith("@")) {
                if (market.image == "@drawable/museumsquartier") {
                    market_image.setImageResource(R.drawable.museumsquartier)
                }
                if (market.image == "@drawable/spittelberg") {
                    market_image.setImageResource(R.drawable.spittelberg)
                }
                if (market.image == "@drawable/wiener_weihnachtstraum") {
                    market_image.setImageResource(R.drawable.wiener_weihnachtstraum)
                }
                if (market.image == "@drawable/zwidemu") {
                    market_image.setImageResource(R.drawable.zwidemu)
                }
                if (market.image == "@drawable/karlsplatz") {
                    market_image.setImageResource(R.drawable.karlsplatz)
                }
            } else {
                val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                Glide.with(itemView.context)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(market.image)
                        .into(market_image)
            }

            if (market.name.length > 17) {
                var text: String = market.name.substring(0, 17) + "...";
                market_name.setText(text)
            } else {
                market_name.setText(market.name)

            }
            market_rating.setText(calculateAverageRating(market.ratings))
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onMarketListener.onMarketClick(adapterPosition)
        }

        private fun calculateAverageRating(list: List<Rating>): String {
            var counter: Int = 0
            var result: Float = 0f
            list.forEach {
                result += (it.ambience + it.crowding + it.drinks + it.family + it.food) / 5.0f
                counter++
            }
            if (counter != 0) {
                result = (result / counter)
                return "" + round(result, 1)
            } else {
                return "0"
            }
        }

        private fun round(number: Float, decimal: Int): Float {
            var numberString: String = "" + number
            var splits: List<String> = numberString.split(".");
            if (splits.size == 1) {
                return number
            }
            if (splits.size == 2) {
                var s: String = splits.get(0) + "." + splits.get(1).substring(0, decimal)
                return s.toFloat()
            } else {
                return 0f
            }
        }


    }
}
