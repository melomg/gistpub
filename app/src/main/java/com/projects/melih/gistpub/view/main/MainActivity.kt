package com.projects.melih.gistpub.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.components.HideSoftKeyboard
import com.projects.melih.gistpub.view.user.LoginActivity
import android.support.v7.app.AlertDialog
import android.view.View
import com.projects.melih.gistpub.view.BaseActivity
import com.projects.melih.gistpub.view.NavigationObserver


open class MainActivity : BaseActivity() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootView = findViewById<View>(R.id.container)
        findViewById<View>(R.id.toolbar_action).setOnClickListener { v ->
            HideSoftKeyboard(v)
            logout()
        }

        val sharedPref = getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE)
        val username = sharedPref.getString(getString(R.string.username_extra), null)
        if (TextUtils.isEmpty(username)) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivityForResult(intent, USER_REQUEST)
        } else {
            openFragmentAndClearStack(HomeFragment.newInstance(), NavigationObserver.Companion.NONE)
        }
    }

    fun logout() {
        if (isFinishing) {
            return
        }
        AlertDialog.Builder(this@MainActivity)
                .setMessage(R.string.are_you_sure_to_logout)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, { _, _ ->
                    val sharedPref = getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.remove(getString(R.string.username_extra))
                    editor.remove(getString(R.string.password_extra))
                    editor.apply()

                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivityForResult(intent, USER_REQUEST)
                })
                .setNegativeButton(R.string.cancel, { dialog, _ ->
                    dialog.dismiss()
                })
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == USER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val username = data?.getStringExtra(getString(R.string.username_extra))
                val password = data?.getStringExtra(getString(R.string.password_extra))
                val sharedPref = getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString(getString(R.string.username_extra), username)
                editor.putString(getString(R.string.password_extra), password)
                editor.apply()
                openFragmentAndClearStack(HomeFragment.newInstance(), NavigationObserver.Companion.NONE)
            } else {
                finish()
            }
        }
    }

    companion object {
        internal val USER_REQUEST = 1
    }
}
