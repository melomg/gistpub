package com.projects.melih.gistpub.view.gist.follower

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.liveviewmodel.FollowersViewModel
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage
import com.projects.melih.gistpub.view.BaseFragment

/**
 * Created by melih on 12/08/2017
 */
open class FollowersFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var viewModel: FollowersViewModel
    private lateinit var emptyView: View
    private lateinit var followerAdapter: FollowersAdapter

    override val menuRes: Int
        get() = Constants.NO_RES

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FollowersViewModel::class.java)
        return inflater?.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyView = view!!.findViewById(R.id.emptyView)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        followerAdapter = FollowersAdapter(object : FollowersAdapter.ItemClickListener {
            override fun onDetailItemClick(follower: Contributor) {
                navigationListener?.openFragment(FollowerDetailFragment.newInstance(follower), null)
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = followerAdapter
        }

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.orange, R.color.green, R.color.blue)
            setOnRefreshListener(this@FollowersFragment)
            post {
                isRefreshing = true
                onRefresh()
            }
        }

        viewModel.isEmptyLiveData.observe(this@FollowersFragment, Observer<Boolean> { isVisible ->
            emptyView.visibility = if (isVisible!!) View.VISIBLE else View.GONE
        })
        viewModel.isLoadingLiveData.observe(this@FollowersFragment, Observer<Boolean> {
            it?.let {
                swipeRefreshLayout.isRefreshing = it
            }
        })
        viewModel.errorLiveData.observe(this@FollowersFragment, Observer<ErrorMessage> {
            it?.let {
                showSnackBar(when (it.errorCode) {
                    ErrorCode.GENERAL_ERROR -> context.getString(R.string.unknown_error)
                    ErrorCode.NO_NETWORK -> context.getString(R.string.unconnected_network)
                })
            }
        })

        viewModel.setUserName(username)
        viewModel.followers.first?.observe(this@FollowersFragment, Observer(followerAdapter::setList))
    }

    override fun onRefresh() {
        viewModel.setUserName(username)
    }

    companion object {
        fun newInstance(): FollowersFragment {
            return FollowersFragment()
        }
    }
}
