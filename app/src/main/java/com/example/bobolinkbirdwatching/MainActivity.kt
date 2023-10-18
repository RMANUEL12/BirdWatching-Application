package com.example.bobolinkbirdwatching
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bobolinkbirdwatching.AddSighting
import com.example.bobolinkbirdwatching.BirdJournal
import com.example.bobolinkbirdwatching.HotSpots
import com.example.bobolinkbirdwatching.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_hotspots -> {
                    replaceFragment(HotSpots())
                    true
                }
                R.id.nav_add -> {
                    replaceFragment(AddSighting())
                    true
                }
                R.id.nav_journal -> {
                    replaceFragment(BirdJournal())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HotSpots())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
