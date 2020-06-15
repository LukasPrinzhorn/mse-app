package com.example.weihnachtmaerkte.markets.detailedview


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.weihnachtmaerkte.R
import com.example.weihnachtmaerkte.entities.Rating
import kotlinx.android.synthetic.main.comment_item.view.*


class CommentRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var ratings: ArrayList<Rating> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.comment_item,
                        parent,
                        false
                ),
                ratings
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentViewHolder -> {
                holder.bind(ratings[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return ratings.size
    }

    interface OnCommentListener

    fun submitList(ratingList: ArrayList<Rating>) {
        ratings = ratingList
    }

    class CommentViewHolder
    constructor(
            itemView: View,
            private var ratings: ArrayList<Rating>
    ) : RecyclerView.ViewHolder(itemView) {
        private var backgroundColors: ArrayList<Int> = arrayListOf(Color.argb(20,146,0,6), Color.argb(40,146,0,6), Color.argb(60,146,0,6))

        private val commentTitle = itemView.comment_title
        private val commentText = itemView.comment_text
        private val commentAuthor = itemView.comment_username
        private val commentItem = itemView.comment_parent_item

        fun bind(rating: Rating, position: Int) {
            val userNames: ArrayList<String> = ArrayList()
            ratings.forEach{
                userNames.add(it.username)
            }
            if (userNames.contains(rating.username)){
                commentTitle.text = rating.title
                commentText.text = rating.text
                commentAuthor.text = rating.username
                commentItem.setBackgroundColor(backgroundColors[position%3])
            }
        }
    }
}
