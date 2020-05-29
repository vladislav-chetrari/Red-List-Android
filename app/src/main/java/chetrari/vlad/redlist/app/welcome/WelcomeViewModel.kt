package chetrari.vlad.redlist.app.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import chetrari.vlad.redlist.base.BaseViewModel
import chetrari.vlad.redlist.base.Event
import chetrari.vlad.redlist.data.persistence.UserPreferences
import chetrari.vlad.redlist.data.persistence.repository.CountryRepository
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    countryRepository: CountryRepository,
    private val userPreferences: UserPreferences
) : BaseViewModel() {

    val countries = countryRepository.all(context)
    val welcomePass = mutableLiveData(userPreferences.isWelcomePassed)
    val loadComplete: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        var currentProgress = 0
        fun checkLoad() = postValue(currentProgress == 1)
        addSource(countries) {
            if (it is Event.Success) currentProgress += 1
            checkLoad()
        }
    }

    fun onProceed() {
        userPreferences.isWelcomePassed = true
        welcomePass.mutable.postValue(true)
    }
}
