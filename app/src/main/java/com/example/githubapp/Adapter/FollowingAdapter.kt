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

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private var followings: List<User> = emptyList()

    class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followingName: TextView = itemView.findViewById(R.id.following_name)
        val profileImage: ImageView = itemView.findViewById(R.id.following_profile_image)
        val followButton: Button = itemView.findViewById(R.id.unfollow_button)

        fun bind(following: User) {
            followingName.text = following.login

            Glide.with(profileImage.context)
                .load(following.avatarUrl)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.following_item, parent, false)
        return FollowingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val following = followings[position]
        holder.bind(following)
    }

    override fun getItemCount(): Int = followings.size

    fun setFollowings(newFollowings: List<User>) {
        followings = newFollowings
        notifyDataSetChanged()
    }
}