@file:Suppress("DEPRECATION")

package com.example.app.data

import com.example.app.data.model.LoggedInUser
import com.example.app.network.OkHttpsClient
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.concurrent.Executors


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val executor = Executors.newSingleThreadExecutor()
            // TODO: handle loggedInUser authentication








            return Result.Success(
                LoggedInUser(
                    username
                )
            )
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}