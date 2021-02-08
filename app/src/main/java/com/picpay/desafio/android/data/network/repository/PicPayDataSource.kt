package com.picpay.desafio.android.data.network.repository

import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.data.network.ServiceCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PicPayDataSource(private val service: PicPayService) : Repository {

    override suspend fun getUsers(result: (callBack: ServiceCallBack) -> Unit) {
        try {
            service.getUsers()
                .enqueue(object : Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        when {
                            response.isSuccessful -> {
                                response.body()?.let { users ->
                                    ServiceCallBack.Success(users)
                                }?.let { result(it) }
                            }
                            else -> {
                                result(ServiceCallBack.Error(R.string.error))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        result(ServiceCallBack.Error(R.string.error))
                    }
                })
        } catch (e: Exception) {
            result(ServiceCallBack.Error(R.string.error))
        }
    }
}
