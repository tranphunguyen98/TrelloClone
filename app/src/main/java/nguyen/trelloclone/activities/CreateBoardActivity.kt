package nguyen.trelloclone.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import nguyen.trelloclone.R
import nguyen.trelloclone.firebase.FirestoreClass
import nguyen.trelloclone.models.Board
import nguyen.trelloclone.utils.Constants
import nguyen.trelloclone.utils.Utils
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var selectedImageFileUri: Uri? = null
    private lateinit var userName: String
    private var boardImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        userName = if (intent.hasExtra(Constants.NAME)) {
            intent.getStringExtra(Constants.NAME)!!
        } else {
            ""
        }

        setUpActionBar()

        iv_board_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Utils.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        btn_create.setOnClickListener {
            if (selectedImageFileUri != null) {
                uploadBoardImage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))

                createBoardData()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun uploadBoardImage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        selectedImageFileUri?.also {
            val sRef: StorageReference =
                FirebaseStorage
                    .getInstance()
                    .reference
                    .child(
                        "BOARD_IMAGE" + System.currentTimeMillis() + "." + Utils.getFileExtension(
                            this,
                            it
                        )
                    )

            sRef.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    hideProgressDialog()
                    taskSnapshot.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())
                            boardImageUrl = uri.toString()

                            createBoardData()
                        }
                        ?.addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                exception.message,
                                Toast.LENGTH_LONG
                            ).show()

                            hideProgressDialog()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()

                    hideProgressDialog()
                }
        }
    }

    private fun createBoardData() {
        val assignedTo: ArrayList<String> = ArrayList()
        assignedTo.add(FirestoreClass().getCurrentUserID())

        val board = Board(et_board_name.text.toString(), boardImageUrl, userName, assignedTo)

        FirestoreClass().createBoard(this,board)
    }

    fun createBoardSuccessful() {
        Toast.makeText(this,"create successful", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {
            selectedImageFileUri = data.data

            try {
                Glide
                    .with(this)
                    .load(Uri.parse(selectedImageFileUri.toString()))
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(iv_board_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_create_board_activity)
        supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            it.title = resources.getString(R.string.create_board_title)
        }

        toolbar_create_board_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}
