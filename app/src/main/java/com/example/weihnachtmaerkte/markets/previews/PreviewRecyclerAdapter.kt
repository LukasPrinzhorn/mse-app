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


class PreviewRecyclerAdapter(private var onMarketListener: OnMarketListener, private var ratings: List<Rating>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Market> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PreviewViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.preview_item,
                        parent,
                        false
                ),
                onMarketListener,
                ratings
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
            private var ratings: List<Rating>
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val marketImage = itemView.market_image
        private val marketName = itemView.market_name
        private val marketRating = itemView.market_rating

        fun bind(market: Market) {
            if (market.image.startsWith("@")) {
                if (market.image == "@drawable/museumsquartier") {
                    marketImage.setImageResource(R.mipmap.museumsquartier)
                }
                if (market.image == "@drawable/spittelberg") {
                    marketImage.setImageResource(R.mipmap.spittelberg)
                }
                if (market.image == "@drawable/wiener_weihnachtstraum") {
                    marketImage.setImageResource(R.mipmap.wiener_weihnachtstraum)
                }
                if (market.image == "@drawable/zwidemu") {
                    marketImage.setImageResource(R.mipmap.zwidemu)
                }
                if (market.image == "@drawable/karlsplatz") {
                    marketImage.setImageResource(R.mipmap.karlsplatz)
                }
            } else {
                val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)

                Glide.with(itemView.context)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(market.image)
                        .into(marketImage)
            }

            if (market.name.length > 16) {
                val text: String = market.name.substring(0, 16) + "..."
                marketName.text = text
            } else {
                marketName.text = market.name

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

        override fun onClick(v: View?) {
            onMarketListener.onMarketClick(adapterPosition)
        }
    }
}
