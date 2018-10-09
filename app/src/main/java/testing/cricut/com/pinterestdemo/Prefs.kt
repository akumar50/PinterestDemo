package testing.cricut.com.pinterestdemo

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
  val PREFS_FILENAME = "testing.cricut.com.pinterestdemo.prefs"
  val BOARD_ID = "board_id"
  val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

  var baordID: String
    get() = prefs.getString(BOARD_ID, "")
    set(value) = prefs.edit().putString(BOARD_ID, value).apply()
}