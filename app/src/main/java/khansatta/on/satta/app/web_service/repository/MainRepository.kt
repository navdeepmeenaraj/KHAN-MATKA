package khansatta.on.satta.app.web_service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import khansatta.on.satta.app.web_service.AppDatabase
import khansatta.on.satta.app.web_service.PaPaDao
import khansatta.on.satta.app.web_service.PaPaDetails
import khansatta.on.satta.app.web_service.api.ApiHelper
import khansatta.on.satta.app.web_service.model.DepositModel
import khansatta.on.satta.app.web_service.model.PasswordReset
import khansatta.on.satta.app.web_service.model.WithdrawRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dataDao: PaPaDao
) {


    private lateinit var appDatabase: AppDatabase


    var paPaDataModel: LiveData<PaPaDetails>? = null

    private fun initializeDB(context: Context): AppDatabase {
        return AppDatabase.getDatabaseClient(context)

    }

    suspend fun getImageList() = apiHelper.getImageList()


    fun insertData(context: Context, paPaDetails: PaPaDetails) {

        appDatabase = initializeDB(context)

        CoroutineScope(IO).launch {
            appDatabase.paymentDao().insertAll(paPaDetails)
        }

    }

    fun dbPaymentDetails(
        context: Context,
    ): LiveData<PaPaDetails>? {

        appDatabase = initializeDB(context)

        paPaDataModel = appDatabase.paymentDao().getPaymentDetails()
        return paPaDataModel
    }


    suspend fun getLoginService(username: String, password: String) =
        apiHelper.provideLoginService(username, password)

    suspend fun getUserRegService(name: String, username: String, password: String, pin: String) =
        apiHelper.provideRegisterService(name, username, password, pin)

    suspend fun getMainMarketData(token: String) =
        apiHelper.provideMarketResponse(token)

    suspend fun getOneMarketData(token: String, mainMarketId: Int) =
        apiHelper.provideOneMarketData(token, mainMarketId)

    suspend fun getServerStatus(token: String) =
        apiHelper.provideServerStatus(token)

    suspend fun getBetHelper(map: HashMap<String, Any?>) =
        apiHelper.provideBetPlace(map)

    suspend fun getUserPoints(token: String, userId: Int) =
        apiHelper.provideUserPoints(token, userId)

    suspend fun getUserProfile(token: String, userId: Int) =
        apiHelper.provideUserProfile(token, userId)

    suspend fun getBetHistory(token: String, userId: Int, from: String, to: String) =
        apiHelper.provideBetHistory(token, userId, from, to)

    suspend fun getWinHistory(token: String, userId: Int, from: String, to: String) =
        apiHelper.provideWinHistory(token, userId, from, to)

    suspend fun getAppConfig(token: String) =
        apiHelper.provideAppConfig(token)

    suspend fun getTransactionHistory(token: String, id: Int, from: String, to: String) =
        apiHelper.provideTransactionHistory(token, id, from, to)

    suspend fun getPaymentDetails(token: String) =
        apiHelper.providePaymentDetails(token)

    suspend fun addFunds(token: String, depositModel: DepositModel) =
        apiHelper.addFunds(token, depositModel)

    suspend fun withdrawFunds(token: String, withdrawRequest: WithdrawRequest) =
        apiHelper.withdrawFunds(token, withdrawRequest)

    suspend fun passwordReset(passwordReset: PasswordReset) = apiHelper.passwordReset(passwordReset)

    suspend fun checkUserStatus(token: String, userId: Int) =
        apiHelper.checkUserVerified(token, userId)

    suspend fun fetchWithdrawRequestList(token: String, userId: Int) =
        apiHelper.fetchWithdrawRequestList(token, userId)

    suspend fun fetchGameRates() = apiHelper.fetchMarketRates()

    suspend fun updatePaymentDetails(
        token: String,
        userId: Int,
        paymentType: Int,
        paymentNumber: String
    ) = apiHelper.addPaymentDetails(token, userId, paymentType, paymentNumber)

    suspend fun userBankDetails(token: String, userId: Int) =
        apiHelper.userBankDetails(token, userId)


    suspend fun verifyUser(token: String, userId: Int, mobile: String) =
        apiHelper.verifyUser(token, userId, mobile)

    suspend fun getVideoLink(token: String) = apiHelper.getVideoLink(token)
    suspend fun transferPoints(
        token: String,
        userId: Int,
        mobile: String,
        amount: String
    ) = apiHelper.transferPoints(
        token,
        userId,
        mobile,
        amount
    )


}