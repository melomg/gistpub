package com.projects.melih.gistpub.view.gist.yourGists

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.melih.gistpub.R

import com.projects.melih.gistpub.liveviewmodel.YourGistsViewModel
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage
import com.projects.melih.gistpub.view.gist.BaseGistsFragment

/**
 * Created by melih on 13/08/2017
 */

open class YourGistsFragment : BaseGistsFragment() {
    private lateinit var viewModel: YourGistsViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(YourGistsViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isEmptyLiveData.observe(this@YourGistsFragment, Observer<Boolean> { isVisible ->
            emptyView.visibility = if (isVisible!!) View.VISIBLE else View.GONE
        })

        viewModel.isLoadingLiveData.observe(this@YourGistsFragment, Observer<Boolean> {
            it?.let {
                swipeRefreshLayout.isRefreshing = it
            }
        })

        viewModel.errorLiveData.observe(this@YourGistsFragment, Observer<ErrorMessage> {
            it?.let {
                showSnackBar(when (it.errorCode) {
                    ErrorCode.GENERAL_ERROR -> context.getString(R.string.unknown_error)
                    ErrorCode.NO_NETWORK -> context.getString(R.string.unconnected_network)
                })
            }
        })

        viewModel.setUserName(username)
        viewModel.gists.first?.observe(this@YourGistsFragment, Observer<PagedList<Gist>> {
            it?.let { gistAdapter.setList(it) }
        })
    }


    override fun onRefresh() {
        viewModel.setUserName(username)
    }

    companion object {
        fun newInstance(): YourGistsFragment {
            return YourGistsFragment()
        }
    }
}