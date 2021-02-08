package com.picpay.desafio.android.ui.users

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.network.PicPayApi
import com.picpay.desafio.android.data.network.repository.PicPayDataSource
import com.picpay.desafio.android.ui.ViewModelFactory

class UsersFragment : Fragment(R.layout.fragment_users) {

    private lateinit var viewModel: UsersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var userAdapter: UserListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.user_list_progress_bar)

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactory(
                PicPayDataSource(
                    PicPayApi.service
                )
            )
        ).get(UsersViewModel::class.java)

        if (savedInstanceState == null) {
            viewModel.getUsers()
        }
    }

    override fun onResume() {
        super.onResume()

        progressBar.visibility = View.VISIBLE

        viewModel.getUsersLiveData.observe(viewLifecycleOwner, Observer { users ->
            progressBar.visibility = View.GONE

            userAdapter = UserListAdapter { user ->
                Toast.makeText(requireContext(), user.name + user.id, Toast.LENGTH_LONG).show()
            }

            recyclerView.run {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                )
                setHasFixedSize(true)
                adapter = userAdapter
                userAdapter.users = users
            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            progressBar.visibility = View.GONE

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                .show()
        })
    }

    private fun removeObserves() {
        viewModel.getUsersLiveData.removeObservers(this)
        viewModel.message.removeObservers(this)
    }

    override fun onDetach() {
        super.onDetach()
        removeObserves()
    }
}