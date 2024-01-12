package com.cookbook.app.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cookbook.app.resources.DSRepo
import com.cookbook.app.utilities.ProjectConstants.Companion.apiKey
import com.cookbook.app.utilities.ProjectConstants.Companion.diet_type_default
import com.cookbook.app.utilities.ProjectConstants.Companion.list_num_default
import com.cookbook.app.utilities.ProjectConstants.Companion.meal_type_default
import com.cookbook.app.utilities.ProjectConstants.Companion.query_AddRecipeInformation
import com.cookbook.app.utilities.ProjectConstants.Companion.query_ApiKey
import com.cookbook.app.utilities.ProjectConstants.Companion.query_Diet
import com.cookbook.app.utilities.ProjectConstants.Companion.query_FillIngredients
import com.cookbook.app.utilities.ProjectConstants.Companion.query_Number
import com.cookbook.app.utilities.ProjectConstants.Companion.query_Search
import com.cookbook.app.utilities.ProjectConstants.Companion.query_Type
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRecipe @Inject constructor(
    application: Application,
    private val dsRepo: DSRepo

) : AndroidViewModel(application) {

    private var mT = meal_type_default
    private var dT = diet_type_default
    val getDietTypeAndMeal = dsRepo.getDietTypeAndMeal
    var connectionStatus = false
    fun dietTypeAndMealSave(mT: String, mTId: Int, dT: String, dTId: Int) =
        viewModelScope.launch(Dispatchers.IO){
            dsRepo.dietTypeAndMealSave(mT,mTId,dT,dTId)
    }

    fun filterQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            getDietTypeAndMeal.collect{vals ->
                mT = vals.checkedMealType
                dT = vals.checkedDietType
            }
        }
        queries[query_Number] = list_num_default
        queries[query_ApiKey] = apiKey
        queries[query_Type] = mT
        queries[query_Diet] = dT
        queries[query_AddRecipeInformation] = "true"
        queries[query_FillIngredients] = "true"

        return queries
    }

    fun showConnectionStatus() {
        Toast.makeText(getApplication(),"No Network Connection",Toast.LENGTH_SHORT).show()
    }

    fun searchQueryFilter(seachQuery:String):HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()
        queries[query_Search] = seachQuery
        queries[query_Number] = list_num_default
        queries[query_ApiKey] = apiKey
        queries[query_AddRecipeInformation] = "true"
        queries[query_FillIngredients] = "true"
        return queries
    }
}