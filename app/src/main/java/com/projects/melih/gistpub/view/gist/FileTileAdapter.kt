package com.projects.melih.gistpub.view.gist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.components.HideSoftKeyboard
import com.projects.melih.gistpub.model.File

import java.util.ArrayList

/**
 * Created by melih on 15/07/2017
 */

open class FileTileAdapter(private val fileItemClickListener: FileItemClickListener) : RecyclerView.Adapter<FileTileAdapter.Companion.FileTileViewHolder>() {
    private var files: ArrayList<File>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileTileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileTileViewHolder(view, fileItemClickListener)
    }

    override fun onBindViewHolder(holder: FileTileViewHolder, position: Int) {
        holder.bindTo(files!![position])
    }

    fun setFiles(files: ArrayList<File>?) {
        this.files = files
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return files?.size!!
    }

    companion object {
        class FileTileViewHolder(itemView: View, fileItemClickListener: FileItemClickListener) : RecyclerView.ViewHolder(itemView) {
            private lateinit var selectedFile: File
            var buttonFile: Button = itemView.findViewById<Button>(R.id.button_file) as Button

            init {
                buttonFile.setOnClickListener { v ->
                    HideSoftKeyboard(v)
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        fileItemClickListener.onFileItemClick(selectedFile)
                    }
                }
                buttonFile.setOnLongClickListener { v ->
                    HideSoftKeyboard(v)
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        fileItemClickListener.onFileItemLongClick(selectedFile)
                    }
                    true
                }
            }

            fun bindTo(file: File) {
                selectedFile = file
                buttonFile.text = file.filename
            }
        }
    }

    interface FileItemClickListener {
        fun onFileItemClick(selectedFile: File)

        fun onFileItemLongClick(selectedFile: File)
    }

}

