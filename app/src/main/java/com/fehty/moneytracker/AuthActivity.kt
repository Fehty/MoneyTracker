package com.fehty.moneytracker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.fehty.moneytracker.Api.Api
import com.fehty.moneytracker.Data.AuthResult
import com.fehty.moneytracker.ItemList.App
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    private var TAG = "AuthActivity"
    private var googleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 321
    private var api: Api? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        api = (application as App).getApi()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signInButton.setOnClickListener({
            signIn()
        })
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        when {
            account != null -> updateUI(account)
        }
    }

    private fun signIn() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        var id = account?.id
        api!!.auth(id!!).enqueue(object : Callback<AuthResult> {
            override fun onResponse(call: Call<AuthResult>?, response: Response<AuthResult>?) {
                var result = response?.body()
                (application as App).saveAuthToken(result!!.token)
                finish()
            }

            override fun onFailure(call: Call<AuthResult>?, t: Throwable?) {
                showError("Auth Error" + t)
            }
        })
    }

    fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

}
