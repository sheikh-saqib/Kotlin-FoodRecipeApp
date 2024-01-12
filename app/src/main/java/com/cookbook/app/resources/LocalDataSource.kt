package com.cookbook.app.resources

import com.cookbook.app.resources.db.entity.RecipeEntity
import com.cookbook.app.resources.db.RecipesDataAccessObject
import com.cookbook.app.resources.db.entity.FavEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDataAccessObject: RecipesDataAccessObject
) {

    suspend fun addRecipes(entityRecipes: RecipeEntity){
        recipesDataAccessObject.addRecipes(entityRecipes)
    }

    fun readDbRecipes(): Flow<List<RecipeEntity>>{
        return  recipesDataAccessObject.getRecipes()
    }

    fun readDbFavRecipes():Flow<List<FavEntity>>{
        return  recipesDataAccessObject.getFavRecipes()
    }

    suspend fun addFavRecipe(favEntity: FavEntity){
        return  recipesDataAccessObject.insertFavRecipe(favEntity)
    }

    suspend fun deleteFavRecipes(favEntity: FavEntity){
        return recipesDataAccessObject.removeFav(favEntity)
    }
    suspend fun deleteAllFav(){
        return recipesDataAccessObject.removeAllFav()
    }

}