package com.cookbook.app.resources.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cookbook.app.models.FoodRecipe
import com.cookbook.app.utilities.ProjectConstants.Companion.tb_Recipes

@Entity(tableName = tb_Recipes)
class RecipeEntity(
    var recipes: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}