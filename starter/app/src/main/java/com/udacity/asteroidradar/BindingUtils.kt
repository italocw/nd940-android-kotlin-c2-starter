package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("isPotentiallyHazardousImage")
fun ImageView.setIsPotentiallyHazardousImage(asteroid: Asteroid?) {
    asteroid?.let {
        setImageResource(
            when (asteroid.isPotentiallyHazardous) {
                false -> R.drawable.ic_status_normal
                true -> R.drawable.ic_status_potentially_hazardous
            }
        )
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