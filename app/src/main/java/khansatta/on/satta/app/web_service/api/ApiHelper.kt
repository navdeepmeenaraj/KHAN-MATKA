package khansatta.on.satta.app.web_service.api

import khansatta.on.satta.app.web_service.model.*
import khansatta.on.satta.app.web_service.model.gali.BidResponse
import khansatta.on.satta.app.web_service.model.gali.GaliMarkets
import khansatta.on.satta.app.web_service.model.gali.bidhis
import khansatta.on.satta.app.web_service.model.gali.date
import khansatta.on.satta.app.web_service.model.login.AuthResponse
import khansatta.on.satta.app.web_service.model.starline.*
import retrofit2.Response

interface ApiHelper {

    suspend fun provideLoginService(username: String, password: String):
            Response<AuthResponse>

    suspend fun getImageList(): Response<BannerImageList>


    suspend fun provideRegisterService(
        name: String,
        username: String,
        password: String,
        pin: String
    ): Response<RegisterResponse>

    suspend fun provideMarketResponse(token: String):
            Response<ArrayList<MarketData>>


    suspend fun provideOneMarketData(token: String, mainMarketId: Int):
            Response<SingleMarketData>

    suspend fun provideServerStatus(token: String):
            Response<ServerCheck>

    suspend fun provideBetPlace(map: HashMap<String, Any?>):
            Response<PlaceBet>

    suspend fun provideUserPoints(token: String, userId: Int):
            Response<UserPoints>

    suspend fun provideUserProfile(token: String, userId: Int):
            Response<UserProfile>

    suspend fun provideBannerImage(token: String):
            Response<BannerImage>

    suspend fun provideBetHistory(token: String, userId: Int, from: String, to: String):
            Response<List<BidHistory>>

    suspend fun provideWinHistory(token: String, userId: Int, from: String, to: String):
            Response<List<WinHistory>>

    suspend fun provideAppConfig(token: String):
            Response<AppConfig>

    suspend fun provideCurrentDate(): Response<String>

    //StarLine
    suspend fun provideStarMarkets(token: String): Response<List<StarlineMarkets>>
    suspend fun provideStarRates(token: String): Response<StarlineRates>
    suspend fun provideStarBidPlace(
        token: String,
        map: HashMap<String, Any?>
    ): Response<StarBidPlace>

    suspend fun provideStarWins(token: String, userId: Int): Response<List<StarWins>>
    suspend fun provideStarBids(token: String, userId: Int): Response<List<StarlineBids>>

    //gali routes
    suspend fun provideGaliMarktes(): Response<List<GaliMarkets>>
    suspend fun provideDate(): Response<date>
    suspend fun providePlaceBid(
        token: String,
        map: HashMap<String, Any?>
    ): Response<BidResponse>

    suspend fun provideGaliWins(token: String, userId: Int): Response<List<bidhis>>

    suspend fun provideGaliBids(token: String, userId: Int): Response<List<bidhis>>

    suspend fun provideChart(id: Int): Response<Charts>

    suspend fun provideTransactionHistory(
        token: String,
        id: Int,
        from: String,
        to: String
    ): Response<List<WalletTransaction>>

    suspend fun providePaymentDetails(token: String): Response<PaymentDetails>
    suspend fun addFunds(token: String, depositModel: DepositModel): Response<DepositResponse>
    suspend fun withdrawFunds(
        token: String,
        withdrawRequest: WithdrawRequest
    ): Response<WithdrawResponse>

    suspend fun passwordReset(passwordReset: PasswordReset): Response<PasswordResetResponse>
    suspend fun checkUserVerified(token: String, userId: Int): Response<UserVerified>

    suspend fun fetchWithdrawRequestList(token: String, userId: Int): Response<List<WithdrawList>>

    suspend fun fetchMarketRates(): Response<List<MarketRates>>

    suspend fun addPaymentDetails(
        token: String,
        userId: Int,
        paymentType: Int,
        paymentNumber: String
    ): Response<PaymentUpdateResponse>

    suspend fun userBankDetails(token: String, userId: Int): Response<UserBankDetails>

    suspend fun getVideoLink(token: String): Response<VideoLink>
    suspend fun transferPoints(
        token: String,
        userId: Int,
        mobile: String,
        amount: String
    ): Response<TransferPointsResponse>

    suspend fun verifyUser(
        token: String,
        userId: Int,
        mobile: String,
    ): Response<VerifyUser>
}