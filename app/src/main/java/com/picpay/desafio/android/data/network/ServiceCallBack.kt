package com.picpay.desafio.android.data.network

import com.picpay.desafio.android.data.model.User

sealed class ServiceCallBack {
    class Success(val users: List<User>): ServiceCallBack()

    class Error(val message: Int) : ServiceCallBack()
}
