package com.example.bobolinkbirdwatching

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BirdJournal.newInstance] factory method to
 * create an instance of this fragment.
 */


class BirdJournal : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Database Reference
    private lateinit var databaseRef: DatabaseReference

    // Listview object
    private lateinit var listView: ListView

    // Store a reference to the context
    private lateinit var fragmentContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bird_journal, container, false)

        // Initialize the DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().getReference("tbl_sightings")
        listView = view.findViewById(R.id.observationList)

        // Store the context reference
        fragmentContext = requireContext()

        return view

//        //Inflating the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_bird_journal, container, false)
//
//        //list of observations
//        val sightingsList = mutableListOf<BirdObservation>()
//
//        //Populating array with BirdObservations
//        val savedObservations: Array<String> = sightingsList.map { "${it.breed} - Date Spotted: ${it.dateSpotted}"}.toTypedArray()
//
//        val observationList = view.findViewById<ListView>(R.id.observationList)
//
//        // Creating a ListView adapter
//        val adapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, R.id.text_view, savedObservations)
//
//        // Setting the ListView adapter with the one created above
//        observationList.adapter = adapter
//
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create an instance of UserSession
        val userSession = UserSession(fragmentContext)

        // ID of the user currently logged in
        val id = userSession.userID

        val query: Query = databaseRef.orderByChild("user").equalTo(id)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val sightingList = mutableListOf<BirdObservation>()

                for (sightingSnapshot in snapshot.children) {
                    val sighting = sightingSnapshot.getValue(BirdObservation::class.java)
                    if (sighting != null) {
                        sightingList.add(sighting)
                    }
                }

                // Create and set the adapter
                val adapter = ObservationListAdapter(fragmentContext, sightingList)
                listView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Logging Error
                Log.e(ContentValues.TAG, "JOURNAL WAS NOT LOADED CORRECTLY. CHECK THIS!")
                Toast.makeText(requireContext(), "Something went wrong, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BirdJournal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BirdJournal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
