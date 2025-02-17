package org.keepgoeat.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.keepgoeat.domain.model.GoalDetail
import org.keepgoeat.domain.model.GoalSticker
import org.keepgoeat.domain.repository.GoalRepository
import org.keepgoeat.util.UiState
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GoalDetailViewModel @Inject constructor(private val goalRepository: GoalRepository) :
    ViewModel() {
    private val _goalStickers = MutableStateFlow<List<GoalSticker>>(emptyList())
    val goalStickers get() = _goalStickers.asStateFlow()
    private val _goalDetail = MutableStateFlow<GoalDetail?>(null)
    val goalDetail get() = _goalDetail.asStateFlow()
    private val _goalId = MutableLiveData<Int>()
    val goalId: LiveData<Int> get() = _goalId
    private val _keepState = MutableLiveData<UiState<Int>>()
    val keepState: LiveData<UiState<Int>> get() = _keepState
    private val _deleteState = MutableLiveData<UiState<Int>>()
    val deleteState: LiveData<UiState<Int>> get() = _deleteState

    fun fetchGoalDetailInfo(goalId: Int) {
        _goalId.value = goalId
        viewModelScope.launch {
            goalRepository.fetchGoalDetail(goalId).onSuccess { detail ->
                _goalDetail.value = detail
                _goalStickers.value = Array(CELL_COUNT) { idx ->
                    GoalSticker(idx, idx < detail.thisMonthCount)
                }.toList()
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }

    fun keepGoal() {
        viewModelScope.launch {
            goalId.value?.let { id ->
                goalRepository.keepGoal(id).let { keptData ->
                    keptData ?: return@launch
                    _keepState.value = UiState.Success(keptData.goalId)
                }
            }
        }
    }

    fun deleteGoal() {
        viewModelScope.launch {
            goalId.value?.let { id ->
                goalRepository.deleteGoal(id).let { deletedData ->
                    deletedData ?: return@launch
                    _deleteState.value = UiState.Success(deletedData.goalId)
                }
            }
        }
    }

    companion object {
        const val CELL_COUNT = 35
    }
}
