package com.picpay.desafio.android.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.network.ServiceCallBack
import com.picpay.desafio.android.data.network.repository.Repository
import com.picpay.desafio.android.ui.users.UsersViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getUsersLiveDataObserver: Observer<List<User>>

    @Mock
    private lateinit var messageLiveDataObserver: Observer<Int>

    private lateinit var viewModel: UsersViewModel

    @Test
    fun `when view model getUsers get success then sets getUsersLiveData`() {
        // Arrange
        val users = listOf(
            User(
                "https://randomuser.me/api/portraits/men/9.jpg",
                "Eduardo Santos", 1001, "@eduardo.santos"
            )
        )

        val resultSuccess = MockRepository(ServiceCallBack.Success(users))
        viewModel = UsersViewModel(resultSuccess)
        viewModel.getUsersLiveData.observeForever(getUsersLiveDataObserver)

        // Adt
        viewModel.getUsers()

        // Assert
        verify(getUsersLiveDataObserver).onChanged(users)
    }

    @Test
    fun `when view model getUsers get server error then sets messageLiveData`() {
        // Arrange
        val message = R.string.error

        val resultServerError = MockRepository(ServiceCallBack.Error(message))
        viewModel = UsersViewModel(resultServerError)
        viewModel.message.observeForever(messageLiveDataObserver)

        // Act
        viewModel.getUsers()

        // Assert
        verify(messageLiveDataObserver).onChanged(message)
    }
}

class MockRepository(private val result: ServiceCallBack) : Repository {
    override suspend fun getUsers(result: (result: ServiceCallBack) -> Unit) {
        result(this.result)
    }
}