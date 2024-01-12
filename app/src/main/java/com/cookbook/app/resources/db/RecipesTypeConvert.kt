package com.cookbook.app.resources.db

import androidx.room.TypeConverter
import com.cookbook.app.models.FoodRecipe
import com.cookbook.app.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConvert {

    var gsonObj = Gson()

    @TypeConverter
    fun recipeToStr(recipe: FoodRecipe):String{
        return gsonObj.toJson(recipe)
    }

    @TypeConverter
    fun strToRecipe(str: String):FoodRecipe{
        val type = object : TypeToken<FoodRecipe>() {}.type
        return gsonObj.fromJson(str,type)
    }

    @TypeConverter
    fun resToStr(res:Result):String{
        return gsonObj.toJson(res)
    }

    @TypeConverter
    fun strToRes(str: String):Result{
        val type = object : TypeToken<Result>() {}.type
        return gsonObj.fromJson(str,type)
    }


}