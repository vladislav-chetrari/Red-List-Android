package chetrari.vlad.rts.app.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import chetrari.vlad.rts.base.BaseViewModel
import chetrari.vlad.rts.base.Event
import chetrari.vlad.rts.data.persistence.UserPreferences
import chetrari.vlad.rts.data.persistence.repository.CountryRepository
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    countryRepository: CountryRepository,
    private val userPreferences: UserPreferences
) : BaseViewModel() {

    //TODO add categories
    //TODO add regions
    val countries = countryRepository.all(context)
    val welcomePass = liveData(userPreferences.isWelcomePassed)
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
