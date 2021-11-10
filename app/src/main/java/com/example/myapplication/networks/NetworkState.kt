package com.example.myapplication.networks

enum class Status {
    RUNNING, SUCCESS, FAILED
}

class NetworkState(val status: Status, val message: String) {

    companion object {
        val SUCCESS: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState

        init {
            SUCCESS =
                NetworkState(
                    Status.SUCCESS,
                    "Success"
                )
            LOADING =
                NetworkState(
                    Status.RUNNING,
                    "Running"
                )
            ERROR =
                NetworkState(
                    Status.FAILED,
                    "Something went wrong"
                )
        }
    }
}