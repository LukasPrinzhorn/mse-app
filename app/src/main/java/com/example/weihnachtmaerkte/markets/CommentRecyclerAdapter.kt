package com.example.weihnachtmaerkte.markets


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Rating
import kotlinx.android.synthetic.main.comment_item.view.*


class CommentRecyclerAdapter(private var onCommentListener: OnCommentListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Rating> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.comment_item,
                        parent,
                        false
                ),
                onCommentListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnCommentListener {
        fun onCommentClick(position: Int)
    }

    fun submitList(ratingList: List<Rating>) {
        items = ratingList
    }

    class CommentViewHolder
    constructor(
            itemView: View,
            private var onCommentListener: OnCommentListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val commentTitle = itemView.comment_title;
        private val commentText = itemView.comment_text;


        fun bind(rating: Rating) {
            commentTitle.text = rating.title
            commentText.text = rating.text

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onCommentListener.onCommentClick(adapterPosition)
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
