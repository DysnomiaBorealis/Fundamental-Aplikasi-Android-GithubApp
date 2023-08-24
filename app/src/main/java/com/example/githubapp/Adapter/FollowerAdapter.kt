package com.example.githubapp.Adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.githubapp.R
import com.example.githubapp.DataClass.User

class FollowerAdapter : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {

    private var followers: List<User> = emptyList()

    class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followerName: TextView = itemView.findViewById(R.id.follower_name)
        val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        val followButton: Button = itemView.findViewById(R.id.follow_button)

        fun bind(follower: User) {
            followerName.text = follower.login

            Glide.with(profileImage.context)
                .load(follower.avatarUrl)
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
                .into(profileImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.follower_item, parent, false)
        return FollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = followers[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int = followers.size

    fun setFollowers(newFollowers: List<User>) {
        followers = newFollowers
        notifyDataSetChanged()
    }
}


