package lee.jaebeom.qrcodereader.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by leejaebeom on 2018. 2. 16..
 */

class SavePreference(val prefs: SharedPreferences){
//    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    fun saveSharedPreference(key: String, data: String){
        val editor = prefs.edit()
        editor.putString(key, data)
        editor.commit()
        editor.apply()
    }

    fun saveSharedPreference(key: String, data: Int){

        val editor = prefs.edit()
        editor.putInt(key, data)
        editor.commit()
        editor.apply()
    }

    fun getStringPreference(key: String) : String{
        return prefs.getString(key, "")
    }

}