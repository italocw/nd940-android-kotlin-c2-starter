package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("isPotentiallyHazardousImage")
fun ImageView.setIsPotentiallyHazardousImage(asteroid: Asteroid?) {
    asteroid?.let {
        fun setStatusNormalResources() {
            setImageResource(R.drawable.ic_status_normal)
            contentDescription = resources.getString(R.string.not_hazardous_asteroid_icon)
        }

        fun setPotentiallyHazardousResources() {
            setImageResource(R.drawable.ic_status_potentially_hazardous)
            contentDescription = resources.getString(R.string.potentially_hazardous_asteroid_icon)
        }

        when (asteroid.isPotentiallyHazardous) {
            false -> setStatusNormalResources()
            true -> setPotentiallyHazardousResources()
        }
    }
}


@BindingAdapter("codename")
fun TextView.setCodename(asteroid: Asteroid?) {
    asteroid?.let {
        text = asteroid.codename
    }
}

@BindingAdapter("closeApproachDate")
fun TextView.setCloseApproachDate(asteroid: Asteroid?) {
    asteroid?.let {
        text = asteroid.closeApproachDate
    }
}