package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.domains.AsteroidApiStatus
import com.udacity.asteroidradar.data.local.models.Asteroid
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.ui.main.adapter.AsteroidListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) { "You can only access the viewModel after onViewCreated()" }
        ViewModelProvider(this, MainViewModelFactory(activity.application))[MainViewModel::class.java]
    }

    private val asteroidListAdapter by lazy { AsteroidListAdapter { onItemClicked(it) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        addMenu()
        initRecyclerView(binding)
        listenToAsteroidListFlowEvent(binding)
        return binding.root
    }

    private fun initRecyclerView(binding: FragmentMainBinding) {
        binding.asteroidRecycler.apply {
            adapter = asteroidListAdapter
        }
    }

    private fun onItemClicked(selectedAsteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(selectedAsteroid))
    }

    /**
     * Listen to the asteroid list flow event.
     */
    private fun listenToAsteroidListFlowEvent(binding: FragmentMainBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.loadingAsteroidState.collectLatest {
                    when (it) {
                        AsteroidApiStatus.LOADING -> {
                            binding.noDataFoundTv.isVisible = false
                            binding.statusLoadingWheel.isVisible = true
                        }
                        AsteroidApiStatus.DONE -> {
                            binding.statusLoadingWheel.isVisible = false
                        }
                        else -> {
                            binding.statusLoadingWheel.isVisible = false
                        }
                    }
                }
            }
            viewModel.asteroidList.collectLatest { asteroidList ->
                if (asteroidList.isEmpty()) {
                    binding.asteroidRecycler.isVisible = false
                    binding.noDataFoundTv.isVisible = true
                } else {
                    binding.asteroidRecycler.isVisible = true
                    binding.noDataFoundTv.isVisible = false

                    asteroidListAdapter.submitList(asteroidList)
                }
            }
        }
    }

    /**
     * Add the menu to the fragment.
     */
    private fun addMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.main_overflow_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.show_saved_asteroids -> {
                            viewModel.filterAsteroidListBySaved()
                            true
                        }

                        R.id.show_today_asteroids -> {
                            viewModel.filterAsteroidListByToday()
                            true
                        }

                        R.id.show_week_asteroids -> {
                            viewModel.filterAsteroidListByWeek()
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
        )
    }
}
