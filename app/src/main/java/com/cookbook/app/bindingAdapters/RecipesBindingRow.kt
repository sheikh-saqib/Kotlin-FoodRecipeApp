package com.cookbook.app.bindingAdapters
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.cookbook.app.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.cookbook.app.models.Result
import com.cookbook.app.ui.RecipeDetailsActivity
import org.jsoup.Jsoup

class RecipesBindingRow {

    companion object{

        @BindingAdapter("onRecipeClickListener")
        //function static to use elsewhere in the project
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout:ConstraintLayout,result: Result){
            recipeRowLayout.setOnClickListener{
                try {
                    // Use findNavController to navigate without directions
                    val bundle = Bundle()
                    bundle.putParcelable("result", result)
                    val navController = recipeRowLayout.findNavController()
                    navController.navigate(R.id.recipeDetailsActivity, bundle)
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener",e.toString())
                }
            }
        }
        //load image
        @BindingAdapter("imageLoading")
        //function static to use elsewhere in the project
        @JvmStatic
        fun imageLoading(view: ImageView,url:String){
            view.load(url){
                crossfade(600)
                error(R.drawable.baseline_image_not_supported_24)
            }
        }

        //check if the recipe is vegan and apply green color
        @BindingAdapter("isVegan")
        //function static to use elsewhere in the project
        @JvmStatic
        fun isVegan(view:View,isVegan:Boolean){
            if(isVegan){
                when(view){
                    is TextView ->{
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }
        @BindingAdapter("parseHtml")
        //function static to use elsewhere in the project
        @JvmStatic
        fun parseHtml(textView: TextView, descrip : String?){
            if (descrip!=null){
                val description = Jsoup.parse(descrip).text()
                textView.text = description
            }
        }
    }
}