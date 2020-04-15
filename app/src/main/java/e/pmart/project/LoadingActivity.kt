package e.pmart.project

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
        finish()
    }
}
