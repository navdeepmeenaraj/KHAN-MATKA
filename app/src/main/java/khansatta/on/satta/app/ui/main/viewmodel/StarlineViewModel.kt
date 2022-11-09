package khansatta.on.satta.app.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khansatta.on.satta.app.web_service.model.starline.*
import khansatta.on.satta.app.web_service.repository.StarlineRepository
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.bearerToken
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.BasicUtils.userId
import khansatta.on.satta.app.basic_utils.NetworkHelper
import khansatta.on.satta.app.basic_utils.Resource
import kotlinx.coroutines.launch

class StarlineViewModel @ViewModelInject constructor(
    private val starlineRepository: StarlineRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _wins = MutableLiveData<Resource<List<StarWins>>>()
    val wins: LiveData<Resource<List<StarWins>>>
        get() = _wins

    private val _bids = MutableLiveData<Resource<List<StarlineBids>>>()
    val bids: LiveData<Resource<List<StarlineBids>>>
        get() = _bids

    private val _bet = MutableLiveData<Resource<StarBidPlace>>()
    val bet: LiveData<Resource<StarBidPlace>>
        get() = _bet

    private val _date = MutableLiveData<Resource<String>>()
    val date: LiveData<Resource<String>>
        get() = _date

    private val _starRates = MutableLiveData<Resource<StarlineRates>>()
    val starRates: LiveData<Resource<StarlineRates>>
        get() = _starRates

    private val _starMarkets = MutableLiveData<Resource<List<StarlineMarkets>>>()
    val starMarkets: LiveData<Resource<List<StarlineMarkets>>>
        get() = _starMarkets

    fun fetchStarWinHistory() {
        viewModelScope.launch {
            _wins.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                starlineRepository.getStarWins(bearerToken(), userId = userId()).let {
                    if (it.isSuccessful) {
                        _wins.postValue(Resource.success(it.body()))
                    } else {
                        cool("Error Place Bet ${it}")
                    }
                }
            } else _wins.postValue(Resource.error("No Internet Connection", null))


        }
    }

    fun fetchStarBidHistory() {
        viewModelScope.launch {
            _bids.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                starlineRepository.getStarBids(bearerToken(), userId = userId()).let {
                    if (it.isSuccessful) {
                        _bids.postValue(Resource.success(it.body()))
                    } else {
                        cool("Error Place Bet $it")
                    }
                }
            } else _bids.postValue(Resource.error("No Internet Connection", null))


        }
    }

    fun placeStarBet(map: HashMap<String, Any?>) {
        cool("Init Place Bet")
        viewModelScope.launch {
            _bet.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                cool("Loading Place Bet")
                starlineRepository.placeStarBid(bearerToken(), map).let {
                    if (it.isSuccessful) {
                        cool("Place Bet Success ${it.body()}")
                        _bet.postValue(Resource.success(it.body()))
                    } else {
                        cool("Error Place Bet ${it}")
                    }
                }
            } else _bet.postValue(Resource.error("No Internet Connection", null))


        }
    }

    fun currentDate() {
        viewModelScope.launch {
            _date.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                starlineRepository.getCurrentDate().let {
                    if (it.isSuccessful) {
                        _date.postValue(Resource.success(it.body()))
                    }
                }
            } else _date.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun getStarMarkets(t: String) {
        viewModelScope.launch {
            _starMarkets.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                starlineRepository.getStarMarkets(t).let {
                    BasicUtils.cool("Starline Market  Response ${it}")
                    if (it.isSuccessful) {
                        _starMarkets.postValue(Resource.success(it.body()))
                    }
                }
            } else _starMarkets.postValue(Resource.error("No Internet Connection", null))
        }
    }
}