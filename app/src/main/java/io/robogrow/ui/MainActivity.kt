package io.robogrow.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.robogrow.R
import io.robogrow.RobogrowApplication
import io.robogrow.classes.Grow
import io.robogrow.dummy.DummyContent
import io.robogrow.networking.AuthenticatedErrorListener
import io.robogrow.ui.growList.GrowListFragment
import io.robogrow.ui.growView.GrowViewActivity
import io.robogrow.utils.AppUtils


class MainActivity : AppCompatActivity(), GrowListFragment.OnListFragmentInteractionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        TODO("This needs to be moved into a class that inherits JsonArrayRequest, something that returns a JSONArray and handles authorization")
        val localJReq: JsonArrayRequest = object : JsonArrayRequest(
            "https://api.robogrow.io/grows",
            Response.Listener { response ->
                if (response != null) {

                    val groupListType =
                        object : TypeToken<ArrayList<Grow?>?>() {}.type
                    var growList: ArrayList<Grow> =
                        Gson().fromJson(response.toString(), groupListType)


                    // Successfully got grows
                    Log.w(
                        "SUCCESS",
                        "SUCCESS"
                    )
                }
            },
            AuthenticatedErrorListener(this@MainActivity)
        ) {
            //here before semicolon ; and use { }.
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                var retHeaders: HashMap<String, String> = hashMapOf()
                retHeaders["x-api-token"] =
                    AppUtils.loadUserFromSharedPreferences(this@MainActivity).token
                return retHeaders
            }

            override fun setRetryPolicy(retryPolicy: RetryPolicy?): Request<*> {
                return super.setRetryPolicy(
                    DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
                )
            }
        }

        RobogrowApplication.queue.addToRequestQueue(localJReq)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_settings
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
