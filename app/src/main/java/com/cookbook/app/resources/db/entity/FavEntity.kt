package com.cookbook.app.resources.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cookbook.app.models.Result
import com.cookbook.app.utilities.ProjectConstants.Companion.tb_Favorite

@Entity(tableName = tb_Favorite)
class FavEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)