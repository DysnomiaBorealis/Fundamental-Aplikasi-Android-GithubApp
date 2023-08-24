package com.example.githubapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.databinding.FragmentSearchUserBinding
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.githubapp.Adapter.UserListAdapter
import com.example.githubapp.Repository.GithubRepository
import com.example.githubapp.utils.Resource
import com.example.githubapp.Network.RetrofitInstance
import com.example.githubapp.SearchUserFragmentDirections
import com.example.githubapp.ViewModel.GithubViewModel
import com.example.githubapp.ViewModel.ViewModelFactory

class SearchUserFragment : Fragment() {
    private lateinit var binding: FragmentSearchUserBinding
    private val viewModel: GithubViewModel by viewModels {
        ViewModelFactory(
            GithubRepository(
                RetrofitInstance.api
            )
        )
    }
    private lateinit var userListAdapter: UserListAdapter
    private val navController by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userListAdapter = UserListAdapter(mutableListOf()) { user ->

            val action =
                SearchUserFragmentDirections.actionSearchUserFragmentToUserDetailFragment(user.login)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userListAdapter
        }

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            viewModel.searchUsers(query)
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    resource.data?.let { data ->
                        userListAdapter.updateUsers(data)
                        Log.d("SearchUserFragment", "Updating user list with ${data.items.size} users.")
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Empty -> {

                    Toast.makeText(requireContext(), "No users found", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }
}