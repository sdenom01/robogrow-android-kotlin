package io.robogrow.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import io.robogrow.R
import io.robogrow.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels.toFloat()

        val llWrapper: LinearLayout = findViewById(R.id.ll_wrapper)
        val springAnim = SpringAnimation(llWrapper, DynamicAnimation.TRANSLATION_Y)
        springAnim.setStartValue(height)

        val spring = SpringForce()
        spring.finalPosition = 0f
        spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
        spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY

        springAnim.addEndListener { _, _, _, _ ->
            Handler().postDelayed(
                {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java).apply {}
                    startActivity(intent)
                },500
            )
        }

        springAnim.spring = spring
        springAnim.start()
    }
}
