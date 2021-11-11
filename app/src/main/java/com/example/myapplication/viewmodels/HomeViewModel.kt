package com.example.myapplication.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.myapplication.data.Apirequest
import com.example.myapplication.data.PrefferenceManager
import com.example.myapplication.models.HomeResponse
import com.example.myapplication.models.UserAccess
import com.example.myapplication.networks.ApiClient
import com.example.myapplication.networks.NetworkState
import com.example.myapplication.networks.Status
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(context) as T
    }
}

class HomeViewModel(context: Context) : ViewModel() {

    private val client: Retrofit
    private val prefferenceManager: PrefferenceManager

    private var homeCall: Call<HomeResponse>? = null

    private val _homeResponse = MutableLiveData<HomeResponse>()
    val homeResponse: LiveData<HomeResponse>
        get() = _homeResponse

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    val userAccessLiveData: LiveData<UserAccess>
    var userAccess: UserAccess? = null

    init {
        val apiClient = ApiClient()
        client = apiClient.getClient()

        prefferenceManager = PrefferenceManager(context)
        userAccessLiveData = prefferenceManager.userAccessFlow.asLiveData()
        provideUserAccess()
    }

    fun provideUserAccess() = viewModelScope.launch {
        prefferenceManager.userAccessFlow.collect {
            userAccess = it
        }
    }

    fun homeData() {
        _networkState.postValue(NetworkState.LOADING)

        homeCall = client.create(Apirequest::class.java)
            .homeData(authorization = "${userAccess?.tokenType} ${userAccess?.accessToken}")

        homeCall?.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful && response.body() != null) {
                        _networkState.postValue(NetworkState.SUCCESS)
                        _homeResponse.postValue(response.body())
                    } else {
                        _networkState.postValue(
                            NetworkState(
                                Status.FAILED,
                                "failed, data home is null ${response.errorBody()}"
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
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

    fun logoutAccount() = viewModelScope.launch {
        prefferenceManager.resetUserAccess()
    }

    fun cancelAll() {
        homeCall?.cancel()
    }
}