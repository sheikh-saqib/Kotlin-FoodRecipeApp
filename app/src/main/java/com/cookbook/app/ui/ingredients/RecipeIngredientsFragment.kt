package com.cookbook.app.ui.ingredients

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.app.R
import com.cookbook.app.adapters.IngredsAdapter
import com.cookbook.app.models.Result

class RecipeIngredientsFragment : Fragment() {
    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    private val mAdapter: IngredsAdapter by lazy { IngredsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_recipe_ingredients, container, false)

        // Retrieve the Result object from the Intent
        val myBundle: Result? = activity?.intent?.parcelable("result")
        setupRecyclerView(view)

        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }
        return view
    }

    private fun setupRecyclerView(view: View){
        view.findViewById<RecyclerView>(R.id.recycler_view_ingredients).adapter = mAdapter
        view.findViewById<RecyclerView>(R.id.recycler_view_ingredients).layoutManager = LinearLayoutManager(requireContext())

    }
}