package com.example.myapplication.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.myapplication.data.Apirequest
import com.example.myapplication.data.PrefferenceManager
import com.example.myapplication.models.HomeResponse
import com.example.myapplication.models.MenuRequest
import com.example.myapplication.models.MenuResponse
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

class MenuViewModeFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = MenuViewModel(context)
        return viewModel as T
    }

}

class MenuViewModel(context: Context) : ViewModel() {

    private val client: Retrofit
    private val prefferenceManager: PrefferenceManager

    private var menuCall: Call<MenuResponse>? = null

    private val _menuResponse = MutableLiveData<MenuResponse>()
    val menuResponse: LiveData<MenuResponse>
        get() = _menuResponse

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

    fun menuData() {
        _networkState.postValue(NetworkState.LOADING)
        menuCall = client.create(Apirequest::class.java)
            .menuData(
                "${userAccess?.tokenType} ${userAccess?.accessToken}",
                MenuRequest(show_all = 1)
            )
        menuCall?.enqueue(object : Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful && response.body() != null) {
                        _networkState.postValue(NetworkState.SUCCESS)
                        _menuResponse.postValue(response.body())
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

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
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

    fun cancelAll() {
        menuCall?.cancel()
    }
}