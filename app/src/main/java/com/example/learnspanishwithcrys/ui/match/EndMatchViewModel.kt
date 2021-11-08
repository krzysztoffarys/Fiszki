package com.example.learnspanishwithcrys.ui.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnspanishwithcrys.data.model.ResultMatch
import com.example.learnspanishwithcrys.other.Resource
import com.example.learnspanishwithcrys.repositories.Repository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class EndMatchViewModel @Inject constructor(
    private val repository: Repository,
    @Named("ranking")
    private val rankingCollection: CollectionReference
): ViewModel() {
    private val _resultStatus = MutableLiveData<Resource<List<ResultMatch>>>()
    val resultStatus: LiveData<Resource<List<ResultMatch>>> = _resultStatus
    val user = "Crys"

    fun addResult(time: Double) {
            val result = ResultMatch(user, time)
            repository.addResult(result)
    }

    fun allResults() {
        _resultStatus.postValue(Resource.loading(null))
        rankingCollection
            .orderBy("time")
            .limit(49)
            .get()
            .addOnCompleteListener { query ->
                if (query.isSuccessful) {
                    val documents = query.result?.documents
                    val list = mutableListOf<ResultMatch>()
                    if (documents!!.isNotEmpty()) {
                        for (document in documents) {
                            try {
                                val result = document.toObject<ResultMatch>()
                                if (result != null) {
                                    list.add(result)
                                }
                            } catch (e: Exception) {
                                _resultStatus.postValue(Resource.error(e.message.toString(), null))
                            }
                        }
                        _resultStatus.postValue(Resource.success(list))
                    }
                } else {
                    _resultStatus.postValue(Resource.error("unknown error", null))
                }
            }

    }

}