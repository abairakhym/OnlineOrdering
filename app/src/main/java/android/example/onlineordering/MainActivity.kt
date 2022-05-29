package android.example.onlineordering

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var detailFragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        detailFragment = DetailFragment()
    }

    fun onClickArbuz(view: View) {
        detailFragment.show(supportFragmentManager, "DetailFragment")
    }
}