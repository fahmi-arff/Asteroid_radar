package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import com.udacity.asteroidradar.models.Asteroid

class MainAdapter(val onClickListener: OnClickListener)  : ListAdapter<Asteroid, MainAdapter.AsteroidPropertyViewHolder>(DiffCallback) {
    // DiffCallBack to automatic smart feature like compare data & data change for efficiency
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    // for holder view & execute pending binding from binding adapter
    class AsteroidPropertyViewHolder(private var binding: ListItemAsteroidBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(marsProperty: Asteroid) {
            binding.property = marsProperty
            binding.executePendingBindings()
        }
    }

    // when create holder & inflate the layout for recyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MainAdapter.AsteroidPropertyViewHolder {
        return AsteroidPropertyViewHolder(ListItemAsteroidBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // to know the position item in recylerView and set onClick on item
    override fun onBindViewHolder(holder: MainAdapter.AsteroidPropertyViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}