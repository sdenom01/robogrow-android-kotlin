package io.robogrow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import io.robogrow.classes.User
import io.robogrow.dummy.DummyContent
import io.robogrow.networking.AuthenticatedErrorListener
import io.robogrow.networking.AuthenticatedGsonRequest
import io.robogrow.networking.grows.GetAllGrowsForUserId
import io.robogrow.utils.AppUtils

class MainActivity : AppCompatActivity(), GrowListFragment.OnListFragmentInteractionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        Toast.makeText(
//            this,
//            "HELLO " + AppUtils.loadUserFromSharedPreferences(this).user.username + " PULLED FROM SHARED!",
//            Toast.LENGTH_SHORT
//        ).show()

        // Request all grows for authenticated user
        var request = GetAllGrowsForUserId(
            this,
            Response.Listener { response ->
                if (response != null) {
                    // Successfully got grows
                    Log.w(
                        "SUCCESS",
                        "SUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESS" +
                                "SUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCC" +
                                "ESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESSSUCCESS"
                    )
                }
            },
            User::class.java
        )

        RobogrowApplication.queue.addToRequestQueue(request)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.d("STUB", "Implement list on click event!")

        val intent = Intent(this@MainActivity, GrowViewActivity::class.java).apply {

        }

        intent.putExtra("title", "Grow " + item?.id)

        startActivity(intent)
    }
}
