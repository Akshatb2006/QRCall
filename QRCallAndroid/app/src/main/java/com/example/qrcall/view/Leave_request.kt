package com.example.qrcall.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcall.R
import java.util.Calendar

class Leave_request : AppCompatActivity() {

    private lateinit var tvAttachFile: TextView
    private lateinit var tvFileName: TextView
    private lateinit var fromDateEditText: EditText
    private lateinit var toDateEditText: EditText

    private val FILE_PICKER_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_request)

        fromDateEditText = findViewById(R.id.from_date)
        toDateEditText = findViewById(R.id.to_date)
        tvAttachFile = findViewById(R.id.attach_file)
        tvFileName = findViewById(R.id.tv_file_name)

        val calendar = Calendar.getInstance()

        val fromDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            fromDateEditText.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
        }

        val toDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            toDateEditText.setText(String.format("%02d/%02d/%04d", day, month + 1, year))
        }

        fromDateEditText.setOnClickListener {
            DatePickerDialog(this, fromDatePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        toDateEditText.setOnClickListener {
            DatePickerDialog(this, toDatePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

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
