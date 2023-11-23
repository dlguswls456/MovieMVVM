package com.example.moviemvvm.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg: String) {
    //static 같은 개념 하지만 완전 동일하지는 않다
    //객체 생성하지 않아도 사용 가능하다
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR = NetworkState(Status.FAILED, "Something went wrong")
        val ENDOFLIST = NetworkState(Status.FAILED, "You have reached the end")
    }
}