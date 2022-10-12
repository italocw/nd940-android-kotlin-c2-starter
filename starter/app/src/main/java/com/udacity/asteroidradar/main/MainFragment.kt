package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private lateinit var asteroidListAdapter: AsteroidAdapter
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.viewModel = viewModel

        AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        }).apply { asteroidListAdapter = this }

        binding.asteroidRecycler.adapter = asteroidListAdapter
        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidNavigated()
            }
        })

        updateDisplayedAsteroids(viewModel.asteroids)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                applyAsteroidFilter(menuItem)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun applyAsteroidFilter(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.show_weeks_asteroids_menu -> updateDisplayedAsteroids(viewModel.nextSevenDaysAsteroids)
            R.id.show_todays_asteroids_menu -> updateDisplayedAsteroids(viewModel.todaysAsteroids)
            R.id.show_saved_asteroids_menu -> updateDisplayedAsteroids(viewModel.asteroids)
        }
    }
    private fun updateDisplayedAsteroids(asteroids: LiveData<List<Asteroid>>) {

        asteroids.observe(viewLifecycleOwner) {
            it?.let {
                asteroidListAdapter.submitList(it)
            }
        }
    }
}
