package khansatta.on.satta.app.web_service

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_details")
data class PaPaDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "upi_id")
    val upi_id: String,

    @ColumnInfo(name = "business_name")
    val business_name: String,

    @ColumnInfo(name = "payment_desc")
    val payment_desc: String,

    @ColumnInfo(name = "max_amount")
    val max_amount: Int,

    @ColumnInfo(name = "min_amount")
    val min_amount: Int,

    @ColumnInfo(name = "min_withdrawal")
    val min_withdrawal: Int,

    @ColumnInfo(name = "withdrawal_time_title")
    val withdrawal_time_title: String,

    @ColumnInfo(name = "merchant_code")
    val merchant_code: String
)