package com.projects.melih.gistpub.view

import android.Manifest
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.annotation.CallSuper
import android.support.annotation.MenuRes
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.webkit.MimeTypeMap

import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.model.File
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.network.service.GitHubApi
import com.projects.melih.gistpub.view.gist.FileViewDialogFragment

import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList

import okhttp3.ResponseBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by melih on 23/12/2016
 */

abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    protected var baseFragment: View? = null
    protected var gists: ArrayList<Gist>? = null
    protected lateinit var selectedFile: File
    protected var file: java.io.File? = null
    protected var navigationListener: NavigationListener? = null
    protected lateinit var responseBody: ResponseBody
    protected lateinit var fileName: String
    protected lateinit var username: String
    protected lateinit var rootView: View

    @get:MenuRes
    protected abstract val menuRes: Int

    private var progressDialog: ProgressDialog? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.navigationListener = context as NavigationListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (menuRes != Constants.NO_RES) {
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var animation: Animation? = super.onCreateAnimation(transit, enter, nextAnim)
        if (disableFragmentAnimations) {
            if (animation == null) {
                animation = object : Animation() {

                }
            }
            animation.duration = 0
        }
        return animation
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = activity.findViewById(R.id.container)

        //TODO("move user data to a data manager like net manager")
        val sharedPref = context?.getSharedPreferences(context?.getString(R.string.preference_user_key), Context.MODE_PRIVATE)
        username = sharedPref?.getString(context?.getString(R.string.username_extra), "") ?: ""
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menuRes != Constants.NO_RES) {
            inflater?.inflate(menuRes, menu)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navigationListener = null
    }

    protected fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }

    protected fun showSnackBar(message: String, actionMessage: String, listener: View.OnClickListener) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).setAction(actionMessage, listener).show()
    }

    private fun openFolder(file: java.io.File?) {
        //TODO("clear download manager notification")
        if (file != null) {
            val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
            startActivity(intent)
        }
    }

    protected fun downloadFile(writeToDisk: Boolean) {
        val call = GitHubApi.getGitHubService(context).downloadFileWithDynamicUrlSync(selectedFile.rawUrl)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if (writeToDisk) {
                        responseBody = response.body()!!
                        fileName = selectedFile.filename
                        writeResponseBodyToDisk()
                    } else {
                        openFile(response.body()?.byteStream()!!, selectedFile)
                    }
                } else {
                    showSnackBar(getString(R.string.unknown_error))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showSnackBar(t.message ?: "")
            }
        })
    }

    @AfterPermissionGranted(RC_WRITE_EXTERNAL_STORAGE)
    private fun writeResponseBodyToDisk() {
        var isSuccess = false
        if (EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            try {
                file = java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + java.io.File.separator + fileName.trim { it <= ' ' } + ".txt")
                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try {
                    val fileReader = ByteArray(4096)
                    val fileSize = responseBody.contentLength()
                    var fileSizeDownloaded: Long = 0

                    inputStream = responseBody.byteStream()
                    outputStream = FileOutputStream(file!!)
                    while (true) {
                        val read = inputStream?.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(fileReader, 0, read!!)
                        fileSizeDownloaded += read.toLong()
                        Log.d("melo", "file download: $fileSizeDownloaded of $fileSize")
                    }
                    outputStream.flush()
                    isSuccess = true
                } catch (e: IOException) {
                    isSuccess = false
                } finally {
                    if (inputStream != null) {
                        inputStream.close()
                    }
                    if (outputStream != null) {
                        outputStream.close()
                    }
                }
            } catch (e: IOException) {
                isSuccess = false
            } finally {
                val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.addCompletedDownload(file!!.name, file!!.name, true, getMimeType(Uri.fromFile(file).toString()), file!!.absolutePath, file!!.length(), true)

                showSnackBar(if (isSuccess) getString(R.string.success_file_download) else getString(R.string.error_file_download), getString(R.string.goToLocation), View.OnClickListener { openFolder(file) })
            }
        } else {
            EasyPermissions.requestPermissions(this@BaseFragment, getString(R.string.permission_write_external_storage_rationale), RC_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    protected fun openFile(inputStream: InputStream, file: File) {
        showFragmentDialog(FileViewDialogFragment.newInstance(inputStream, file.filename), FileViewDialogFragment.TAG)
    }

    protected fun showLoading(message: String) {
        val activity = activity ?: return
        if (!isVisible && activity.isFinishing) {
            return
        }
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog?.setCancelable(false)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog?.setMessage(if (TextUtils.isEmpty(message)) getString(R.string.pleaseWait) else message)
            progressDialog?.show()
        }
    }

    protected fun dismissLoading() {
        val activity = activity ?: return
        if (!isVisible && activity.isFinishing || progressDialog == null) {
            return
        }
        progressDialog?.isShowing!!.let { progressDialog?.dismiss() }
    }

    protected fun showFragmentDialog(dialogFragment: DialogFragment, tag: String) {
        val activity = activity ?: return
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            fragmentTransaction.remove(fragment)
        }
        fragmentTransaction.addToBackStack(null)
        dialogFragment.show(fragmentTransaction, tag)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this@BaseFragment).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            downloadFile(true)
        }
    }

    companion object {
        var disableFragmentAnimations: Boolean = false

        public val TAG = "BaseFragment"
        const val RC_WRITE_EXTERNAL_STORAGE = 122

        fun getMimeType(url: String): String {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type ?: ""
        }
    }
}
