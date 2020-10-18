package com.example.madlevel4task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_game.view.*


class GameAdapter(private val game: List<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(game[position])
    }

    override fun getItemCount(): Int {
        return game.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // id corresponding to choice made
        private val imageId = arrayListOf(R.drawable.rock, R.drawable.paper, R.drawable.scissors)

        fun databind(game: Game) {
            when (game.result) {
                WIN -> itemView.textResult.text = itemView.context.getString(R.string.win)
                DRAW -> itemView.textResult.text = itemView.context.getString(R.string.draw)
                LOSE -> itemView.textResult.text = itemView.context.getString(R.string.lose)
            }

            itemView.textDate.text = game.date.toString()
            itemView.imageResultPlayer.setBackgroundResource(imageId[game.playerChoice.ordinal])
            itemView.imageResultComputer.setBackgroundResource(imageId[game.computerChoice.ordinal])
        }
    }
}