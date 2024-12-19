package com.example.bobolinkbirdwatching
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Initialising app with Firebase
        FirebaseApp.initializeApp(this)


        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_hotspots -> {
                    replaceFragment(FindBirds())
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
                R.id.nav_settings -> {
                    replaceFragment(UserSetting())
                    true
                }
                else -> false
            }
        }
        replaceFragment(FindBirds())


    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
