package com.faishalbadri.penyakitikan.viewmodel.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faishalbadri.penyakitikan.util.UserPreferences
import com.faishalbadri.penyakitikan.viewmodel.DiseaseViewModel
import com.faishalbadri.penyakitikan.viewmodel.UserViewModel

class ViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(DiseaseViewModel::class.java) -> {
                DiseaseViewModel(userPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}