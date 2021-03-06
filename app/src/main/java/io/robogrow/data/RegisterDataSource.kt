package io.robogrow.data

import io.robogrow.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ register credentials and retrieves user information.
 */
class RegisterDataSource {

    fun register(username: String, password: String, password2: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

