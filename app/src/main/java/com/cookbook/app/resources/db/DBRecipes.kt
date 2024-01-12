package com.cookbook.app.resources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cookbook.app.resources.db.entity.FavEntity
import com.cookbook.app.resources.db.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class, FavEntity::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(RecipesTypeConvert::class)
abstract class DBRecipes:RoomDatabase() {
    abstract fun recipesDataAccessObj(): RecipesDataAccessObject
}

