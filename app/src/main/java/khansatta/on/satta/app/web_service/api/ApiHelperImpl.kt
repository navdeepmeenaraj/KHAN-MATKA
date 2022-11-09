package khansatta.on.satta.app.web_service.api

import khansatta.on.satta.app.web_service.model.*
import khansatta.on.satta.app.web_service.model.gali.BidResponse
import khansatta.on.satta.app.web_service.model.gali.GaliMarkets
import khansatta.on.satta.app.web_service.model.gali.bidhis
import khansatta.on.satta.app.web_service.model.gali.date
import khansatta.on.satta.app.web_service.model.login.AuthResponse
import khansatta.on.satta.app.web_service.model.starline.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun provideLoginService(
        username: String,
        password: String
    ): Response<AuthResponse> =
        apiService.provideLoginService(username, password)

    override suspend fun getImageList(): Response<BannerImageList> = apiService.getBannerImageList()


    override suspend fun provideRegisterService(
        name: String,
        username: String,
        password: String,
        pin: String
    ): Response<RegisterResponse> =
        apiService.provideRegisterService(name, username, password, pin)

    override suspend fun provideMarketResponse(token: String): Response<ArrayList<MarketData>> =
        apiService.provideMarketData(token)


    override suspend fun provideOneMarketData(
        token: String,
        mainMarketId: Int
    ): Response<SingleMarketData> =
        apiService.provideOneMarketData(token, mainMarketId)

    override suspend fun provideServerStatus(token: String): Response<ServerCheck> =
        apiService.provideServerStatus(token)

    override suspend fun provideBetPlace(map: HashMap<String, Any?>): Response<PlaceBet> =
        apiService.provideBetPlace(map)

    override suspend fun provideUserPoints(token: String, userId: Int): Response<UserPoints> =
        apiService.provideUserPoints(token, userId)

    override suspend fun provideUserProfile(token: String, userId: Int): Response<UserProfile> =
        apiService.provideUserProfile(token, userId)

    override suspend fun provideBannerImage(token: String): Response<BannerImage> =
        apiService.provideBannerImage(token)


    override suspend fun provideBetHistory(
        token: String, userId: Int, from: String,
        to: String
    ): Response<List<BidHistory>> =
        apiService.provideBetHistory(token, userId, from, to)

    override suspend fun provideWinHistory(
        token: String,
        userId: Int,
        from: String,
        to: String
    ): Response<List<WinHistory>> =
        apiService.provideWinHistory(token, userId, from, to)

    override suspend fun provideAppConfig(token: String): Response<AppConfig> =
        apiService.provideAppConfig(token)

    override suspend fun provideCurrentDate(): Response<String> = apiService.provideCurrentDate()


    //StarLine
    override suspend fun provideStarMarkets(token: String): Response<List<StarlineMarkets>> =
        apiService.provideStarMarkets(token)

    override suspend fun provideStarRates(token: String): Response<StarlineRates> =
        apiService.provideStarRates(token)

    override suspend fun provideStarBidPlace(
        token: String,
        map: HashMap<String, Any?>
    ): Response<StarBidPlace> = apiService.provideStarBidPlace(token, map)

    override suspend fun provideStarWins(token: String, userId: Int): Response<List<StarWins>> =
        apiService.provideStarWinHistory(token = token, userID = userId)

    override suspend fun provideStarBids(token: String, userId: Int): Response<List<StarlineBids>> =
        apiService.provideStarBidHistory(token = token, userID = userId)

    override suspend fun provideGaliMarktes(): Response<List<GaliMarkets>> =
        apiService.provideGaliMarkets()

    override suspend fun provideDate(): Response<date> = apiService.getDate()
    override suspend fun providePlaceBid(
        token: String, map: HashMap<String, Any?>
    ): Response<BidResponse> = apiService.placeGaliBid(token, map)

    override suspend fun provideGaliWins(token: String, userId: Int): Response<List<bidhis>> =
        apiService.provideGaliWinHistory(token = token, userID = userId)

    override suspend fun provideGaliBids(token: String, userId: Int): Response<List<bidhis>> =
        apiService.provideGaliBidHistory(token = token, userID = userId)

    override suspend fun provideChart(id: Int): Response<Charts> = apiService.provideMarketChart(id)
    override suspend fun provideTransactionHistory(
        token: String,
        id: Int,
        from: String,
        to: String
    ): Response<List<WalletTransaction>> = apiService.getTransactionHistory(token, id, from, to)

    override suspend fun providePaymentDetails(token: String): Response<PaymentDetails> =
        apiService.getPaymentDetails(token)

    override suspend fun addFunds(
        token: String,
        depositModel: DepositModel
    ): Response<DepositResponse> =
        apiService.addFunds(token, depositModel)

    override suspend fun withdrawFunds(
        token: String,
        withdrawRequest: WithdrawRequest
    ): Response<WithdrawResponse> =
        apiService.withdrawFunds(token, withdrawRequest)

    override suspend fun passwordReset(passwordReset: PasswordReset): Response<PasswordResetResponse> =
        apiService.resetPassword(passwordReset)

    override suspend fun checkUserVerified(token: String, userId: Int): Response<UserVerified> =
        apiService.checkUserStatus(token, userId)

    override suspend fun fetchWithdrawRequestList(
        token: String,
        userId: Int
    ): Response<List<WithdrawList>> = apiService.fetchWithdrawRequestHistory(token, userId)

    override suspend fun fetchMarketRates(): Response<List<MarketRates>> =
        apiService.fetchGameRates()

    override suspend fun addPaymentDetails(
        token: String,
        userId: Int,
        paymentType: Int,
        paymentNumber: String
    ): Response<PaymentUpdateResponse> =
        apiService.addPaymentDetails(token, userId, paymentType, paymentNumber)

    override suspend fun userBankDetails(token: String, userId: Int): Response<UserBankDetails> =
        apiService.fetchPaymentDetails(token, userId)

    override suspend fun getVideoLink(token: String): Response<VideoLink> =
        apiService.getVideoLink(token)

    override suspend fun transferPoints(
        token: String,
        userId: Int,
        mobile: String,
        amount: String
    ): Response<TransferPointsResponse> = apiService.transferPoints(token, userId, mobile, amount)

    override suspend fun verifyUser(
        token: String,
        userId: Int,
        mobile: String
    ): Response<VerifyUser> = apiService.verifyUser(token, userId, mobile)

}