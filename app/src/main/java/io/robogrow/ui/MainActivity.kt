package io.robogrow.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.robogrow.R
import io.robogrow.classes.Grow
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

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val headerView: View = navView.getHeaderView(0)
        val tvUsername: TextView = headerView.findViewById(R.id.tv_username)
        val tvEmail: TextView = headerView.findViewById(R.id.tv_email)

        val user = AppUtils.loadUserFromSharedPreferences(this@MainActivity)
        tvUsername.text = user.user.username
        tvEmail.text = user.user.email

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_logout
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onListFragmentInteraction(item: Grow?) {
        val intent = Intent(this@MainActivity, GrowViewActivity::class.java).apply { }

        intent.putExtra("id", item?._id)
        intent.putExtra("title", item?.name)

        startActivity(intent)
    }
}
