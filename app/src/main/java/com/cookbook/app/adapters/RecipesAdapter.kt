package com.cookbook.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.app.databinding.RecipeRowLayoutBinding
import com.cookbook.app.models.FoodRecipe
import com.cookbook.app.models.Result
import com.cookbook.app.utilities.recipeDiffUtility

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    private var recipeResult = emptyList<Result>()

    class ViewHolder(private val binding: RecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(result: Result) {
            //bind data
            binding.result = result
            //update layout when data changes
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflaterLayout = LayoutInflater.from(parent.context)
                val binding = RecipeRowLayoutBinding.inflate(inflaterLayout, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return recipeResult.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current_item = recipeResult[position]
        //bind data to result
        holder.bindData(current_item)
    }

    fun dataSetting(newData: FoodRecipe) {
        val recipesDiffUtility = recipeDiffUtility(recipeResult, newData.results)
        //calculate the difference between new and old list so that old data does not load again
        val diffUtilityResult = DiffUtil.calculateDiff(recipesDiffUtility)
        recipeResult = newData.results
        diffUtilityResult.dispatchUpdatesTo(this)
        //populate recipeResult variable with new data

    }
}