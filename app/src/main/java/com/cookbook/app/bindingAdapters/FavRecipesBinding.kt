package com.cookbook.app.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cookbook.app.adapters.FavRecipesAdapter
import com.cookbook.app.resources.db.entity.FavEntity

class FavRecipesBinding {

    companion object {
        @BindingAdapter("viewVisibility","settingData", requireAll = false)
        @JvmStatic
        fun settingDataAndViewVisibility(
            view: View,
            favEntity: List<FavEntity>?,
            mAdapter: FavRecipesAdapter?
        ) {
            //show the no recipes image and text when there are no values in the database
            if (favEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }

                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }

                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }

                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }

                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.settingData(favEntity)
                    }
                }
            }
        }
    }
}