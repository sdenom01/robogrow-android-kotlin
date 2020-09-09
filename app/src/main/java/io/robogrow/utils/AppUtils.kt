package io.robogrow.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import io.robogrow.classes.AuthenticatedUser


object AppUtils {
    // This is the sharedpreferences file that is used to save UserInfoDTO list.
    private val SHARED_PREFERENCES_FILE_USER_INFO_LIST = "userInfoList"

    // This is the saved UserInfoDTO list jason string key in sharedpreferences file..
    private val SHARED_PREFERENCES_KEY_USER_INFO_LIST = "User_Info_List"

    // This is the debug log info tag which will be printed in the android monitor console.
    private val USER_INFO_LIST_TAG = "USER_INFO_LIST_TAG"

    fun saveUserToSharedPreferences(ctx: Context, json: String) {
        val sharedPreferences: SharedPreferences = ctx.getSharedPreferences(
            SHARED_PREFERENCES_FILE_USER_INFO_LIST,
            MODE_PRIVATE
        )

        val editor = sharedPreferences.edit()
        editor.putString(SHARED_PREFERENCES_KEY_USER_INFO_LIST, json)
        editor.commit()
    }

    fun loadUserFromSharedPreferences(ctx: Context): AuthenticatedUser {
        val sharedPreferences = ctx.getSharedPreferences(
            SHARED_PREFERENCES_FILE_USER_INFO_LIST,
            MODE_PRIVATE
        )

        val userInfoListJsonString =
            sharedPreferences.getString(SHARED_PREFERENCES_KEY_USER_INFO_LIST, "")

        val gson = Gson()
        return (gson.fromJson(userInfoListJsonString, AuthenticatedUser::class.java))
    }
}