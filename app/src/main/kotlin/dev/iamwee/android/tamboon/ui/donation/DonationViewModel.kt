package dev.iamwee.android.tamboon.ui.donation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.iamwee.android.tamboon.common.CoroutineContextProvider
import dev.iamwee.android.tamboon.common.Result
import dev.iamwee.android.tamboon.http.data.DonateBody

class DonationViewModel @ViewModelInject constructor(
    private val donateUseCase: DonateUseCase,
    private val provider: CoroutineContextProvider
) : ViewModel() {

    private val donateParams: MutableLiveData<DonateBody> = MutableLiveData()
    val donateResult: LiveData<Result<Unit>> = donateParams.switchMap {
        donateUseCase(it).asLiveData(viewModelScope.coroutineContext + provider.io)
    }

    fun donate(token: String, cardName: String, amountInSatang: Long) {
        donateParams.postValue(DonateBody(
            amount = amountInSatang,
            name = cardName,
            token = token)
        )
    }

}