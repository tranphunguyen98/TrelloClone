package nguyen.trelloclone.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager.LayoutParams.*
import kotlinx.android.synthetic.main.activity_splash.*
import nguyen.trelloclone.R
import nguyen.trelloclone.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )

        val typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name.typeface = typeface

        Handler().postDelayed({
            if (FirestoreClass().getCurrentUserID().isNotEmpty()) {
                Log.d("SplashActivity", "not Empty")
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                Log.d("SplashActivity", "Empty")

                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish()
        }, 1500)
    }
}
