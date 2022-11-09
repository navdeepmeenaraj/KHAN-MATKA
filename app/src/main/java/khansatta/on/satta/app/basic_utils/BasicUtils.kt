package khansatta.on.satta.app.basic_utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import khansatta.on.satta.app.basic_utils.Constants.FULL_DATE_FORMAT
import khansatta.on.satta.app.basic_utils.Constants.HUMAN_DATE_FORMAT
import khansatta.on.satta.app.basic_utils.Constants.IS_VERIFIED
import khansatta.on.satta.app.basic_utils.Constants.PREFS_TOKEN
import khansatta.on.satta.app.basic_utils.Constants.PREFS_USER_ID
import khansatta.on.satta.app.basic_utils.Constants.TAG
import com.pixplicity.easyprefs.library.Prefs
import com.sdsmdg.tastytoast.TastyToast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs


object BasicUtils {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate2(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return current.format(formatter)
    }

    fun getMinMaxBetMessage(): String {
        return "Minimum Bet ${Prefs.getInt(Constants.MIN_BET, 10)} | Maximum Bet ${
            Prefs.getInt(
                Constants.MAX_BET,
                10000
            )
        }"
    }

    fun containsDigit(s: String?): Boolean {
        var containsDigit = false
        if (s != null && !s.isEmpty()) {
            for (c in s.toCharArray()) {
                if (Character.isDigit(c).also { containsDigit = it }) {
                    break
                }
            }
        }
        return containsDigit
    }

    fun checkIfUserIsVerified(): Boolean {
        return Prefs.getInt(IS_VERIFIED, 0) == 1
    }

    fun getCurrentDate(): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat(HUMAN_DATE_FORMAT, Locale.ENGLISH)
        return df.format(c)
    }

    fun getDateForHistory(): String {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat(FULL_DATE_FORMAT, Locale.ENGLISH)
        return df.format(c)
    }

    fun Activity.showSuccessSnackBar(message: String) {
        TastyToast.makeText(this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }


    fun Activity.showErrorSnackBar(message: String) {
        TastyToast.makeText(this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    fun Activity.showInfoSnackBar(message: String) {
        TastyToast.makeText(this, message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }


    fun Activity.hideSoftKeyboard(view: View) {
        val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isBetween(value: Int): Boolean {
        return value in Prefs.getInt(Constants.MIN_BET, 9) until Prefs.getInt(
            Constants.MAX_BET,
            10001
        )
    }

    fun convertToStatus(status: Int): String {
        return if (status == 0) {
            "Closed"
        } else
            return "Running"

    }

    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun toDate(stringDate: String): String {
        val dateFormatter = SimpleDateFormat("H:m", Locale.ENGLISH)
        val date: Date? = dateFormatter.parse(stringDate)
        val timeFormatter = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
        return timeFormatter.format(date as Date)
    }

    fun bearerToken(): String {
        return "Bearer " + Prefs.getString(PREFS_TOKEN, "null")
    }

    fun cool(message: String) {
        Log.d(TAG, message)
    }

    fun userId(): Int {
        return Prefs.getInt(PREFS_USER_ID, 0)
    }

    fun lastDigit(number: Int): Int {
        return abs(number) % 10
    }

    fun sumOfDigits(n: String): Int {
        val arr = n.toList()
        val arrOfInt = arr.map { it.toString().toInt() }
        return arrOfInt.sum()
    }

    fun spDigits(): Array<Int> {
        return arrayOf(
            469,
            234,
            450,
            270,
            379,
            180,
            568,
            360,
            135,
            478,
            289,
            126,
            459,
            260,
            378,
            189,
            369,
            170,
            567,
            350,
            134,
            468,
            279,
            125,
            458,
            269,
            368,
            250,
            359,
            179,
            890,
            340,
            160,
            467,
            278,
            124,
            367,
            240,
            358,
            178,
            349,
            169,
            790,
            268,
            150,
            457,
            259,
            123,
            456,
            249,
            357,
            230,
            348,
            168,
            780,
            267,
            159,
            690,
            258,
            140,
            590,
            239,
            356,
            167,
            347,
            158,
            789,
            257,
            149,
            680,
            248,
            130,
            580,
            238,
            490,
            157,
            346,
            148,
            689,
            256,
            139,
            670,
            247,
            120,
            570,
            237,
            480,
            156,
            390,
            147,
            679,
            345,
            138,
            589,
            246,
            129,
            560,
            245,
            489,
            236,
            470,
            146,
            678,
            380,
            137,
            579,
            290,
            128,
            479,
            280,
            460,
            190,
            389,
            145,
            578,
            370,
            136,
            569,
            235,
            127
        )
    }

    fun dpDigits(): List<Int> {
        return listOf(
            100,
            110,
            112,
            113,
            114,
            115,
            116,
            117,
            118,
            119,
            122,
            133,
            144,
            155,
            166,
            177,
            188,
            199,
            200,
            220,
            223,
            224,
            225,
            226,
            227,
            228,
            229,
            233,
            244,
            255,
            266,
            277,
            288,
            299,
            300,
            330,
            334,
            335,
            336,
            337,
            338,
            339,
            344,
            355,
            366,
            377,
            388,
            399,
            400,
            440,
            445,
            446,
            447,
            448,
            449,
            455,
            466,
            477,
            488,
            499,
            500,
            550,
            556,
            557,
            558,
            559,
            566,
            577,
            588,
            599,
            600,
            660,
            667,
            668,
            669,
            677,
            688,
            699,
            700,
            770,
            778,
            779,
            788,
            799,
            800,
            880,
            889,
            899,
            900,
            990
        )
    }

    fun tpDigits(): Array<String> {
        return arrayOf("000", "111", "222", "333", "444", "555", "666", "777", "888", "999")
    }

    fun halfSangamDigits(): Array<Int> {
        return arrayOf(
            100,
            110,
            112,
            113,
            114,
            115,
            116,
            117,
            118,
            119,
            120,
            122,
            123,
            124,
            125,
            126,
            127,
            128,
            129,
            130,
            133,
            134,
            135,
            136,
            137,
            138,
            139,
            140,
            144,
            145,
            146,
            147,
            148,
            149,
            150,
            155,
            156,
            157,
            158,
            159,
            160,
            166,
            167,
            168,
            169,
            170,
            177,
            178,
            179,
            180,
            188,
            189,
            190,
            199,
            200,
            220,
            223,
            224,
            225,
            226,
            227,
            228,
            229,
            230,
            233,
            234,
            235,
            236,
            237,
            238,
            239,
            240,
            244,
            245,
            246,
            247,
            248,
            249,
            250,
            255,
            256,
            257,
            258,
            259,
            260,
            266,
            267,
            268,
            269,
            270,
            277,
            278,
            279,
            280,
            288,
            289,
            290,
            299,
            300,
            330,
            334,
            335,
            336,
            337,
            338,
            339,
            340,
            344,
            345,
            346,
            347,
            348,
            349,
            350,
            355,
            356,
            357,
            358,
            359,
            360,
            366,
            367,
            368,
            369,
            370,
            377,
            378,
            379,
            380,
            388,
            389,
            390,
            399,
            400,
            440,
            445,
            446,
            447,
            448,
            449,
            450,
            455,
            456,
            457,
            458,
            459,
            460,
            466,
            467,
            468,
            469,
            470,
            477,
            478,
            479,
            480,
            488,
            489,
            490,
            499,
            500,
            550,
            556,
            557,
            558,
            559,
            560,
            566,
            567,
            568,
            569,
            570,
            577,
            578,
            579,
            580,
            588,
            589,
            590,
            599,
            600,
            660,
            667,
            668,
            669,
            670,
            677,
            678,
            679,
            680,
            688,
            689,
            690,
            699,
            700,
            770,
            778,
            779,
            780,
            788,
            789,
            790,
            799,
            800,
            880,
            889,
            890,
            899,
            900,
            990
        )
    }

    fun singleDigits(): Array<String> {
        return arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    }
}
