package com.example.androidfinal

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.*

class EditUser : AppCompatActivity(), View.OnClickListener {
    private var mBtnCancel: Button? = null
    private var mBtnDone: Button? = null
    private var mTvNameUser: EditText? = null
    private var mTvDateUser: EditText? = null
    private var mTvSexUser: EditText? = null
    private var mTvEmailUser: EditText? = null
    private var mCalendar: Calendar? = null
    private var mImgUser: ImageView? = null
    private var mCurrentPhotoPath: String? = null
    private var mImageBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        init()
        mBtnCancel!!.setOnClickListener(this as View.OnClickListener)
        mBtnDone!!.setOnClickListener(this as View.OnClickListener)
        mTvDateUser!!.setOnClickListener(this as View.OnClickListener)
        mImgUser!!.setOnClickListener(this)

//        Gson gson = new Gson();
//        String json = mSharedPreferencesUser.getString(KEY_SHARE_SAVE_DATA, "");
//        User user = gson.fromJson(json, User.class);
    }

    private fun init() {
        mBtnCancel = findViewById(R.id.activity_edit_user_btn_cancel)
        mBtnDone = findViewById(R.id.activity_edit_user_btn_done)
        mTvNameUser = findViewById(R.id.activity_edit_user_tv_name_user)
        mTvDateUser = findViewById(R.id.activity_edit_user_tv_date_user)
        mTvSexUser = findViewById(R.id.activity_edit_user_tv_sex_user)
        mTvEmailUser = findViewById(R.id.activity_edit_user_tv_email_user)
        mImgUser = findViewById(R.id.activity_edit_user_img)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.activity_edit_user_btn_cancel -> {
                finish()
                clickBtnDone()
            }
            R.id.activity_edit_user_btn_done -> clickBtnDone()
            R.id.activity_edit_user_img -> clickImgAvatar()
            R.id.activity_edit_user_tv_date_user -> clickTvDate()
            else -> {
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val imageFileName = "picture"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",  // suffix
            storageDir // directory
        )
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    Uri.parse(mCurrentPhotoPath)
                )
                mImgUser!!.setImageBitmap(mImageBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun clickBtnDone() {
        if (mTvNameUser != null && mTvDateUser != null && mTvEmailUser != null && mTvSexUser != null) {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] b = baos.toByteArray();
//                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            val editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit()
            editor.putString("name_user", mTvNameUser!!.text.toString())
            editor.putString("sex_user", mTvSexUser!!.text.toString())
            editor.putString("email_user", mTvEmailUser!!.text.toString())
            editor.putString("date_user", mTvDateUser!!.text.toString())
            editor.commit()
            Toast.makeText(this, "Edit user completed !!!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please fill in all the information!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clickTvDate() {
        mCalendar = Calendar.getInstance()
        val mYear = mCalendar?.get(Calendar.YEAR)
        val mMonth = mCalendar?.get(Calendar.MONTH)
        val mDay = mCalendar?.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this@EditUser,
            { _, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
                mTvDateUser!!.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            }, mYear!!, mMonth!!, mDay!!
        )
        datePickerDialog.show()
    }

    private fun clickImgAvatar() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            Log.i("TAG", "IOException")
        }
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this@EditUser, applicationContext.packageName + ".provider",
                photoFile!!
            )
        )
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    companion object {
        const val KEY_SHARE_SAVE_DATA = "MyObjectUser"
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}