package com.projects.melih.gistpub.view.gist.starredGists

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.liveviewmodel.StarredGistsViewModel
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage
import com.projects.melih.gistpub.view.gist.BaseGistsFragment

/**
 * Created by melih on 13/08/2017
 */
open class StarredGistsFragment : BaseGistsFragment() {
    private lateinit var viewModel: StarredGistsViewModel

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(StarredGistsViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isEmptyLiveData.observe(this@StarredGistsFragment, Observer<Boolean> { isVisible ->
            emptyView.visibility = if (isVisible!!) View.VISIBLE else View.GONE
        })

        viewModel.isLoadingLiveData.observe(this@StarredGistsFragment, Observer<Boolean> {
            it?.let {
                swipeRefreshLayout.isRefreshing = it
            }
        })

        viewModel.errorLiveData.observe(this@StarredGistsFragment, Observer<ErrorMessage> {
            it?.let {
                showSnackBar(when(it.errorCode) {
                    ErrorCode.GENERAL_ERROR -> context.getString(R.string.unknown_error)
                    ErrorCode.NO_NETWORK -> context.getString(R.string.unconnected_network)
                })
            }
        })

        viewModel.gists.first?.observe(this@StarredGistsFragment, Observer { it -> gistAdapter.setList(it) })
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    companion object {
        fun newInstance(): StarredGistsFragment {
            return StarredGistsFragment()
        }
    }
}
