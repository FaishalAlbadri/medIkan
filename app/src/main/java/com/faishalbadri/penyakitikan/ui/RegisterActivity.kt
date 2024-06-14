package com.faishalbadri.penyakitikan.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.faishalbadri.penyakitikan.R
import com.faishalbadri.penyakitikan.databinding.ActivityRegisterBinding
import com.faishalbadri.penyakitikan.util.createAlertDialog
import com.faishalbadri.penyakitikan.util.htmlStringFormat
import com.faishalbadri.penyakitikan.viewmodel.UserViewModel
import com.faishalbadri.penyakitikan.viewmodel.base.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityRegisterBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val userViewModel: UserViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            btnLogin.apply {
                text = htmlStringFormat(this@RegisterActivity, "Already have account?", "Log In")
                setOnClickListener {
                    onBackPressedCallback.handleOnBackPressed()
                }
            }

            btnRegister.setOnClickListener {
                if (edtUsername.length() == 0 || edtEmail.length() == 0 || edtPassword.length() < 8) {
                    if (edtUsername.length() == 0) {
                        edtUsername.error = getString(R.string.error_field)
                    }
                    if (edtEmail.length() == 0) {
                        edtEmail.error = getString(R.string.error_field)
                    }
                    if (edtPassword.length() < 8) {
                        edtPassword.error = getString(R.string.error_field)
                    }
                } else {
                    userViewModel.register(edtUsername.text.toString(), edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }

        userViewModel.apply {
            isLoading.observe(this@RegisterActivity) {
                showLoading(it)
            }
            message.observe(this@RegisterActivity) {
                it.getContentIfNotHandled()?.let {
                    Log.i("messageErrorRegister", it)
                    Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(this@RegisterActivity) {
                onBackPressedCallback.handleOnBackPressed()
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