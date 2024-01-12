package com.cookbook.app.resources.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cookbook.app.resources.db.entity.FavEntity
import com.cookbook.app.resources.db.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDataAccessObject {

    //replace with new one when there is a conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipeEntity: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavRecipe(favEntity: FavEntity)

    //use flow for live data display
    @Query("SELECT * FROM recipes_tb")
    fun getRecipes(): Flow<List<RecipeEntity>>

    //use flow for live data display
    @Query("SELECT * FROM favorite_tb order by id asc")
    fun getFavRecipes(): Flow<List<FavEntity>>

    @Delete
    suspend fun removeFav(favEntity: FavEntity)

    @Query("Delete FROM favorite_tb")
    suspend fun removeAllFav()
}