package com.projects.melih.gistpub.view.gist.follower

import android.arch.paging.PagedListAdapter
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.components.HideSoftKeyboard
import com.projects.melih.gistpub.extensions.ImageListener
import com.projects.melih.gistpub.extensions.loadImage
import com.projects.melih.gistpub.model.Contributor

/**
 * Created by melih on 12/08/2017
 */
open class FollowersAdapter(private val itemClickListener: ItemClickListener) : PagedListAdapter<Contributor, FollowersAdapter.Companion.FollowerViewHolder>(DIFF_CALLBACK) {
    //private var followers: ArrayList<Contributor>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false)
        return FollowerViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = getItem(position)
        if (follower != null) {
            holder.bindTo(follower)
        } else {
            TODO()
            //holder.clear()
        }
        /*if (position > CollectionUtils.size(followers) - Constants.PAGINATION_THRESHOLD) {
            itemClickListener.onLoadMore()
        }*/
    }

    /*fun setFollowers(followers: ArrayList<Contributor>) {
        this.followers = followers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return followers?.size!!
    }*/

    companion object {
        val DIFF_CALLBACK: DiffCallback<Contributor> = object : DiffCallback<Contributor>() {
            override fun areContentsTheSame(oldFollower: Contributor, newFollower: Contributor): Boolean {
                return oldFollower == newFollower
            }

            override fun areItemsTheSame(oldFollower: Contributor, newFollower: Contributor): Boolean {
                return oldFollower.id === newFollower.id
            }
        }

        class FollowerViewHolder(itemView: View, itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {
            private val textViewFollowerName: TextView = itemView.findViewById<TextView>(R.id.followerName) as TextView
            private val imageViewAvatar: ImageView = itemView.findViewById<ImageView>(R.id.avatarImage) as ImageView
            private val progressBar: ContentLoadingProgressBar = itemView.findViewById<ContentLoadingProgressBar>(R.id.progressBar) as ContentLoadingProgressBar
            private lateinit var follower: Contributor
            init {
                itemView.setOnClickListener { v ->
                    HideSoftKeyboard(v)
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onDetailItemClick(follower)
                    }
                }
            }

            fun bindTo(follower: Contributor) {
                this.follower = follower
                textViewFollowerName.text = follower.login
                progressBar.show()
                imageViewAvatar.loadImage(follower.avatarUrl, object :ImageListener {
                    override fun hide() {
                        progressBar.hide()
                    }
                })
            }
        }
    }

    interface ItemClickListener {
        fun onDetailItemClick(follower: Contributor)
    }
}
