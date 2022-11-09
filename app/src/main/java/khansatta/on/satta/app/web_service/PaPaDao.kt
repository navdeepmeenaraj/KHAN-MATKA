package khansatta.on.satta.app.web_service

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PaPaDao {
    @Query("SELECT * FROM payment_details")
    fun getAll(): List<PaPaDetails>

    @Query("SELECT * FROM payment_details WHERE id = 1")
    fun getPaymentDetails(): LiveData<PaPaDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg paPaDetails: PaPaDetails)

    @Delete
    fun delete(paPaDetails: PaPaDetails)
}