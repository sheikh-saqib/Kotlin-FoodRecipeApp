package com.cookbook.app.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.app.R
import com.cookbook.app.adapters.FavRecipesAdapter
import com.cookbook.app.databinding.FragmentDashboardBinding
import com.cookbook.app.viewModels.MainViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val mViewModel:MainViewModel by viewModels()
    private val mAdapter: FavRecipesAdapter by lazy { FavRecipesAdapter(requireActivity(),mViewModel) }
    lateinit var mAdView : AdView

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val mView =  inflater.inflate(R.layout.fragment_dashboard, container, false)

        _binding = FragmentDashboardBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = this
        binding.mViewModel = mViewModel
        binding.mAdapter = mAdapter
        setupMenu()
        setRecyclerView(binding.favRecipesRecyclerView)
        loadBanner()
        return binding.root
    }

    private fun loadBanner() {
        MobileAds.initialize(requireContext()) {}

        mAdView = binding.root.findViewById(R.id.adView)  // Assuming adView is in the fragment layout
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun setRecyclerView(rV: RecyclerView){
        rV.adapter = mAdapter
        rV.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        //set binding to null when fragment is destroyed
        _binding = null
        mAdapter.clearActionContextual()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fav_menu,menu)
    }

    private fun displaySnackbar(){
        Snackbar.make(
            binding.root,
            "All recipes deleted.",
            Snackbar.LENGTH_SHORT
        ).setAction("Ok"){}
            .show()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
                Log.d("fav_menu", "onPrepareMenu")
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fav_menu, menu)
                Log.d("fav_menu", "onCreateMenu")
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                if(menuItem.itemId == R.id.fav_DeleteAll){
                    mViewModel.deleteAllFavRecipes()
                    displaySnackbar()
                }
                return true
            }

            override fun onMenuClosed(menu: Menu) {
                Log.d("fav_menu", "onMenuClosed")
                super.onMenuClosed(menu)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}

