package com.picpay.desafio.android.data.network.repository

import com.picpay.desafio.android.data.network.ServiceCallBack

interface Repository {

    suspend fun getUsers(result: (callBack: ServiceCallBack) -> Unit)
}
