package khansatta.on.satta.app.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khansatta.on.satta.app.web_service.model.*
import khansatta.on.satta.app.web_service.repository.MainRepository
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.NetworkHelper
import khansatta.on.satta.app.basic_utils.Resource
import kotlinx.coroutines.launch

class SecondViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _config = MutableLiveData<Resource<AppConfig>>()
    val config: LiveData<Resource<AppConfig>>
        get() = _config

    private val _bets = MutableLiveData<Resource<List<BidHistory>>>()
    val bets: LiveData<Resource<List<BidHistory>>>
        get() = _bets

    private val _wins = MutableLiveData<Resource<List<WinHistory>>>()
    val wins: LiveData<Resource<List<WinHistory>>>
        get() = _wins

    private val _profile = MutableLiveData<Resource<UserProfile>>()
    val profile: LiveData<Resource<UserProfile>>
        get() = _profile

    private val _points = MutableLiveData<Resource<UserPoints>>()
    val points: LiveData<Resource<UserPoints>>
        get() = _points

    private val _betPlace = MutableLiveData<Resource<PlaceBet>>()
    val betPlace: LiveData<Resource<PlaceBet>>
        get() = _betPlace

    private val _oneMarket = MutableLiveData<Resource<SingleMarketData>>()
    val singleMarket: LiveData<Resource<SingleMarketData>>
        get() = _oneMarket

    private val _digits = MutableLiveData<Resource<MarketDataAndDigits>>()
    val digits: LiveData<Resource<MarketDataAndDigits>>
        get() = _digits

    private val _market = MutableLiveData<Resource<List<MarketData>>>()
    val market: LiveData<Resource<List<MarketData>>>
        get() = _market

    private val _transaction = MutableLiveData<Resource<List<WalletTransaction>>>()
    val transaction: LiveData<Resource<List<WalletTransaction>>>
        get() = _transaction

    fun getTransactionHistory(token: String, id: Int, from: String, to: String) {
        viewModelScope.launch {
            _transaction.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getTransactionHistory(token, id, from, to).let {
                    if (it.isSuccessful) {
                        BasicUtils.cool("Trans Data Success : ${it.body()}")
                        _transaction.postValue(Resource.success(it.body()))
                    } else {
                        _transaction.postValue(Resource.error("Error Code ${it.code()}", null))
                    }
                }
            } else _transaction.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun fetchAppConfig(token: String) {
        viewModelScope.launch {
            _config.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getAppConfig(token).let {
                    if (it.isSuccessful) {
                        _config.postValue(Resource.success(it.body()))
                    } else {
                        _config.postValue(Resource.error("Error", null))
                    }
                }
            } else _config.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun fetchBetHistory(token: String, userId: Int, from: String, to: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getBetHistory(token, userId, from, to).let {
                    if (it.isSuccessful) {
                        _bets.postValue(Resource.success(it.body()))
                    } else {
                        _bets.postValue(Resource.error("No Data", null))
                    }
                }
            } else {
                _bets.postValue(Resource.error("No Internet Connection", null))
            }
        }
    }

    fun fetchWinHistory(token: String, userId: Int, from: String, to: String) {
        viewModelScope.launch {
            _wins.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getWinHistory(token, userId, from, to).let {
                    if (it.isSuccessful) {
                        _wins.postValue(Resource.success(it.body()))
                    } else {
                        _wins.postValue(Resource.error("No Data", null))
                        BasicUtils.cool("Win Data Error : $it")
                    }
                }
            } else {
                _wins.postValue(Resource.error("No Internet Connection", null))
            }
        }
    }


    fun fetchUserProfile(token: String, userId: Int) {
        viewModelScope.launch {
            _profile.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUserProfile(token, userId).let {
                    if (it.isSuccessful) {
                        _profile.postValue(Resource.success(it.body()))
                    } else _profile.postValue(Resource.error(it.code().toString(), null))
                }
            } else _profile.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun getUserPoints(token: String, userId: Int) {
        viewModelScope.launch {
            _points.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUserPoints(token, userId).let {
                    if (it.code() == 200) {
                        _points.postValue(Resource.success(it.body()))
                    } else _points.postValue(Resource.error(it.code().toString(), null))
                }
            } else _points.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun placeBet(map: HashMap<String, Any?>) {
        viewModelScope.launch {
            _betPlace.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getBetHelper(map).let {
                    if (it.isSuccessful) {
                        _betPlace.postValue(Resource.success(it.body()))

                    } else _betPlace.postValue(Resource.error(it.code().toString(), null))
                }
            } else _betPlace.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun fetchOneMarketData(token: String, mainMarketId: Int) {
        viewModelScope.launch {
            _oneMarket.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getOneMarketData(token, mainMarketId).let {
                    if (it.isSuccessful) {
                        _oneMarket.postValue(Resource.success(it.body()))
                    } else _oneMarket.postValue(Resource.error(it.code().toString(), null))
                }
            } else _oneMarket.postValue(Resource.error("No Internet Connection", null))
        }
    }



    fun fetchMarkets(token: String) {
        viewModelScope.launch {
            _market.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getMainMarketData(token).let {
                    if (it.isSuccessful) {
                        _market.postValue(Resource.success(it.body()))
                    } else _market.postValue(Resource.error(it.code().toString(), null))
                }
            } else _market.postValue(Resource.error("No internet connection", null))
        }

    }

}