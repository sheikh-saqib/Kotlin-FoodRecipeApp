package com.cookbook.app.service

import android.content.Context
import androidx.room.Room
import com.cookbook.app.resources.db.DBRecipes
import com.cookbook.app.utilities.ProjectConstants.Companion.db_Name
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        DBRecipes::class.java,
        db_Name
    ).build()

    @Singleton
    @Provides
    fun provideDataAccessObject(db:DBRecipes) = db.recipesDataAccessObj()
}