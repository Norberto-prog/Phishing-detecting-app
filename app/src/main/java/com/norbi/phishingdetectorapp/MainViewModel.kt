package com.norbi.phishingdetectorapp.viewmodel

import androidx.lifecycle.*
import com.norbi.phishingdetectorapp.network.ClassificationResult
import com.norbi.phishingdetectorapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _resultSMS = MutableLiveData<ClassificationResult?>()
    val smsResult: LiveData<ClassificationResult?> = _resultSMS
    private val _result = MutableLiveData<ClassificationResult?>()
    val result: LiveData<ClassificationResult?> = _result

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    private val _errorSmS = MutableLiveData<String?>()
    val smsError: LiveData<String?> = _errorSmS

    fun classifyEmail(text: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.classify(mapOf("text" to text))
                _result.value = res
                _error.value = null
            } catch (e: Exception) {
                _result.value = null
                _error.value = e.localizedMessage ?: "Unknown error"
            }
        }
    }
    fun clearResult(){
        _result.value = null
    }

    fun classifySms(inputSms: String) {
        viewModelScope.launch {
            try {
                val res = RetrofitInstance.api.classify(mapOf("text" to inputSms))
                _resultSMS.value = res
                _errorSmS.value = null
            } catch (e: Exception) {
                _resultSMS.value = null
                _errorSmS.value = e.localizedMessage ?: "Unknown error"
            }
        }
    }

    fun clearSmsResult() {
        _resultSMS.value = null
    }
}
