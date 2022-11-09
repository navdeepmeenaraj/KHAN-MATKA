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

class PViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _userBankDetails = MutableLiveData<Resource<UserBankDetails>>()
    val userBankDetails: LiveData<Resource<UserBankDetails>>
        get() = _userBankDetails


    private val _paymentDetails = MutableLiveData<Resource<PaymentUpdateResponse>>()
    val paymentDetails: LiveData<Resource<PaymentUpdateResponse>>
        get() = _paymentDetails


    private val _verifyUser = MutableLiveData<Resource<VerifyUser>>()
    val verifyUser: LiveData<Resource<VerifyUser>>
        get() = _verifyUser


    private val _transferPoints = MutableLiveData<Resource<TransferPointsResponse>>()
    val transferPoints: LiveData<Resource<TransferPointsResponse>>
        get() = _transferPoints


    private val _withdrawList = MutableLiveData<Resource<List<WithdrawList>>>()
    val withdrawList: LiveData<Resource<List<WithdrawList>>>
        get() = _withdrawList

    fun verifyUser(mobile: String) {
        viewModelScope.launch {
            _verifyUser.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.verifyUser(
                    BasicUtils.bearerToken(),
                    BasicUtils.userId(),
                    mobile
                ).let {
                    if (it.isSuccessful) {
                        _verifyUser.postValue(Resource.success(it.body()))
                    } else {
                        _verifyUser.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _verifyUser.postValue(Resource.error("No internet connection", null))
        }

    }

    fun transferPoints(mobile: String, amount: String) {
        viewModelScope.launch {
            _transferPoints.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.transferPoints(
                    BasicUtils.bearerToken(),
                    BasicUtils.userId(),
                    mobile, amount
                ).let {
                    if (it.isSuccessful) {
                        _transferPoints.postValue(Resource.success(it.body()))
                    } else {
                        _transferPoints.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _transferPoints.postValue(Resource.error("No internet connection", null))
        }
    }

    fun getUserBankDetails() {
        viewModelScope.launch {
            _userBankDetails.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.userBankDetails(
                    BasicUtils.bearerToken(),
                    BasicUtils.userId()
                ).let {
                    if (it.isSuccessful) {
                        _userBankDetails.postValue(Resource.success(it.body()))
                    } else {
                        _userBankDetails.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _userBankDetails.postValue(Resource.error("No internet connection", null))
        }

    }

    fun updatePaymentDetails(paymentType: Int, paymentNumber: String) {
        viewModelScope.launch {
            _paymentDetails.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.updatePaymentDetails(
                    BasicUtils.bearerToken(),
                    BasicUtils.userId(),
                    paymentType,
                    paymentNumber
                ).let {
                    if (it.isSuccessful) {
                        _paymentDetails.postValue(Resource.success(it.body()))
                    } else {
                        _paymentDetails.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _withdrawList.postValue(Resource.error("No internet connection", null))
        }
    }

    fun getWithdrawRequestList() {
        viewModelScope.launch {
            _withdrawList.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.fetchWithdrawRequestList(
                    BasicUtils.bearerToken(),
                    BasicUtils.userId()
                ).let {
                    if (it.isSuccessful) {
                        _withdrawList.postValue(Resource.success(it.body()))
                    } else {
                        _withdrawList.postValue(Resource.error(it.code().toString(), null))
                    }
                }
            } else _withdrawList.postValue(Resource.error("No internet connection", null))
        }
    }
}