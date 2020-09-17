package io.robogrow.ui.logout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.robogrow.ui.login.LoginActivity
import io.robogrow.utils.AppUtils

class LogoutFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppUtils.removeUserFromSharedPreferences(context!!)
        startActivity(Intent(context, LoginActivity::class.java).apply {})
    }
}