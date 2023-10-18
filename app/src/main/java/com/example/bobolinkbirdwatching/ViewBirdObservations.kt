package com.example.bobolinkbirdwatching

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class BirdObservationAdapter(context: Context, observations: List<BirdObservation>) :
    ArrayAdapter<BirdObservation>(context, 0, observations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.activity_view_bird_observations, parent, false
        )

        val observation = getItem(position)

        val breedText: TextView = itemView.findViewById(R.id.breedTextView)
        val dateText: TextView = itemView.findViewById(R.id.dateTextView)

        breedText.text = observation?.breed
        dateText.text = observation?.dateSpotted

        return itemView
    }
}
class ViewBirdObservations : AppCompatActivity() {
    private val sightingsList = mutableListOf<BirdObservation>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bird_observations)

        val observationList = findViewById<ListView>(R.id.observationList)

        val adapter = BirdObservationAdapter(this, sightingsList)
        observationList.adapter = adapter
    }
}