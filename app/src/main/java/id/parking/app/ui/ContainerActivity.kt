package id.parking.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.parking.app.Nav
import id.parking.app.databinding.ActivityContainerBinding
import id.parking.core.firestore.FDBApp
import id.parking.core.rdb.RDBApp

@AndroidEntryPoint
class ContainerActivity : AppCompatActivity() {

    private var binding: ActivityContainerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContainerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        FDBApp.init(this)
        RDBApp.init(this)
        Nav(this).startApp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}