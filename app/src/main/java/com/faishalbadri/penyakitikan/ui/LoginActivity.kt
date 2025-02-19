package com.faishalbadri.penyakitikan.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.faishalbadri.penyakitikan.R
import com.faishalbadri.penyakitikan.data.SessionData
import com.faishalbadri.penyakitikan.databinding.ActivityLoginBinding
import com.faishalbadri.penyakitikan.util.createAlertDialog
import com.faishalbadri.penyakitikan.util.htmlStringFormat
import com.faishalbadri.penyakitikan.viewmodel.UserViewModel
import com.faishalbadri.penyakitikan.viewmodel.base.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val userViewModel: UserViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            btnRegister.apply {
                text = htmlStringFormat(this@LoginActivity, "Sudah Punya Akun?", "Masuk")
                setOnClickListener {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }

            btnLogin.setOnClickListener {
                if (edtEmail.length() == 0 || edtPassword.length() < 8) {
                    if (edtEmail.length() == 0) {
                        edtEmail.error = getString(R.string.error_field)
                    }
                    if (edtPassword.length() < 8) {
                        edtPassword.error = getString(R.string.error_field)
                    }
                } else {
                    userViewModel.login(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }

        userViewModel.apply {
            isLoading.observe(this@LoginActivity) {
                showLoading(it)
            }
            message.observe(this@LoginActivity) {
                it.getContentIfNotHandled()?.let {
                    Log.i("messageErrorRegister", it)
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(this@LoginActivity) {
                saveSession(SessionData(it.token, true))
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun createLoading() {
        loading = createAlertDialog(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }
}