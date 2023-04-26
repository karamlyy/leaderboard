package com.karamlyy.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaderboardAdapter(
    private var leaderboardMembers : List<LeaderboardMember> = mutableListOf()
) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val rankView = itemView.findViewById<TextView>(R.id.orderByRank)
        val studentUsername = itemView.findViewById<TextView>(R.id.studentUsername)
        val pointView = itemView.findViewById<TextView>(R.id.studentTotalPoint)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.leaderboard_layout,
            parent,
            false
        )
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int = leaderboardMembers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMember = leaderboardMembers[position]

        holder.rankView.text = "${position + 1}"
        holder.studentUsername.text = currentMember.username
        holder.pointView.text = "${currentMember.point}"
    }

    fun setLeaderboardMembers(members: List<LeaderboardMember>){
        this.leaderboardMembers = members
        this.notifyDataSetChanged()
    }
}