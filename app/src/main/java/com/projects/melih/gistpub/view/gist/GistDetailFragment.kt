package com.projects.melih.gistpub.view.gist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.view.BaseFragment

open class GistDetailFragment : BaseFragment() {
    private var gist: Gist? = null

    override val menuRes: Int
        get() = Constants.NO_RES

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_gist_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        gist = arguments.getParcelable(KEY_GIST)
    }

    companion object {
        private val KEY_GIST = "key_gist"

        fun newInstance(gist: Gist): GistDetailFragment {
            val fragment = GistDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_GIST, gist)
            fragment.arguments = bundle
            return fragment
        }
    }
}
