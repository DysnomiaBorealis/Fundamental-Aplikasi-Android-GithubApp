package com.example.githubapp

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.githubapp.ViewModel.MainViewModel
import com.example.githubapp.ViewModel.MainViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModelFactory = MainViewModelFactory(pref)
        val mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        // Set the theme before setContentView
        mainViewModel.themeSetting.observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val switchTheme = menu.findItem(R.id.action_switch_theme).actionView as SwitchMaterial

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModelFactory = MainViewModelFactory(pref)
        val mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        // Set the switch to match the current theme
        mainViewModel.themeSetting.observe(this) { isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                mainViewModel.saveThemeSetting(isChecked)
            }
        }

        return true
    }

}

