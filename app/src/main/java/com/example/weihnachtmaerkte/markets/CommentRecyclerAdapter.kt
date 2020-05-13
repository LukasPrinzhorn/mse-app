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

        private val commentTitle = itemView.comment_title
        private val commentText = itemView.comment_text


        fun bind(rating: Rating) {
            commentTitle.text = rating.title
            commentText.text = rating.text

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onCommentListener.onCommentClick(adapterPosition)
        }

    }
}
