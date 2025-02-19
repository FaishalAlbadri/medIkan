package com.faishalbadri.penyakitikan.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.faishalbadri.penyakitikan.R
import com.faishalbadri.penyakitikan.databinding.ActivitySplashScreenBinding
import com.faishalbadri.penyakitikan.viewmodel.UserViewModel
import com.faishalbadri.penyakitikan.viewmodel.base.ViewModelFactory
import java.util.Timer
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val userViewModel: UserViewModel by viewModels { viewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(this)

        userViewModel.apply {
            getSessionUser().observe(this@SplashScreenActivity) {
                Timer("OpenLogin", false).schedule(3000) {
                    if (it.isLogin){
                        startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                    } else {
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    }
                    finish()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}