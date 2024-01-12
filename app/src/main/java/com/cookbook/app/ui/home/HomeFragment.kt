package com.cookbook.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.cookbook.app.MyApp
import com.cookbook.app.viewModels.MainViewModel
import com.cookbook.app.R
import com.cookbook.app.adapters.RecipesAdapter
import com.cookbook.app.utilities.ApiResponse
import com.cookbook.app.utilities.NetworkListener
import com.cookbook.app.utilities.ProjectConstants.Companion.apiKey
import com.cookbook.app.utilities.observeOnce
import com.cookbook.app.viewModels.ViewModelRecipe
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var rView: View
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelRecipe: ViewModelRecipe
    private lateinit var connectionLister: NetworkListener

    //on create called before oncreateview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModelRecipe = ViewModelProvider(requireActivity()).get(ViewModelRecipe::class.java)
    }

    override fun onResume() {
            val myApp = requireActivity().application as MyApp
            super.onResume()
            if(myApp.fromFilter){
                loadShimmer()
                activity?.invalidateOptionsMenu()
                getApiData()
                mAdapter.notifyDataSetChanged()
                hideShimmer()
                myApp.fromFilter = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rView = inflater.inflate(R.layout.fragment_home, container, false)
        setupRecylerView()
//        getApiData()
        readDb()
        lifecycleScope.launch {
            connectionLister = NetworkListener()
            connectionLister.checkConnection(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
//                    viewModelRecipe.connectionStatus = status
//                    viewModelRecipe.showConnectionStatus()
                }
        }
        // Find the FloatingActionButton by its ID
        val fabAdd = rView.findViewById<FloatingActionButton>(R.id.floatingActionBtn_recipes)
        // Set the click listener for the FloatingActionButton
        fabAdd.setOnClickListener {
//            if (viewModelRecipe.connectionStatus) {
            onFabAddClick(it)
//            } else {
//                viewModelRecipe.showConnectionStatus()
//
//            }
        }
        return rView
    }

    private fun readDb() {
        lifecycleScope.launch {
            mainViewModel.getRecipes.observeOnce(viewLifecycleOwner) { db ->
                if (db.isEmpty()) {
                    getApiData()
                } else {
                    Log.d("RecipesFragment", "readDb called")
                    mAdapter.dataSetting(db[0].recipes)
                    hideShimmer()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
                Log.d("setupMenu", "onPrepareMenu")
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_recipes, menu)
                Log.d("setupMenu", "onCreateMenu")
                // Retrieve the search menu item
                val searchItem = menu.findItem(R.id.search_menu)
                if (searchItem != null) {
                    val searchView = searchItem.actionView as? SearchView
                    setupSearchView(searchView)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                Log.d("setupMenu", "onMenuItemSelected")
                return true
            }

            override fun onMenuClosed(menu: Menu) {
                Log.d("setupMenu", "onMenuClosed")
                super.onMenuClosed(menu)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSearchView(searchView: SearchView?) {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryString: String?): Boolean {
                Log.d("onQueryTextSubmit", "onQueryTextSubmit")
                if (queryString != null) {
                    searchApiItems(queryString)
                }
                else {
                    // If the query is empty, reset the data
                    resetData()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0.isNullOrBlank()) {
                    // If the search text is empty, reset the data
                    resetData()
                }
                return true
            }
        })
    }


    private fun loadShimmer() {
        shimmerLayout = rView.findViewById(R.id.shimmerLayout)
        recyclerView =
            rView.findViewById(R.id.recycler_view) // Replace with your actual RecyclerView ID

        // Start shimmer animation when loading
        shimmerLayout.startShimmer()

        // Hide RecyclerView while shimmering
        recyclerView.visibility = View.GONE
    }

    private fun hideShimmer() {
        // Stop shimmer animation when done loading
        shimmerLayout.stopShimmer()

        // Show RecyclerView and hide shimmer layout
        recyclerView.visibility = View.VISIBLE
        shimmerLayout.visibility = View.GONE
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_recipes,menu)
//
//        val search = menu.findItem(R.id.search_menu)
//        val searchView = search.actionView as? SearchView
//        searchView?.isSubmitButtonEnabled = true
//        searchView?.setOnQueryTextListener(this)
//    }


    override fun onQueryTextSubmit(queryString: String?): Boolean {
        Log.d("onQueryTextSubmit", "onQueryTextSubmit")
        if (queryString != null) {
            searchApiItems(queryString)
        }
        else {
            // If the query is empty, reset the data
            resetData()
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if (p0.isNullOrBlank()) {
            // If the search text is empty, reset the data
            resetData()
        }
        return true
    }
    private fun setupRecylerView() {
        recyclerView = rView.findViewById(R.id.recycler_view)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadShimmer()

    }

    fun getApiData() {
        Log.d("RecipesFragment", "getApiData called")
        mainViewModel.getAllRecipes(viewModelRecipe.filterQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.ResponseSuccess -> {
                    hideShimmer()
                    response.responseData?.let { mAdapter.dataSetting(it) }
                }

                is ApiResponse.ResponseError -> {
                    hideShimmer()
                    loadCacheData()
                    Toast.makeText(
                        requireContext(),
                        response.responseMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResponse.ResponseLoading -> {
                    loadShimmer()
                }
            }
        }
    }

    private fun searchApiItems(searchQuery: String) {
        loadShimmer()
        mainViewModel.searchItemRecipe(viewModelRecipe.searchQueryFilter(searchQuery))
        mainViewModel.searchRecipeResponse.observe(viewLifecycleOwner) { res ->
            when (res) {
                is ApiResponse.ResponseSuccess -> {
                    hideShimmer()
                    val foodRecipe = res.responseData
                    foodRecipe?.let { mAdapter.dataSetting(it) }
                }

                is ApiResponse.ResponseError -> {
                    hideShimmer()
                    loadCacheData()
                    Toast.makeText(
                        requireContext(),
                        res.responseMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ApiResponse.ResponseLoading -> {
                    loadShimmer()
                }

            }
        }
    }

    private fun loadCacheData() {
        lifecycleScope.launch {
            mainViewModel.getRecipes.observe(viewLifecycleOwner) { db ->
                if (db.isEmpty()) {
                } else {
                    mAdapter.dataSetting(db[0].recipes)
                }
            }
        }
    }

    fun onFabAddClick(view: View) {
        findNavController().navigate(R.id.action_navigation_home_to_recipesFilter)
    }

    private fun resetData() {
        // Reload the original data, for example, by calling the method to fetch all recipes
        getApiData()
        mAdapter.notifyDataSetChanged()
        hideShimmer()
    }




}