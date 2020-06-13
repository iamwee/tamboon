package dev.iamwee.android.tamboon.ui.charity

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.iamwee.android.tamboon.data.CharityInfo
import dev.iamwee.android.tamboon.data.CharityRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CharityViewModel @ViewModelInject constructor(
    repository: CharityRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val _charities: MutableLiveData<Unit> = MutableLiveData()
    val charities: LiveData<List<CharityInfo>> = _charities.switchMap {
        flow { emit(repository.getCharities()) }
            .catch { _error.value = it }
            .asLiveData(viewModelScope.coroutineContext)
    }

    private val _error: MutableLiveData<Throwable> = MutableLiveData()
    val error: LiveData<Throwable> = _error

    fun getCharities() {
        _charities.postValue(Unit)
    }

}