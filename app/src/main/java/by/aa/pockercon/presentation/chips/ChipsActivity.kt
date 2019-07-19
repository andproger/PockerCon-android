package by.aa.pockercon.presentation.chips

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import by.aa.pockercon.R
import kotlinx.android.synthetic.main.activity_main.*

class ChipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chips)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}
