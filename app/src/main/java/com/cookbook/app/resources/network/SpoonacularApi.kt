package com.cookbook.app.resources.network

import com.cookbook.app.models.FoodRecipe
import com.cookbook.app.utilities.ProjectConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface SpoonacularApi {
    @GET("/recipes/complexSearch")
    @Headers("X-RapidAPI-Key: ${ProjectConstants.apiKey}")
    suspend fun getSpoonacularRecipes(
        //suspend used to run the call in background thread
        @QueryMap queries:Map<String,String>
    ): Response<FoodRecipe>


    @GET("/recipes/complexSearch")
    @Headers("X-RapidAPI-Key: ${ProjectConstants.apiKey}")
    suspend fun searchRecipe(
        //suspend used to run the call in background thread
        @QueryMap searchItem:Map<String,String>
    ): Response<FoodRecipe>
}