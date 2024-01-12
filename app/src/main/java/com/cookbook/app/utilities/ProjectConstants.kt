package com.cookbook.app.utilities

class ProjectConstants {
    companion object{
        const val apiKey = ""
        const val baseUrl = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com"
        const val baseImgUrl = "https://spoonacular.com/cdn/ingredients_100x100/"


        // Query Constants
        const val query_Number = "number"
        const val query_ApiKey = "apiKey"
        const val query_Type = "type"
        const val query_Diet = "diet"
        const val query_AddRecipeInformation = "addRecipeInformation"
        const val query_FillIngredients = "fillIngredients"
        const val query_Search = "query"


        // ROOM DB

        const val db_Name = "recipes_db"
        const val tb_Recipes = "recipes_tb"
        const val tb_Favorite = "favorite_tb"

        // FILTER

        const val meal_type_default = "main course"
        const val diet_type_default = "gluten free"
        const val list_num_default = "50"
        const val pref_meal_type = "mealType"
        const val pref_meal_type_Id = "mealTypeId"
        const val pref_diet_type = "dietType"
        const val pref_diet_type_Id = "dietTypeId"
        const val pref_Name = "user_preferences"

    }
}