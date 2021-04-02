package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var adapter: MainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        adapter = MainAdapter(MainAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        binding.asteroidRecycler.adapter = adapter

        // when list change submit it to adapter
        viewModel.asteroidList.observe(viewLifecycleOwner) {
            if(it != null){
                adapter.submitList(it)
            }
        }

        // navigate to detail with asteroid data
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner) {
            if ( it != null ) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     when (item.itemId) {
            R.id.view_week -> viewModel.getSeventhDayAsteroid()
            R.id.view_today -> viewModel.getTodayAsteroid()
            R.id.view_saved -> viewModel.getSavedAsteroid()
        }
    return true
    }
}
