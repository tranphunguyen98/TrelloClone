package nguyen.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import nguyen.trelloclone.R
import nguyen.trelloclone.adapters.BoardItemsAdapter
import nguyen.trelloclone.firebase.FirestoreClass
import nguyen.trelloclone.models.Board
import nguyen.trelloclone.models.User
import nguyen.trelloclone.utils.Constants

class MainActivity : BaseActivity() {
    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
    }

    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirestoreClass().loadUserData(this)
        FirestoreClass().getBoardsList(this)
        setUpActionBar()

        nav_view.setNavigationItemSelectedListener {menu ->
            when(menu.itemId) {
                R.id.nav_my_profile -> {
                    startActivityForResult(Intent(this,MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
                }
                R.id.nav_sign_out -> {
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }

        fab_create_board.setOnClickListener {
            val intent = Intent(this,CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, userName)

            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE) {
            FirestoreClass().loadUserData(this)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    fun populateBoardsListToUI(listBoard: List<Board>) {
        if(listBoard.isNotEmpty()) {
            with(rv_boards_list) {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = BoardItemsAdapter(listBoard)
                hasFixedSize()
            }
            tv_no_boards_available.visibility = View.GONE
        } else {
            rv_boards_list.visibility = View.GONE
            tv_no_boards_available.visibility = View.VISIBLE
        }
    }

    fun updateNavigationUserDetails(user: User) {
        userName = user.name

        Glide.with(this@MainActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_user_image)

        tv_username.text = user.name
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu_24dp)
        toolbar_main_activity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }
}
