package nguyen.trelloclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setUpToolBar()
    }

    private fun setUpToolBar() {
        setSupportActionBar(toolbar_sign_up_activity)
        supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back_color_black_24dp)
        }
        
        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}
