package com.udacity.asteroidradar.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.local.models.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding

/**
 * @author Komi Donon
 * @since 5/14/2023
 */
class AsteroidListAdapter(private val clickItemCallback: (Asteroid) -> Unit) : ListAdapter<Asteroid, AsteroidListAdapter.AsteroidViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position), clickItemCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class AsteroidViewHolder(private val binding: AsteroidItemViewBinding) : ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, clickItemCallback: (Asteroid) -> Unit) {
            binding.root.setOnClickListener {
                clickItemCallback(asteroid)
            }

            binding.itemTitle.text = asteroid.codename
            binding.itemDate.text = asteroid.closeApproachDate

            if (asteroid.isPotentiallyHazardous) {
                binding.itemStatus.setImageResource(R.drawable.ic_status_potentially_hazardous)
                binding.itemStatus.contentDescription = binding.root.context.getString(R.string.potentially_hazardous_asteroid_image)
            } else {
                binding.itemStatus.setImageResource(R.drawable.ic_status_normal)
                binding.itemStatus.contentDescription = binding.root.context.getString(R.string.not_hazardous_asteroid_image)
            }
        }
    }
}
