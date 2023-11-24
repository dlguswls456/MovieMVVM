package com.example.moviemvvm.data.repository

import com.example.moviemvvm.App
import com.example.moviemvvm.R

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    //static 같은 개념 하지만 완전 동일하지는 않다
    //객체 생성하지 않아도 사용 가능하다
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, App.context().getString(R.string.success))
        val LOADING: NetworkState = NetworkState(Status.RUNNING, App.context().getString(R.string.running))
        val ERROR = NetworkState(Status.FAILED, App.context().getString(R.string.error))
        val ENDOFLIST = NetworkState(Status.FAILED, App.context().getString(R.string.end_of_list))
    }
}