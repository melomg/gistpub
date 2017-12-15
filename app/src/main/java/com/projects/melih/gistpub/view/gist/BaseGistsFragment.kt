package com.projects.melih.gistpub.view.gist

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.model.File
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.view.BaseFragment
import java.util.ArrayList

/**
 * Created by melih on 15.10.2017
 */
abstract class BaseGistsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    protected lateinit var gistAdapter: GistsAdapter
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var emptyView: View

    abstract override fun onRefresh()

    override val menuRes: Int
        get() = Constants.NO_RES

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_gists, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gists = ArrayList()
        val recyclerView: RecyclerView = view!!.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        emptyView = view.findViewById(R.id.emptyView)

        gistAdapter = GistsAdapter(object : GistsAdapter.ItemClickListener {
            override fun onDetailItemClick(selectedGist: Gist) {
                //TODO dont call gists remove it.
                navigationListener?.openFragment(GistDetailFragment.newInstance(selectedGist), null)
            }

            override fun onFileItemClick(selectedGist: Gist, selectedFile: File) {
                this@BaseGistsFragment.selectedFile = selectedFile
                downloadFile(false)
            }

            override fun onFileItemLongClick(selectedGist: Gist, selectedFile: File) {
                this@BaseGistsFragment.selectedFile = selectedFile
                downloadFile(true)
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = gistAdapter
        }

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.orange, R.color.green, R.color.blue)
            setOnRefreshListener(this@BaseGistsFragment)
            //TODO delete this post
            post {
                isRefreshing = true
                onRefresh()
            }
        }
    }
}
