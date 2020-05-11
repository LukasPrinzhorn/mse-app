package com.example.weihnachtmaerkte.markets


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


class ListRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Market> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PreviewViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item,
                        parent,
                        false
                )
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

    class PreviewViewHolder
    constructor(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val marketImage = itemView.market_image
        private val marketName = itemView.market_name
        private val marketRating = itemView.market_rating

        fun bind(market: Market) {
            //only for testing purpose
            if (market.image.startsWith("@")) {
                if (market.image == "@drawable/museumsquartier") {
                    marketImage.setImageResource(R.drawable.museumsquartier)
                }
                if (market.image == "@drawable/spittelberg") {
                    marketImage.setImageResource(R.drawable.spittelberg)
                }
                if (market.image == "@drawable/wiener_weihnachtstraum") {
                    marketImage.setImageResource(R.drawable.wiener_weihnachtstraum)
                }
                if (market.image == "@drawable/zwidemu") {
                    marketImage.setImageResource(R.drawable.zwidemu)
                }
                if (market.image == "@drawable/karlsplatz") {
                    marketImage.setImageResource(R.drawable.karlsplatz)
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
            marketName.text = market.name
            marketRating.text = calculateAverageRating(market.ratings)

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

        private fun round(number:Float, decimal:Int):Float{
            val numberString: String = "" + number
            val splits : List<String> = numberString.split(".")
            if (splits.size == 1){
                return number
            }
            return if (splits.size == 2){
                val s: String = splits[0] + "." + splits[1].substring(0, decimal)
                s.toFloat()
            } else {
                0f
            }
        }
    }
}
