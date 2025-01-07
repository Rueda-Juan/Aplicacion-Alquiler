package com.Juan.myapplication.UI.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.Juan.myapplication.R
import com.Juan.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, PersonaPageFragment.newInstance())
                    .commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, PersonaPageFragment.newInstance())
                            .commit()
                    true
                }
                R.id.navigation_calendar -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, CalendarFragment())
                            .commit()
                    true
                }
                else -> false
            }
        }
    }
}
