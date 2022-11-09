package khansatta.on.satta.app.ui.main.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import khansatta.on.satta.app.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StarLineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starline)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navController = this.findNavController(R.id.star_nav_host)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.star_nav_host)
        return navController.navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.star, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val items= item.itemId
        if (items == R.id.starline_chart) {
            val navController = Navigation.findNavController(this, R.id.star_nav_host)
            navController.navigateUp()
            navController.navigate(R.id.action_starLineDashboard_to_starChartFragment)

        }
        return super.onOptionsItemSelected(item)
    }


}