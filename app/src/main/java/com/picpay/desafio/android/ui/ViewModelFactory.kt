package com.picpay.desafio.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.picpay.desafio.android.data.network.repository.Repository
import com.picpay.desafio.android.ui.users.UsersViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return modelClass.getConstructor(Repository::class.java)
                .newInstance(repository)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}