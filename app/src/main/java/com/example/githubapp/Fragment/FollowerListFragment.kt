package com.example.githubapp.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.Adapter.FollowerAdapter
import com.example.githubapp.Repository.GithubRepository
import com.example.githubapp.utils.Resource
import com.example.githubapp.Network.RetrofitInstance
import com.example.githubapp.ViewModel.GithubViewModel
import com.example.githubapp.ViewModel.ViewModelFactory
import com.example.githubapp.databinding.FragmentFollowerListBinding

class FollowerListFragment : Fragment() {

    private lateinit var binding: FragmentFollowerListBinding
    private val viewModel: GithubViewModel by viewModels { ViewModelFactory(GithubRepository(
        RetrofitInstance.api
    )) }

    private val adapter = FollowerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString("username")
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        if (username != null) {
            viewModel.getFollowers(username)

            viewModel.followers.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { followers ->
                            adapter.setFollowers(followers)
                        }
                    }
                    is Resource.Empty -> {

                        Toast.makeText(requireContext(), "No followers found", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (username != null) {
                viewModel.getFollowers(username)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}
