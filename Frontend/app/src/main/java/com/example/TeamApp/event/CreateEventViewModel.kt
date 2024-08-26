package com.example.TeamApp.event

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.TeamApp.data.Event
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDateTime


class CreateEventViewModel : ViewModel() {
    private val _sport = MutableLiveData<String>()
    val activityList =   mutableStateListOf<Event>()
    val sport: LiveData<String> get()= _sport
    private var isDataFetched = false

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    private val _dateTime = MutableLiveData<String>()
    val dateTime: LiveData<String> = _dateTime

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private val _limit = MutableLiveData<String>()
    val limit: LiveData<String> get() = _limit

    fun navigateToProfile(navController: NavController){
        navController.navigate("profile"){
            popUpTo("createEvent"){inclusive = true}
        }
    }
    fun navigateToSearchThrough(navController: NavController){
        navController.navigate("search"){
            popUpTo("createEvent"){inclusive = true}
        }
    }

    fun createEvent(event : Event){
        val db = Firebase.firestore
        db.collection("events").add(event)
            .addOnSuccessListener { Log.d("CreateEventViewModel", "Event successfully created") }
            .addOnFailureListener { e -> Log.w("CreateEventViewModel", "Error creating event", e) }
        activityList.add(event)
    }
    fun fetchEvents() {
        if (isDataFetched) return
        Log.d("CreateEventViewModel", "fetchEvents")
        val db = Firebase.firestore
        db.collection("events").get()
            .addOnSuccessListener { result ->
                activityList.clear()
                for (document in result) {
                    val event = document.toObject(Event::class.java)
                    activityList.add(event)
                }
                if (!activityList.isEmpty()) {
                    Log.d("CreateEventViewModel", "events found")
                }
                Log.d("CreateEventViewModel", "Events fetched successfully")
                isDataFetched = true
            }
            .addOnFailureListener { e ->
                Log.w("CreateEventViewModel", "Error fetching events", e)
            }
    }



    //temporary here
    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("register") {
            popUpTo("createEvent") { inclusive = true }
        }
    }

    fun onSportChange(newSport: String) {
        _sport.value = newSport.toString()
    }

    fun onAddressChange(newAddress: String) {
        _location.value = newAddress
    }

    fun onDateChange(newDate: String) {
        _dateTime.value = newDate
    }

    fun onLimitChange(newLimit: String) {
        _limit.value = newLimit
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun getAvailableSports():List<String>{
        return Event.sportIcons.keys.toList()
    }

}