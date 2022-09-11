package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AsteroidAdapter : ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = getItem(position)
        val res = holder.itemView.context.resources
        holder.bind(asteroid)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val codename: TextView = itemView.findViewById(R.id.codename_text)
        val closeApproachDate: TextView = itemView.findViewById(R.id.close_approach_date_text)
        val isPotentiallyHazardous: ImageView =
            itemView.findViewById(R.id.is_potentially_hazardous_image)

        fun bind(asteroid: Asteroid) {
            codename.text = asteroid.codename
            closeApproachDate.text = asteroid.closeApproachDate
            isPotentiallyHazardous.setImageResource(
                when (true) {
                    false -> R.drawable.ic_status_normal
                    true -> R.drawable.ic_status_potentially_hazardous
                })
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_asteroid, parent, false) as View
                return ViewHolder(view)
            }
        }
    }

 class AsteroidDiffCallback:DiffUtil.ItemCallback<Asteroid>(){
     override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
      return  oldItem.id==newItem.id
     }

     override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem==newItem
     }
 }
}