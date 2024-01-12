package com.cookbook.app.resources

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.cookbook.app.utilities.ProjectConstants.Companion.diet_type_default
import com.cookbook.app.utilities.ProjectConstants.Companion.meal_type_default
import com.cookbook.app.utilities.ProjectConstants.Companion.pref_Name
import com.cookbook.app.utilities.ProjectConstants.Companion.pref_diet_type
import com.cookbook.app.utilities.ProjectConstants.Companion.pref_diet_type_Id
import com.cookbook.app.utilities.ProjectConstants.Companion.pref_meal_type
import com.cookbook.app.utilities.ProjectConstants.Companion.pref_meal_type_Id
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DSRepo @Inject constructor(@ApplicationContext private val context: Context) {
    //data store runs on bg thread, not on main thread
    private object PrefKeys {
        val checkedMealType = preferencesKey<String>(pref_meal_type)
        val checkedMealTypeId = preferencesKey<Int>(pref_meal_type_Id)
        val checkedDietType = preferencesKey<String>(pref_diet_type)
        val checkedDietTypeId = preferencesKey<Int>(pref_diet_type_Id)
    }

    private val ds: DataStore<Preferences> = context.createDataStore(
        name = pref_Name
    )

    suspend fun dietTypeAndMealSave(mT: String, mTId: Int, dT: String, dTId: Int) {
        ds.edit { preferences ->
            preferences[PrefKeys.checkedMealType] = mT
            preferences[PrefKeys.checkedMealTypeId] = mTId
            preferences[PrefKeys.checkedDietType] = dT
            preferences[PrefKeys.checkedDietTypeId] = dTId
        }
    }

    val getDietTypeAndMeal: Flow<DietTypeAndMeal> = ds.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val checkedMealType = preferences[PrefKeys.checkedMealType] ?: meal_type_default
            val checkedMealTypeId = preferences[PrefKeys.checkedMealTypeId] ?: 0
            val checkedDietType = preferences[PrefKeys.checkedDietType] ?: diet_type_default
            val checkedDietTypeId = preferences[PrefKeys.checkedDietTypeId] ?: 0

            DietTypeAndMeal(
                checkedMealType, checkedMealTypeId, checkedDietType, checkedDietTypeId
            )
        }
}

data class DietTypeAndMeal(
    val checkedMealType: String,
    val checkedMealTypeId: Int,
    val checkedDietType: String,
    val checkedDietTypeId: Int
)