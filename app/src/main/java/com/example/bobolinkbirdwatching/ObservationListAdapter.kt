package com.example.bobolinkbirdwatching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ObservationListAdapter (private val context: Context, private val sightingList: List<BirdObservation>) : BaseAdapter() {

    override fun getCount(): Int = sightingList.size

    override fun getItem(position: Int): Any = sightingList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.observation_list_adapter, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val sighting = getItem(position) as BirdObservation
        holder.breedTextView.text = "Breed: " + sighting.breed
        holder.locationTextView.text = "Location Found: " + sighting.sightLocation
        holder.dateTextView.text = "Date Spotted: " + sighting.dateSpotted

        return view
    }

    private class ViewHolder(view: View) {
        val breedTextView: TextView = view.findViewById(R.id.breedTextView)
        val locationTextView: TextView = view.findViewById(R.id.locationTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    }
}