package com.projects.melih.gistpub.view.gist

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.components.HideSoftKeyboard
import com.projects.melih.gistpub.model.File
import com.projects.melih.gistpub.model.Gist

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Arrays
import java.util.regex.Pattern

/**
 * Created by melih on 11/07/2017
 */

open class GistsAdapter : PagedListAdapter<Gist, GistsAdapter.Companion.GistViewHolder> {
    //private var gists: ArrayList<Gist>? = null

    private val itemClickListener: ItemClickListener

    constructor(itemClickListener: ItemClickListener) : super(DIFF_CALLBACK) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_gist, parent, false)
        return GistViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: GistViewHolder, position: Int) {
        val gist: Gist? = getItem(position)
        if (gist != null) {
            holder.bindTo(gist)
        }
        /*if (position > CollectionUtils.size(gists) - Constants.PAGINATION_THRESHOLD) {
            itemClickListener.onLoadMore()
        }*/
    }

    /*fun setGists(gists: ArrayList<Gist>?) {
        this.gists = gists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return gists?.size!!
    }*/

    companion object {
        val DIFF_CALLBACK: DiffCallback<Gist> = object : DiffCallback<Gist>() {
            override fun areContentsTheSame(oldGist: Gist, newGist: Gist): Boolean {
                return oldGist == newGist
            }

            override fun areItemsTheSame(oldGist: Gist, newGist: Gist): Boolean {
                return oldGist.id === newGist.id
            }
        }


        class GistViewHolder(itemView: View, itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

            private lateinit var selectedGist: Gist
            private val fileTileAdapter: FileTileAdapter
            private val viewDescriptionArea: View = itemView.findViewById<View>(R.id.descriptionArea)
            private val textViewDescription: TextView = itemView.findViewById<TextView>(R.id.description)
            private val textViewCreatedDate: TextView = itemView.findViewById<TextView>(R.id.createdDate)
            private val recyclerView: RecyclerView = itemView.findViewById<RecyclerView>(R.id.filesRecyclerView)
            private var files: ArrayList<File>? = null

            init {
                val layoutManager = FlexboxLayoutManager(itemView.context)
                layoutManager.apply {
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }
                recyclerView.layoutManager = layoutManager
                fileTileAdapter = FileTileAdapter(object : FileTileAdapter.FileItemClickListener {
                    override fun onFileItemClick(selectedFile: File) {
                        val gistPosition = adapterPosition
                        if (gistPosition != RecyclerView.NO_POSITION) {
                            itemClickListener.onFileItemClick(selectedGist, selectedFile)
                        }
                    }

                    override fun onFileItemLongClick(selectedFile: File) {
                        val gistPosition = adapterPosition
                        if (gistPosition != RecyclerView.NO_POSITION) {
                            itemClickListener.onFileItemLongClick(selectedGist, selectedFile)
                        }
                    }
                })
                recyclerView.adapter = fileTileAdapter

                itemView.findViewById<View>(R.id.goToDetail).setOnClickListener { v ->
                    HideSoftKeyboard(v)
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onDetailItemClick(selectedGist)
                    }
                }
            }

            fun bindTo(gist: Gist?) {
                if (gist != null) {
                    selectedGist = gist
                    val description = gist.description
                    if (!TextUtils.isEmpty(description)) {
                        textViewDescription.text = gist.description
                        viewDescriptionArea.visibility = View.VISIBLE
                    } else {
                        viewDescriptionArea.visibility = View.GONE
                    }
                    files = getFiles(gist)
                    gist.rawData = files
                    fileTileAdapter.setFiles(files)
                    val inputFormat = SimpleDateFormat(Constants.DATE_SERVICE_PATTERN, java.util.Locale.getDefault())
                    val outputFormat = SimpleDateFormat(Constants.DATE_VIEW_PATTERN, java.util.Locale.getDefault())
                    try {
                        val date = inputFormat.parse(gist.createdAt)
                        textViewCreatedDate.text = outputFormat.format(date)
                    } catch (ignoredException: ParseException) {
                        textViewCreatedDate.text = ""
                    }
                }
            }

            private fun getFiles(selectedGist: Gist): ArrayList<File> {
                val files = ArrayList<File>()
                if (selectedGist.filesObject != null) {
                    val data = selectedGist.filesObject.toString()
                    val halfJson = removeLastChar(data)
                    val half1Json = removeFirstChar(halfJson)
                    val unParsedFilesData = Arrays.asList(*half1Json.split(Pattern.quote("},").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                    for (unparsedFileData in unParsedFilesData) {
                        val halfUnParsedJson = removeLastChar(unparsedFileData)
                        val correctJson = halfUnParsedJson.substring(halfUnParsedJson.indexOf("={") + 2)
                        val fileData = Arrays.asList(*correctJson.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                        val file = File()
                        for (part in fileData) {
                            val property = part.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            val propertyValue = property[1]
                            when (property[0].trim { it <= ' ' }) {
                                "filename" -> {
                                    file.filename = propertyValue
                                }
                                "type" -> {
                                    file.type = propertyValue
                                }
                                "language" -> {
                                    file.language = propertyValue
                                }
                                "raw_url" -> {
                                    file.rawUrl = propertyValue
                                }
                                "size" -> {
                                    file.size = propertyValue
                                }
                            }
                        }

                        files.add(file)
                    }
                }
                return files
            }

            private fun removeLastChar(str: String?): String {
                var str = str
                if (str != null && str.isNotEmpty() && str[str.length - 1] == '}') {
                    str = str.substring(0, str.length - 1)
                }
                return str ?: ""
            }

            private fun removeFirstChar(str: String?): String {
                var str = str
                if (str != null && str.isNotEmpty() && str[0] == '{') {
                    str = str.substring(1, str.length)
                }
                return str ?: ""
            }
        }
    }

    interface ItemClickListener {
        fun onDetailItemClick(selectedGist: Gist)

        fun onFileItemClick(selectedGist: Gist, selectedFile: File)

        fun onFileItemLongClick(selectedGist: Gist, selectedFile: File)

        //fun onLoadMore()
    }
}