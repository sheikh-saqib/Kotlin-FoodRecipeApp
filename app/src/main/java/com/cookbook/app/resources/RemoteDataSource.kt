package com.cookbook.app.resources

import com.cookbook.app.models.FoodRecipe
import com.cookbook.app.resources.network.SpoonacularApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val spoonacularApi: SpoonacularApi
) {

    suspend fun getAllRecipes(queries:Map<String,String>):Response<FoodRecipe>{
        return spoonacularApi.getSpoonacularRecipes(queries)
    }

    suspend fun searchRecipes(searchString:Map<String,String>):Response<FoodRecipe>{
        return spoonacularApi.searchRecipe(searchString)
    }
}