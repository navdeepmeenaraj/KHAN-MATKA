package khansatta.on.satta.app.ui.main.viewmodel

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import khansatta.on.satta.app.web_service.model.*
import khansatta.on.satta.app.web_service.model.login.AuthResponse
import khansatta.on.satta.app.web_service.repository.MainRepository
import khansatta.on.satta.app.basic_utils.BasicUtils
import khansatta.on.satta.app.basic_utils.BasicUtils.cool
import khansatta.on.satta.app.basic_utils.NetworkHelper
import khansatta.on.satta.app.basic_utils.Resource
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _imageList = MutableLiveData<Resource<BannerImageList>>()
    val imageList: LiveData<Resource<BannerImageList>>
        get() = _imageList


    private val _videoLink = MutableLiveData<Resource<VideoLink>>()
    val videoLik: LiveData<Resource<VideoLink>>
        get() = _videoLink

    private val _rates = MutableLiveData<Resource<List<MarketRates>>>()
    val rates: LiveData<Resource<List<MarketRates>>>
        get() = _rates

    private val _verified = MutableLiveData<Resource<UserVerified>>()
    val isVerified: LiveData<Resource<UserVerified>>
        get() = _verified

    private val _reset = MutableLiveData<Resource<PasswordResetResponse>>()
    val reset: LiveData<Resource<PasswordResetResponse>>
        get() = _reset

    private val _withdraw = MutableLiveData<Resource<WithdrawResponse>>()
    val withdraw: LiveData<Resource<WithdrawResponse>>
        get() = _withdraw

    private val _payment = MutableLiveData<Resource<PaymentDetails>>()
    val payment: LiveData<Resource<PaymentDetails>>
        get() = _payment

    private val _deposit = MutableLiveData<Resource<DepositResponse>>()
    val deposit: LiveData<Resource<DepositResponse>>
        get() = _deposit

    private val _server = MutableLiveData<Resource<ServerCheck>>()
    val server: LiveData<Resource<ServerCheck>>
        get() = _server

    private val userReg = MutableLiveData<Resource<RegisterResponse>>()
    val resp: LiveData<Resource<RegisterResponse>>
        get() = userReg

    private val _users = MutableLiveData<Resource<AuthResponse>>()
    val users: LiveData<Resource<AuthResponse>>
        get() = _users


    fun bannerImageList() {
        viewModelScope.launch {
            _imageList.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getImageList().let {
                    if (it.isSuccessful) {
                        _imageList.postValue(Resource.success(it.body()))
                    } else {
                        _imageList.postValue(Resource.error(it.message().toString(), null))
                    }
                }
            } else _imageList.postValue(Resource.error("No internet connection", null))
        }
    }


    fun passwordReset(passwordReset: PasswordReset) {
        viewModelScope.launch {
            _reset.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.passwordReset(passwordReset).let {
                    if (it.isSuccessful) {
                        _reset.postValue(Resource.success(it.body()))
                    } else {
                        _reset.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _reset.postValue(Resource.error("No internet connection", null))
        }
    }

    fun withdrawRequest(withdrawRequest: WithdrawRequest) {
        viewModelScope.launch {
            _withdraw.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.withdrawFunds(BasicUtils.bearerToken(), withdrawRequest).let {
                    if (it.isSuccessful && it.body()!!.status) {
                        _withdraw.postValue(Resource.success(it.body()))
                    } else {
                        _withdraw.postValue(Resource.error(it.message().toString(), it.body()))
                    }
                }
            } else _withdraw.postValue(Resource.error("No internet connection", null))
        }
    }

    fun checkUserToken(token: String) {
        viewModelScope.launch {
            _server.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getServerStatus(token).let {
                    if (it.isSuccessful) {
                        cool("Sever Status Fetched : ${it.body()}")
                        _server.postValue(Resource.success(it.body()))
                    } else {
                        cool("Sever Status Error Fetching : ${it.code()}")
                        _server.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _server.postValue(Resource.error("No internet connection", null))
        }
    }


    fun dbPaymentData(context: Context): LiveData<khansatta.on.satta.app.web_service.PaPaDetails>? {
        return mainRepository.dbPaymentDetails(context)
    }

    fun insertPaymentDetails(
        context: Context,
        paPaDetails: khansatta.on.satta.app.web_service.PaPaDetails
    ) {
        mainRepository.insertData(context, paPaDetails)
    }

    fun addFunds(depositModel: DepositModel) {
        viewModelScope.launch {
            _deposit.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.addFunds(BasicUtils.bearerToken(), depositModel)
                    .let {
                        cool("payment Response: ${it.body()}")
                        if (it.isSuccessful) {
                            val data = it.body()
                            if (data != null) {
                                _deposit.postValue(Resource.success(data))
                            } else
                                _deposit.postValue(
                                    Resource.error(
                                        "Unable to Complete Request",
                                        null
                                    )
                                )
                        } else
                            _deposit.postValue(Resource.error(it.code().toString(), null))

                    }
            } else
                _deposit.postValue(Resource.error("No internet connection", null))
        }
    }

    fun paymentDetails() {
        viewModelScope.launch {
            _payment.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getPaymentDetails(BasicUtils.bearerToken())
                    .let {
                        cool("payment Response: ${it.body()}")
                        if (it.isSuccessful) {
                            val data = it.body()
                            if (data != null) {
                                _payment.postValue(Resource.success(data))
                            } else
                                _payment.postValue(
                                    Resource.error(
                                        "Unable to fetch payment data",
                                        null
                                    )
                                )
                        } else
                            _payment.postValue(Resource.error(it.code().toString(), null))

                    }
            } else
                _payment.postValue(Resource.error("No internet connection", null))
        }
    }


    fun fetchUsers(username: String, password: String) {
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getLoginService(username, password).let {
                    if (it.isSuccessful) {
                        val data = it.body()
                        if (data!!.isError) {
                            _users.postValue(Resource.error(data.message, null))
                        } else
                            _users.postValue(Resource.success(it.body()))
                    } else
                        _users.postValue(Resource.error(it.code().toString(), null))

                }
            } else
                _users.postValue(Resource.error("No internet connection", null))
        }
    }

    fun regUser(name: String, username: String, password: String, pin: String) {
        viewModelScope.launch {
            userReg.postValue(Resource.loading(null))

            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUserRegService(name, username, password, pin).let {
                    if (it.isSuccessful) {
                        val data = it.body()
                        if (data!!.isError) {
                            userReg.postValue(Resource.error(data.message, null))
                        } else
                            userReg.postValue(Resource.success(it.body()))
                    } else
                        userReg.postValue(Resource.error(it.message(), null))
                }
            } else
                userReg.postValue(Resource.error("No internet connection", null))
        }
    }

    fun checkUserStatus() {
        viewModelScope.launch {
            _verified.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.checkUserStatus(BasicUtils.bearerToken(), BasicUtils.userId()).let {
                    if (it.isSuccessful) {
                        val data = it.body()
                        if (data != null) {
                            _verified.postValue(Resource.success(data))
                        } else {
                            _verified.postValue(Resource.error(it.message().toString(), null))
                        }
                    } else {
                        _verified.postValue(Resource.error(it.message().toString(), null))
                    }
                }
            } else {
                _verified.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    fun marketRates() {
        viewModelScope.launch {
            _rates.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.fetchGameRates().let {
                    if (it.isSuccessful) {
                        val data = it.body()
                        if (data != null) {
                            _rates.postValue(Resource.success(data))
                        } else {
                            _rates.postValue(Resource.error(it.message().toString(), null))
                        }
                    } else {
                        _rates.postValue(Resource.error(it.message().toString(), null))
                    }
                }
            } else {
                _rates.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    fun videoLink() {
        viewModelScope.launch {
            _videoLink.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getVideoLink(BasicUtils.bearerToken()).let {
                    if (it.isSuccessful) {
                        val data = it.body()
                        if (data != null) {
                            _videoLink.postValue(Resource.success(data))
                        } else {
                            _videoLink.postValue(Resource.error(it.message().toString(), null))
                        }
                    } else {
                        _videoLink.postValue(Resource.error(it.message().toString(), null))
                    }
                }
            } else {
                _videoLink.postValue(Resource.error("No internet connection", null))
            }
        }

    }
}