package com.example.lab1.ui.theme

import android.R
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel: ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or passsword empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error("Something went wrong")

                }
            }
    }

    fun register(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or passsword empty")
            return
        } else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                if(task.isSuccessful){

                    val user = auth.currentUser
                    _authState.value = AuthState.Authenticated
                    Log.d("UserCreate","Utworzono pomyslnie uzytkownika${authState.value}")
                } else {
                    Log.w("UserCreate","Nie udalo sie stworzyc uzytkownika")
                    _authState.value = AuthState.Error("Nie udalo sie zalozyc konta")
                }
            }
        }

    }


    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message : String) : AuthState()
}