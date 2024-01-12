package com.cookbook.app.ui

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.cookbook.app.R
import com.cookbook.app.adapters.PagerAdapter
import com.cookbook.app.databinding.ActivityRecipeDetailsBinding
import com.cookbook.app.models.Result
import com.cookbook.app.resources.db.entity.FavEntity
import com.cookbook.app.ui.ingredients.RecipeIngredientsFragment
import com.cookbook.app.ui.instructions.RecipeInstructionsFragment
import com.cookbook.app.ui.overview.RecipeOverviewFragment
import com.cookbook.app.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsActivity : AppCompatActivity() {
    private var isRecipeSaved = false
    private var savedRecipeId = 0
    private var myBundle: Result? = null
    private val mainViewModel: MainViewModel by viewModels()
    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        // Retrieve the bundle
        myBundle = intent?.parcelable("result")
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(RecipeOverviewFragment())
        fragments.add(RecipeIngredientsFragment())
        fragments.add(RecipeInstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.isUserInputEnabled=false
        val pagerAdapter = PagerAdapter(this, fragments)
        viewPager.adapter = pagerAdapter

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        else if (item.itemId == R.id.save_to_fav_menu && !isRecipeSaved){
            saveAsFavorites(item)
        }
        else if(item.itemId == R.id.save_to_fav_menu && isRecipeSaved){
            deleteFromFav(item)
        }
        else if(item.itemId == R.id.share){
            shareItem(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareItem(item: MenuItem) {
        // Create a share intent
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        // Add the content you want to share (replace with your link)
        val shareMessage = "Check out this awesome recipe link:" + myBundle?.sourceUrl
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)

        // Start the chooser dialog
        startActivity(Intent.createChooser(shareIntent, "Share Recipe"))
    }


    private fun saveAsFavorites(item: MenuItem) {
        myBundle?.let { result ->
            val favEntity = FavEntity(0, result)
            mainViewModel.addFavRecipe(favEntity)
        }
        changeFavColor(item,R.color.yellow)
        showBottomSnackBar("Recipe Saved.")
        isRecipeSaved =true
    }

    private fun deleteFromFav(menuItem:MenuItem){
        myBundle?.let { result ->
            val favEntity = FavEntity(savedRecipeId, result)
            mainViewModel.deleteFavRecipe(favEntity)
            changeFavColor(menuItem,R.color.white)
            showBottomSnackBar("Recipe Removed from Favorites.")
            isRecipeSaved =false
        }
    }

    private fun showBottomSnackBar(msg: String) {
        val parentLayout = findViewById<View>(R.id.recipeDetailsLayout)
        Snackbar.make(
            parentLayout,
            msg,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    private fun changeFavColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this,color))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_fav_menu)
        checkRecipeSaved(menuItem!!)
        return true
    }

    private fun checkRecipeSaved(menuItem: MenuItem) {
        mainViewModel.getFavRecipes.observe(this) { favEntity ->
            try {
                for (savedItem in favEntity) {
                    if (savedItem.result.id == myBundle?.id) {
                        changeFavColor(menuItem, R.color.yellow)
                        savedRecipeId = savedItem.id
                        isRecipeSaved = true
                        break
                    }
                    else{
                        changeFavColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("RecipeDetailsActivity", e.message.toString())
            }
        }
    }
    private fun onShareButtonClick(){
        Log.d("RecipesFragment", "getApiData called")
    }

}