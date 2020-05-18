package nguyen.trelloclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_my_profile.*
import nguyen.trelloclone.R

class MyProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        toolbar_my_profile_activity.setNavigationIcon(R.drawable.ic_white_color_back_24dp)
        toolbar_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
