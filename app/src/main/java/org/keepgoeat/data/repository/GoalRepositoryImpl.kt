package org.keepgoeat.data.repository

import org.keepgoeat.data.ApiResult
import org.keepgoeat.data.datasource.remote.GoalDataSource
import org.keepgoeat.data.model.response.ResponseHome
import org.keepgoeat.domain.repository.GoalRepository
import timber.log.Timber
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDataSource: GoalDataSource,
) : GoalRepository {
    override suspend fun fetchHomeEntireData(): ResponseHome.HomeData? {
        val result = goalDataSource.fetchHomeEntireData()
        return when (result) {
            is ApiResult.Success -> {
                result.data?.data
            }
            is ApiResult.NetworkError -> {
                Timber.d("Network Error")
                null
            }
            is ApiResult.GenericError -> {
                Timber.d("(${result.code}): $(result.message}")
                null
            }
        }
    }
}
