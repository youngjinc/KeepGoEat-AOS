package org.keepgoeat.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.keepgoeat.domain.repository.GoalRepository
import org.keepgoeat.presentation.model.GoalContent
import org.keepgoeat.presentation.type.EatingType
import org.keepgoeat.util.UiState
import org.keepgoeat.util.extension.toStateFlow
import org.keepgoeat.util.safeLet
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GoalSettingViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
) : ViewModel() {
    val goalTitle = MutableStateFlow<String>("")
    private val _eatingType = MutableStateFlow<EatingType?>(null)
    val eatingType get() = _eatingType.asStateFlow()
    var goalId: Int? = null

    val isValidTitle: StateFlow<Boolean>
        get() = goalTitle.map { title ->
            title.length in 1..15 && title.isNotBlank() && title.matches(TITLE_PATTERN.toRegex())
        }.toStateFlow(viewModelScope, false)

    private val _uploadState = MutableStateFlow<UiState<Int>>(UiState.Loading)
    val uploadState: StateFlow<UiState<Int>> get() = _uploadState

    fun uploadGoal() {
        if (goalId == null) addGoal()
        else editGoal()
    }

    private fun addGoal() {
        viewModelScope.launch {
            goalRepository.uploadGoalContent(
                goalTitle.value.trim(),
                eatingType.value == EatingType.MORE
            ).onSuccess {
                _uploadState.value = UiState.Success(it)
            }.onFailure {
                _uploadState.value = UiState.Error(it.message)
                Timber.e(it.message)
            }
        }
    }

    private fun editGoal() {
        viewModelScope.launch {
            safeLet(goalId, goalTitle.value) { id, title ->
                goalRepository.editGoalContent(id, title)
                    .onSuccess {
                        _uploadState.value = UiState.Success(it)
                    }.onFailure {
                        _uploadState.value = UiState.Error(it.message)
                        Timber.e(it.message)
                    }
            }
        }
    }

    fun setEatingType(eatingType: EatingType) {
        _eatingType.value = eatingType
    }

    fun setGoalContent(goal: GoalContent) {
        goalId = goal.id
        goalTitle.value = goal.goalTitle
    }

    companion object {
        private const val TITLE_PATTERN = "^[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣0-9\\s]*\$"
    }
}
