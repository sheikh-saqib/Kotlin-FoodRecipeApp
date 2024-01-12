package com.cookbook.app.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cookbook.app.models.FoodRecipe
import com.cookbook.app.resources.Repos
import com.cookbook.app.resources.db.entity.FavEntity
import com.cookbook.app.resources.db.entity.RecipeEntity
import com.cookbook.app.utilities.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repos: Repos,
    application: Application
) : AndroidViewModel(application) {

    //section for room db
    val getRecipes: LiveData<List<RecipeEntity>> = repos.localData.readDbRecipes().asLiveData()
    val getFavRecipes:LiveData<List<FavEntity>> = repos.localData.readDbFavRecipes().asLiveData()
    private fun addRecipes(recipeEntity: RecipeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repos.localData.addRecipes(recipeEntity)
        }

    fun addFavRecipe(favEntity: FavEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repos.localData.addFavRecipe(favEntity)
        }

     fun deleteFavRecipe(favEntity: FavEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repos.localData.deleteFavRecipes(favEntity)
        }
     fun deleteAllFavRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repos.localData.deleteAllFav()
        }


    //section for retro fit
    var recipeResponse: MutableLiveData<ApiResponse<FoodRecipe>> = MutableLiveData()
    var searchRecipeResponse:MutableLiveData<ApiResponse<FoodRecipe>> = MutableLiveData()

    fun getAllRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getAllRecipesSafeCall(queries)
    }

    fun searchItemRecipe(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipeItemSafeCall(searchQuery)
    }

    private suspend fun getAllRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = ApiResponse.ResponseLoading()
        if (checkConnectivity()) {
            try {

                val apiResponse = repos.remoteData.getAllRecipes(queries)
                recipeResponse.value = handleApiResponse(apiResponse)
                val foodItemRecipe = recipeResponse.value!!.responseData
                if(foodItemRecipe !=null){
                    offlineCacheRecipes(foodItemRecipe)
                }

            } catch (e: Exception) {
                recipeResponse.value = ApiResponse.ResponseError("No recipes found")
            }
        } else {
            recipeResponse.value =
                ApiResponse.ResponseError("Ooops No Internet Connection!!!")
        }
    }
    private suspend fun searchRecipeItemSafeCall(searchQuery: Map<String, String>) {
        searchRecipeResponse.value = ApiResponse.ResponseLoading()
        if (checkConnectivity()) {
            try {

                val apiResponse = repos.remoteData.searchRecipes(searchQuery)
                searchRecipeResponse.value = handleApiResponse(apiResponse)

            } catch (e: Exception) {
                searchRecipeResponse.value = ApiResponse.ResponseError("No recipes found")
            }
        } else {
            searchRecipeResponse.value =
                ApiResponse.ResponseError("Ooops No Internet Connection!!!")
        }
    }

    private fun offlineCacheRecipes(foodItemRecipe: FoodRecipe) {
        val recipesItemEntity = RecipeEntity(foodItemRecipe)
        addRecipes(recipesItemEntity)
    }

    private fun handleApiResponse(apiResponse: Response<FoodRecipe>): ApiResponse<FoodRecipe>? {
        when {
            apiResponse.message().toString().contains("timeout") -> {
                return ApiResponse.ResponseError("Connection Timeout")
            }

            apiResponse.code() == 402 -> {
                return ApiResponse.ResponseError("Unauthorized")
            }

            apiResponse.body()!!.results.isNullOrEmpty() -> {
                return ApiResponse.ResponseError("Recipes Not Found")
            }

            apiResponse.isSuccessful -> {
                val recipesData = apiResponse.body()
                return ApiResponse.ResponseSuccess(recipesData!!)
            }

            else -> {
                return ApiResponse.ResponseError(apiResponse.message())
            }
        }
    }

    private fun checkConnectivity(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val connectedNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectedNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}