package com.projects.melih.gistpub.view.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.components.HideSoftKeyboard
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.network.service.GitHubApi
import com.projects.melih.gistpub.view.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/**
 * Created by melih on 30/08/2017
 */

open class BasicAuthFragment : BaseFragment() {
    private var callStarredGistsNext: Call<ArrayList<Gist>>? = null
    private var callLogin: Call<Contributor>? = null
    private var editTextUsername: EditText? = null
    private var editTextPassword: EditText? = null

    override val menuRes: Int
        get() = Constants.NO_RES

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_basic_auth, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextUsername = view?.findViewById<EditText>(R.id.input_username) as EditText
        editTextPassword = view.findViewById<EditText>(R.id.input_password) as EditText

        view.findViewById<View>(R.id.login)?.setOnClickListener { v ->
            HideSoftKeyboard(v)
            val username = editTextUsername?.text.toString().trim { it <= ' ' }
            val password = editTextPassword?.text.toString()

            when {
                TextUtils.isEmpty(username) -> editTextUsername?.error = getString(R.string.warning_empty_username)
                TextUtils.isEmpty(password) -> editTextPassword?.error = getString(R.string.warning_empty_password)
                else -> {
                    showLoading(context.getString(R.string.pleaseWait))
                    callLogin = GitHubApi.getBasicAuthGitHubService(username, password).login()
                    callLogin?.enqueue(object : Callback<Contributor?> {
                        override fun onResponse(call: Call<Contributor?>?, response: Response<Contributor?>?) {
                            dismissLoading()
                            //TODO save user from DataManager
                            val data = response?.body()
                            callLogin = null

                            val intent = Intent()
                            if (data == null) {
                                showSnackBar(response?.message()!!)
                            } else {
                                intent.putExtra(getString(R.string.username_extra), data.login)
                                intent.putExtra(getString(R.string.password_extra), password)
                                activity.setResult(Activity.RESULT_OK, intent)
                                activity.finish()
                            }
                        }

                        override fun onFailure(call: Call<Contributor?>, t: Throwable) {
                            dismissLoading()
                            showSnackBar(t.message ?: "")
                            //swipeLayout?.isRefreshing = false
                        }
                    })
                }
            }
        }
    }

    override fun onDestroyView() {
        if (callStarredGistsNext != null) {
            callStarredGistsNext?.cancel()
        }
        if (callLogin != null) {
            callLogin?.cancel()
        }
        super.onDestroyView()
    }

    companion object {

        fun newInstance(): BasicAuthFragment {
            return BasicAuthFragment()
        }
    }
}
