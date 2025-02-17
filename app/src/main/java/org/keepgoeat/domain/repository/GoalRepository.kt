package org.keepgoeat.domain.repository

import org.keepgoeat.data.model.response.*
import org.keepgoeat.domain.model.GoalDetail

interface GoalRepository {
    suspend fun fetchHomeEntireData(): ResponseHome.HomeData?
    suspend fun achieveGoal(
        goalId: Int,
        isAchieved: Boolean
    ): ResponseGoalAchievement.ResponseGoalAchievementData?

    suspend fun uploadGoalContent(
        title: String,
        isMore: Boolean
    ): Result<Int>

    suspend fun editGoalContent(
        id: Int,
        title: String
    ): Result<Int>

    suspend fun fetchGoalDetail(goalId: Int): Result<GoalDetail>
    suspend fun keepGoal(id: Int): ResponseGoalKeep.ResponseGoalKeepData?
    suspend fun deleteGoal(id: Int): ResponseGoalDeleted.ResponseGoalDeletedData?
}
