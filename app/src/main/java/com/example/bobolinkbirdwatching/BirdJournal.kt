//package com.example.bobolinkbirdwatching
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.ListView
//import androidx.fragment.app.Fragment
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [BirdJournal.newInstance] factory method to
// * create an instance of this fragment.
// */
//class BirdJournal : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        // Array of desired items - replace with user store observations
//        val mItems: Array<String> = arrayOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Zero")
//
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_bird_journal, container, false)
//
//        val mListView = view.findViewById<ListView>(R.id.observationList)
//
//        // Creating a ListView adapter
//        val mAdapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, R.id.text_view, mItems)
//
//        // Setting the ListView adapter with the one created above
//        mListView.adapter = mAdapter
//
//        return view
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment BirdJournal.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            BirdJournal().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}
