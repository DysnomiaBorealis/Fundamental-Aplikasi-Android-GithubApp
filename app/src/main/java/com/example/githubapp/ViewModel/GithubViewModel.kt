package com.example.githubapp.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.Repository.GithubRepository
import com.example.githubapp.utils.Resource
import com.example.githubapp.DataClass.SearchResult
import com.example.githubapp.DataClass.User
import com.example.githubapp.DataClass.UserDetail
import kotlinx.coroutines.launch
import retrofit2.Response


class GithubViewModel(private val repository: GithubRepository) : ViewModel() {
    private val _searchResult = MutableLiveData<Resource<SearchResult>>()
    val searchResult: LiveData<Resource<SearchResult>> = _searchResult

    private val _userDetail = MutableLiveData<Resource<UserDetail>>()
    val userDetail: LiveData<Resource<UserDetail>> = _userDetail

    private val _followers = MutableLiveData<Resource<List<User>>>()
    val followers: LiveData<Resource<List<User>>> = _followers

    private val _following = MutableLiveData<Resource<List<User>>>()
    val following: LiveData<Resource<List<User>>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun searchUsers(query: String) {
        fetchData(_isLoading, _searchResult) { repository.searchUsers(query) }
    }

    fun getUserDetail(username: String) {
        fetchData(_isLoading, _userDetail) { repository.getUserDetail(username) }
    }

    fun getFollowers(username: String) {
        fetchData(_isLoading, _followers) { repository.getFollowers(username) }
    }

    fun getFollowing(username: String) {
        fetchData(_isLoading, _following) { repository.getFollowing(username) }
    }

    private fun <T> fetchData(
        loading: MutableLiveData<Boolean>,
        result: MutableLiveData<Resource<T>>,
        apiCall: suspend () -> Response<T>
    ) = viewModelScope.launch {
        loading.value = true
        result.postValue(Resource.Loading())
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                if (response.body() is SearchResult && (response.body() as SearchResult).items.isEmpty()) {
                    result.postValue(Resource.Empty())
                } else {
                    result.postValue(Resource.Success(response.body()))
                }
            } else {
                result.postValue(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            result.postValue(Resource.Error(e.localizedMessage ?: "An error occurred"))
        } finally {
            loading.value = false
        }
    }

}
