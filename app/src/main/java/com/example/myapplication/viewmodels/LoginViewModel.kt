package com.example.myapplication.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.myapplication.data.Apirequest
import com.example.myapplication.data.PrefferenceManager
import com.example.myapplication.models.UserAccess
import com.example.myapplication.models.UserAccessRequest
import com.example.myapplication.networks.ApiClient
import com.example.myapplication.networks.NetworkState
import com.example.myapplication.networks.Status
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(context) as T
    }
}


class LoginViewModel(context: Context) : ViewModel() {

    private val client: Retrofit
    private val prefferenceManager: PrefferenceManager

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _userAccess = MutableLiveData<UserAccess>()
    val userAccess: LiveData<UserAccess>
        get() = _userAccess

    val userAccessLiveData: LiveData<UserAccess>

    init {
        val apiClient = ApiClient()
        client = apiClient.getClient()

        prefferenceManager = PrefferenceManager(context)
        userAccessLiveData = prefferenceManager.userAccessFlow.asLiveData()
    }

    fun processLogin(email: String, password: String) {
        _networkState.postValue(NetworkState.LOADING)
        val loginCall = client.create(Apirequest::class.java).reqUserAccess(
            UserAccessRequest(
                grantType = "password",
                clientSecret = "0a40f69db4e5fd2f4ac65a090f31b823",
                clientId = "e78869f77986684a",
                username = email,
                password = password
            )
        )
        loginCall.enqueue(object : Callback<UserAccess> {
            override fun onResponse(call: Call<UserAccess>, response: Response<UserAccess>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful && response.body() != null) {
                        _networkState.postValue(NetworkState.SUCCESS)
                        _userAccess.postValue(response.body())
                    } else {
                        _networkState.postValue(
                            NetworkState(
                                Status.FAILED,
                                "failed, data user access is null ${response.errorBody()}"
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<UserAccess>, t: Throwable) {
                if (!call.isCanceled) {
                    _networkState.postValue(
                        NetworkState(
                            Status.FAILED,
                            "failed, request error"
                        )
                    )
                }
            }

        })
    }

    fun updateUserAccessToDataStore(userAccess: UserAccess) = viewModelScope.launch {
        prefferenceManager.updateUserAccess(userAccess)
    }

    fun resetUserAccessToDataStore() = viewModelScope.launch {
        prefferenceManager.resetUserAccess()
    }
}