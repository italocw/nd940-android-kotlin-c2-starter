package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AsteroidAdapter : RecyclerView.Adapter<AsteroidAdapter.ViewHolder>() {
    var data = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = data[position]
        val res = holder.itemView.context.resources
        holder.bind(asteroid)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_asteroid, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 1//data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    }
}