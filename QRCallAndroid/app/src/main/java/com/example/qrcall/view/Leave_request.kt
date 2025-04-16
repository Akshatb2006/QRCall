package com.example.qrcall.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcall.R

class Leave_request : AppCompatActivity() {

    private lateinit var tvAttachFile: TextView
    private lateinit var tvFileName: TextView

    private val FILE_PICKER_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_request)

        tvAttachFile = findViewById(R.id.attach_file)
        tvFileName = findViewById(R.id.tv_file_name)

        tvAttachFile.setOnClickListener {
            openFilePicker()
        }
    }
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select File"), FILE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val fileName = getFileName(uri)
                tvFileName.text = fileName ?: "File selected"
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                if (it.moveToFirst()) {
                    result = it.getString(displayNameIndex)
                }
            } else {
                result = "Unknown file"
            }
        }
        return result
    }
}
