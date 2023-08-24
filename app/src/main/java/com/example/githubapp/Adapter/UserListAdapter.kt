package com.example.githubapp.Adapter
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.githubapp.DataClass.SearchResult
import com.example.githubapp.DataClass.User
import com.example.githubapp.databinding.UserListItemBinding

class UserListAdapter(
    private var userList: MutableList<User>,
    private val onUserClickListener: (User) -> Unit
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    fun updateUsers(data: SearchResult?) {
        data?.let {
            this.userList.clear()
            this.userList.addAll(it.items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    inner class UserViewHolder(private val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {

                userName.text = user.login

                println("Avatar URL: ${user.avatarUrl}")

                Glide.with(userAvatar.context)
                    .load(user.avatarUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            e?.printStackTrace()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(userAvatar)
                itemView.setOnClickListener { onUserClickListener(user) }
            }
        }
    }
}