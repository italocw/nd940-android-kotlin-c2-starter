package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter : ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = getItem(position)
        val res = holder.itemView.context.resources
        holder.bind(asteroid)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.codenameText.text = asteroid.codename
            binding.closeApproachDateText.text = asteroid.closeApproachDate
            binding.isPotentiallyHazardousImage.setImageResource(
                when (true) {
                    false -> R.drawable.ic_status_normal
                    true -> R.drawable.ic_status_potentially_hazardous
                }
            )
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false) as View
                return ViewHolder(binding)
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