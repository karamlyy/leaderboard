package com.karamlyy.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class RatingsAdapter(
    private var ratings: List<Rating> = mutableListOf()
): RecyclerView.Adapter<RatingsAdapter.ViewHolder>() {
    private val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm")
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val adminView = itemView.findViewById<TextView>(R.id.adminUsername)
        val ratingAmountView = itemView.findViewById<TextView>(R.id.ratingAmount)
        val commentView = itemView.findViewById<TextView>(R.id.adminComment)
        val ratingDateView = itemView.findViewById<TextView>(R.id.ratingDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rating_layout,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = ratings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRating = ratings[position]
        holder.adminView.text = currentRating.admin
        holder.ratingAmountView.text = "${currentRating.point}"
        holder.commentView.text = currentRating.comment
        holder.ratingDateView.text = currentRating.createdAt.format(formatter)
    }

    fun setRatings(ratings: List<Rating>){
        this.ratings = ratings
        this.notifyDataSetChanged()

    }
}