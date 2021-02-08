package com.picpay.desafio.android.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.network.ServiceCallBack
import com.picpay.desafio.android.data.network.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: Repository) : ViewModel() {

    private val ioScope = CoroutineScope(context = Dispatchers.IO + SupervisorJob())

    private val _getUsersLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val getUsersLiveData: LiveData<List<User>> = _getUsersLiveData

    private val _message: MutableLiveData<Int> = MutableLiveData()
    val message: LiveData<Int> = _message

    fun getUsers() {
        ioScope.launch {
            try {
                repository.getUsers { result ->
                    when (result) {
                        is ServiceCallBack.Success -> {
                            _getUsersLiveData.value = result.users
                        }
                        is ServiceCallBack.Error -> {
                            _message.value = result.message
                        }
                    }
                }
            } catch (e: Exception) {
                _message.value = R.string.error
            }
        }
    }
}
