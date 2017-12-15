package com.projects.melih.gistpub.view.gist

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDialogFragment
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.projects.melih.gistpub.R

import java.io.IOException
import java.io.InputStream

/**
 * Created by melih on 16/07/2017
 */

open class FileViewDialogFragment : AppCompatDialogFragment() {
    private var inputStream: InputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_DialogWhenLarge)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_file_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filename = arguments.getString(KEY_DESCRIPTION)
        val textViewFile = view?.findViewById<TextView>(R.id.fileText) as TextView
        val textViewDescription = view.findViewById<TextView>(R.id.fileDescription) as TextView
        if (TextUtils.isEmpty(filename)) {
            textViewDescription.visibility = View.GONE
        } else {
            textViewDescription.text = filename
        }

        val pollingThread = object : Thread() {
            override fun run() {
                try {
                    val fileReader = ByteArray(4096)
                    val stringBuilder = StringBuilder()
                    while (true) {
                        val read = inputStream?.read(fileReader)
                        if (read == -1) {
                            break
                        }

                        //TODO java.lang.OutOfMemoryError crash: probably it tries to hold the data on bundle to save instance
                        stringBuilder.append(String(fileReader, charset(charsetName = "UTF-8")))
                    }
                    activity.runOnUiThread { textViewFile.text = stringBuilder.toString() }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        pollingThread.start()

        this.dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()
            }
            true
        }

        /*
        this.getDialog().setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });
         */
    }

    companion object {
        val TAG = "FileViewDialogFragment"
        private val KEY_DESCRIPTION = "key_description"

        fun newInstance(inputStream: InputStream, filename: String): FileViewDialogFragment {
            val fragment = FileViewDialogFragment()
            fragment.inputStream = inputStream
            val bundle = Bundle()
            bundle.putString(KEY_DESCRIPTION, filename)
            fragment.arguments = bundle
            return fragment
        }
    }
}
