package com.cookbook.app.adapters

import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.app.R
import com.cookbook.app.databinding.FavrecipeRowLayoutBinding
import com.cookbook.app.resources.db.entity.FavEntity
import com.cookbook.app.utilities.recipeDiffUtility
import com.cookbook.app.viewModels.MainViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class FavRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mViewModel : MainViewModel
) : RecyclerView.Adapter<FavRecipesAdapter.FavViewHolder>(), ActionMode.Callback {

    private var favRecipes = emptyList<FavEntity>()
    private var multiSelect = false
    private var selectedRecipes = arrayListOf<FavEntity>()
    private var mViewHolders = arrayListOf<FavViewHolder>()
    private lateinit var actionMode: ActionMode
    private lateinit var rView: View



    class FavViewHolder(private val binding: FavrecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(favEntity: FavEntity) {
            binding.favEntity = favEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val bindingData = FavrecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return FavViewHolder(bindingData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        return FavViewHolder.from(parent)
    }


    override fun getItemCount(): Int {
        return favRecipes.size
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        mViewHolders.add(holder)
        rView = holder.itemView.rootView
        val currentRecipe = favRecipes[position]
        holder.bindData(currentRecipe)

        val favConstraintLayout =
            holder.itemView.findViewById<ConstraintLayout>(R.id.favRecipesRowLayout)
        saveScrollState(currentRecipe,holder)
        //single click
        favConstraintLayout.setOnClickListener {
            if (multiSelect) {
                applySelectedRecipes(holder, currentRecipe)
            } else {
                val bundle = Bundle()
                bundle.putParcelable("result", currentRecipe.result)
                val navController = favConstraintLayout.findNavController()
                navController.navigate(R.id.recipeDetailsActivity, bundle)
            }

        }
        // Long Click
        holder.itemView.findViewById<ConstraintLayout>(R.id.favRecipesRowLayout)
            .setOnLongClickListener {
                if (!multiSelect) {
                    multiSelect = true
                    requireActivity.startActionMode(this)
                    applySelectedRecipes(holder, currentRecipe)
                    true
                } else {
                    applySelectedRecipes(holder,currentRecipe)
                    true
                }

            }
    }

    private fun saveScrollState(selectedCurrentRecipe:FavEntity,favHolder: FavViewHolder){
        if (selectedRecipes.contains(selectedCurrentRecipe)) {
            recipeChangeStyle(favHolder, R.color.cardBgLightColor, R.color.purple_200)
        } else {
            recipeChangeStyle(favHolder, R.color.cardBgColor, R.color.colorStroke)
        }
    }

    private fun applySelectedRecipes(favHolder: FavViewHolder, selectedCurrentRecipe: FavEntity) {
        if (selectedRecipes.contains(selectedCurrentRecipe)) {
            selectedRecipes.remove(selectedCurrentRecipe)
            recipeChangeStyle(favHolder, R.color.cardBgColor, R.color.colorStroke)
            setActionModeTitle()
        } else {
            selectedRecipes.add(selectedCurrentRecipe)
            recipeChangeStyle(favHolder, R.color.cardBgLightColor, R.color.purple_200)
            setActionModeTitle()
        }
    }

    private fun recipeChangeStyle(favHolder: FavViewHolder, bgColor: Int, strokeColor: Int) {
        favHolder.itemView.findViewById<ConstraintLayout>(R.id.favRecipesRowLayout)
            .setBackgroundColor(
                ContextCompat.getColor(requireActivity, bgColor)
            )
        favHolder.itemView.findViewById<MaterialCardView>(R.id.favrecipe_cardView).strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun onCreateActionMode(actMode: ActionMode?, menu: Menu?): Boolean {
        actMode?.menuInflater?.inflate(R.menu.fav_contextual_menu, menu)
        actionMode = actMode!!
        setStatusBarColor(R.color.statusBarContextual)
        return true
    }

    override fun onPrepareActionMode(actMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_fav_menu){
            selectedRecipes.forEach {
                mViewModel.deleteFavRecipe(it)
            }
            displaySnackBar("${selectedRecipes.size} Recipe/s Deleted.")
            multiSelect = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    private fun displaySnackBar(msg:String){
        Snackbar.make(
            rView,
            msg,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok"){}
            .show()
    }

    override fun onDestroyActionMode(actMode: ActionMode?) {
        mViewHolders.forEach { holder ->
            recipeChangeStyle(holder, R.color.cardBgColor, R.color.colorStroke)
        }
        multiSelect = false
        selectedRecipes.clear()
        setStatusBarColor(R.color.statusBar)
    }



    private fun setStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun settingData(newFavRecipes: List<FavEntity>) {
        val recipesDU = recipeDiffUtility(favRecipes, newFavRecipes)
        val diffUtilRes = DiffUtil.calculateDiff(recipesDU)
        favRecipes = newFavRecipes
        diffUtilRes.dispatchUpdatesTo(this)
    }

    fun clearActionContextual(){
        if(this::actionMode.isInitialized){
            actionMode.finish()
        }
    }

    private fun setActionModeTitle(){
        when(selectedRecipes.size){
            0 ->{
                actionMode.finish()
                multiSelect = false
            }
            1 ->{
                actionMode.title = "${selectedRecipes.size} recipe selected"
            }
            else ->{
            actionMode.title = "${selectedRecipes.size} recipes selected"
        }
        }
    }
}