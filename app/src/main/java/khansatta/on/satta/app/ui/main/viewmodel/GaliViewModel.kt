package khansatta.on.satta.app.ui.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khansatta.on.satta.app.web_service.model.gali.GaliMarkets
import khansatta.on.satta.app.web_service.model.gali.bidhis
import khansatta.on.satta.app.web_service.model.gali.BidResponse
import khansatta.on.satta.app.web_service.repository.GaliRepository
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.NetworkHelper
import khansatta.on.satta.app.basic_utils.Resource
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.launch

class GaliViewModel @ViewModelInject constructor(
    private val galirepo: GaliRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _wins = MutableLiveData<Resource<List<bidhis>>>()
    val wins: LiveData<Resource<List<bidhis>>>
        get() = _wins

    private val _bids = MutableLiveData<Resource<List<bidhis>>>()
    val bids: LiveData<Resource<List<bidhis>>>
        get() = _bids


    private val _place_bid = MutableLiveData<Resource<BidResponse>>()
    val place_bid: LiveData<Resource<BidResponse>>
        get() = _place_bid

    private val _gali = MutableLiveData<Resource<List<GaliMarkets>>>()
    val galis: LiveData<Resource<List<GaliMarkets>>>
        get() = _gali

    fun fetchMarketChart(id: Int, onChartLoaded: (String) -> Unit): String {
        var chartUrl = ""
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                galirepo.getMarketChart(id).let {
                    if (it.isSuccessful) {
                        chartUrl = it.body()?.url.toString()
                        onChartLoaded(it.body()?.url.toString())
                    } else {
                        chartUrl = "null"
                    }
                }
            }
        }
        return chartUrl
    }

    fun fetchDate() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                galirepo.getDate().let {
                    if (it.isSuccessful) {
                        Prefs.putString(
                            "gali_date", it.body()?.date
                        )
                    }
                }
            }
        }
    }

    fun galiBidPlaced(map: HashMap<String, Any?>) {
        viewModelScope.launch {
            _place_bid.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                galirepo.galiBidPlace(BasicUtils.bearerToken(), map).let {
                    Log.d("place", it.toString())
                    if (it.isSuccessful) {
                        _place_bid.postValue(Resource.success(it.body()))
                    }
                }
            } else _place_bid.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun getGaliMarkets() {
        viewModelScope.launch {
            _gali.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                galirepo.getGaliMarkets().let {
                    Log.d("galimarkes", it.toString())
                    if (it.isSuccessful) {
                        _gali.postValue(Resource.success(it.body()))
                    }
                }
            } else _gali.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun fetchGaliWinHistory() {
        viewModelScope.launch {
            _wins.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                galirepo.getGaliWins(BasicUtils.bearerToken(), userId = BasicUtils.userId()).let {
                    Log.d("wins", it.body().toString())
                    if (it.isSuccessful) {
                        _wins.postValue(Resource.success(it.body()))
                    } else {
                        BasicUtils.cool("Error Place Bet ${it}")
                    }
                }
            } else _wins.postValue(Resource.error("No Internet Connection", null))


        }
    }

    fun fetchGaliBidHistory() {
        viewModelScope.launch {
            _bids.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                galirepo.getGaliBids(BasicUtils.bearerToken(), userId = BasicUtils.userId()).let {

                    if (it.isSuccessful) {
                        _bids.postValue(Resource.success(it.body()))
                    } else {
                        BasicUtils.cool("Error Place Bet $it")
                    }
                }
            } else _bids.postValue(Resource.error("No Internet Connection", null))

        }
    }
}