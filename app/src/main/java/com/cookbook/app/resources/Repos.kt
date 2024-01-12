package com.cookbook.app.resources

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repos @Inject constructor(
    remoteDs: RemoteDataSource,
    localDs: LocalDataSource
) {
    val remoteData = remoteDs
    val localData = localDs
}