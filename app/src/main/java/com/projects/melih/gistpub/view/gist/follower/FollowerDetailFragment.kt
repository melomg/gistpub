package com.projects.melih.gistpub.view.gist.follower

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.melih.gistpub.R

import com.projects.melih.gistpub.liveviewmodel.FollowerGistsViewModel
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage
import com.projects.melih.gistpub.view.gist.BaseGistsFragment

open class FollowerDetailFragment : BaseGistsFragment() {
    private lateinit var follower: Contributor
    private lateinit var viewModel: FollowerGistsViewModel


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(FollowerGistsViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        follower = arguments.getParcelable(KEY_FOLLOWER)

        viewModel.isEmptyLiveData.observe(this@FollowerDetailFragment, Observer<Boolean> { isVisible ->
            emptyView.visibility = if (isVisible!!) View.VISIBLE else View.GONE
        })

        viewModel.isLoadingLiveData.observe(this@FollowerDetailFragment, Observer<Boolean> {
            it?.let {
                swipeRefreshLayout.isRefreshing = it
            }
        })

        viewModel.errorLiveData.observe(this@FollowerDetailFragment, Observer<ErrorMessage> {
            it?.let {
                showSnackBar(when(it.errorCode) {
                    ErrorCode.GENERAL_ERROR -> context.getString(R.string.unknown_error)
                    ErrorCode.NO_NETWORK -> context.getString(R.string.unconnected_network)
                })
            }
        })

        viewModel.setFollowerName(follower.login)
        viewModel.gists.first?.observe(this@FollowerDetailFragment, Observer { it -> gistAdapter.setList(it) })
    }

    override fun onRefresh() {
        viewModel.setFollowerName(follower.login)
    }

    companion object {
        private val KEY_FOLLOWER = "key_follower"

        fun newInstance(follower: Contributor): FollowerDetailFragment {
            val fragment = FollowerDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_FOLLOWER, follower)
            fragment.arguments = bundle
            return fragment
        }
    }
}
