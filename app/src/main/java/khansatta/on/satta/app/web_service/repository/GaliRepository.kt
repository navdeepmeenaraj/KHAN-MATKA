package khansatta.on.satta.app.web_service.repository

import khansatta.on.satta.app.web_service.api.ApiHelper
import javax.inject.Inject

class GaliRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getGaliMarkets() = apiHelper.provideGaliMarktes()
    suspend fun getDate() = apiHelper.provideDate()
    suspend fun galiBidPlace(token: String, map: HashMap<String, Any?>) =
        apiHelper.providePlaceBid(token, map)

    suspend fun getGaliBids(token: String, userId: Int) =
        apiHelper.provideGaliBids(token, userId)

    suspend fun getGaliWins(token: String, userId: Int) =
        apiHelper.provideGaliWins(token, userId)

    suspend fun getMarketChart(id: Int) = apiHelper.provideChart(id)

}