package org.ibadalrahman.remoteconfig.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.ibadalrahman.remoteconfig.repository.data.domain.FeatureFlagKey
import org.ibadalrahman.remoteconfig.repository.data.remote.RemoteConfigDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteConfigDataSource,
) : RemoteConfigRepository {

    private val _forceUpdateFlow = MutableStateFlow(false)
    override val forceUpdateFlow: StateFlow<Boolean> = _forceUpdateFlow.asStateFlow()

    override suspend fun refresh() {
        _forceUpdateFlow.value = remoteDataSource.fetchFlag(FeatureFlagKey.ForceUpdate)
    }
}
