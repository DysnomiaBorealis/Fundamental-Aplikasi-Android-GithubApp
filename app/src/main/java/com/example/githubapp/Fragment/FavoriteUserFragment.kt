package com.example.githubapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.Adapter.UserAdapter
import com.example.githubapp.ViewModel.UserViewModel
import com.example.githubapp.databinding.FragmentFavoriteUserBinding

class FavoriteUserFragment : Fragment() {

        private lateinit var binding: FragmentFavoriteUserBinding
        private val viewModel: UserViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)
            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserAdapter ()
        binding.favoriteUsersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteUsersRecyclerView.adapter = adapter

        viewModel.allUsers.observe(viewLifecycleOwner, Observer { users ->
            users?.let { adapter.updateUserList(it) }
        })
    }

    }

