package com.example.githubapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import androidx.navigation.fragment.findNavController
import com.example.githubapp.R
import com.example.githubapp.Repository.GithubRepository
import com.example.githubapp.utils.Resource
import com.example.githubapp.Network.RetrofitInstance
import com.example.githubapp.DataClass.UserEntity
import com.example.githubapp.ViewModel.GithubViewModel
import com.example.githubapp.ViewModel.UserViewModel
import com.example.githubapp.ViewModel.UserViewModelFactory
import com.example.githubapp.ViewModel.ViewModelFactory
import com.example.githubapp.databinding.FragmentUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: GithubViewModel by viewModels { ViewModelFactory(GithubRepository(
        RetrofitInstance.api
    )) }
    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(requireActivity().application) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = requireArguments().getString("username") ?: throw RuntimeException("Username is null")

        if (username != null) {
            viewModel.getUserDetail(username)

            viewModel.userDetail.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { userDetail ->
                            binding.userName.text = userDetail.name
                            binding.userBio.text = userDetail.bio
                            binding.userFollowers.text = "Followers: ${userDetail.followers}"
                            binding.userFollowing.text = "Following: ${userDetail.following}"

                            Glide.with(this)
                                .load(userDetail.avatarUrl)
                                .placeholder(CircularProgressDrawable(requireContext()).apply {
                                    strokeWidth = 5f
                                    centerRadius = 30f
                                    start()
                                })
                                .into(binding.userImage)

                            val user = UserEntity(
                                id = userDetail.id,
                                username = userDetail.login,
                                avatarUrl = userDetail.avatarUrl
                            )


                            userViewModel.isFavorite(user.id).observe(viewLifecycleOwner, Observer { isFavorite ->
                            if (isFavorite) {
                                    binding.favoriteButton.text = "Remove from favorites"
                                    // Update the button state to indicate that the user is a favorite
                                } else {
                                    binding.favoriteButton.text = "Add to favorites"
                                    // Update the button state to indicate that the user is not a favorite
                                }
                            })

                            binding.favoriteButton.setOnClickListener {
                                userViewModel.isFavorite(user.id).observe(viewLifecycleOwner, Observer { isFavorite ->
                                    if (isFavorite) {
                                        userViewModel.removeFromFavorites(user)
                                    } else {
                                        userViewModel.addToFavorites(user)
                                    }
                                })
                            }

                            binding.showFavoritesButton.setOnClickListener {
                                findNavController().navigate(R.id.action_UserDetailFragment_to_FavoriteUserFragment)
                            }
                        }
                    }
                    is Resource.Empty -> {

                        Toast.makeText(requireContext(), "No user detail found", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

        }

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                val fragment = when (position) {
                    0 -> FollowerListFragment()
                    1 -> FollowingListFragment()
                    else -> throw IllegalArgumentException("Invalid position")
                }

                fragment.arguments = Bundle().apply {
                    putString("username", username)
                }

                return fragment
            }
        }

        binding.viewPager.adapter = adapter

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> throw IllegalArgumentException("Invalid position")
            }
        }

        tabLayoutMediator.attach()
    }
}
