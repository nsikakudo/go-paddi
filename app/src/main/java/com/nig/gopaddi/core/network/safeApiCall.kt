package com.nig.gopaddi.core.network

import com.nig.gopaddi.core.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(dispatcher) {
        try {
            Resource.Success(apiCall.invoke())
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                400 -> "We couldn't process this trip request. Please check your details."
                401 -> "Your session has expired. Please log in again."
                404 -> "We couldn't find the destination or trip you're looking for."
                in 500..599 -> "Our travel servers are taking a break. Please try again shortly."
                else -> "Something went wrong on our end. Error: ${e.code()}"
            }
            Resource.Error(errorMessage)
        } catch (e: IOException) {
            Resource.Error("Connection lost! Please check your internet to continue planning your adventure.")
        } catch (e: Exception) {
            Resource.Error("An unexpected hitch occurred: ${e.localizedMessage ?: "Unknown error"}")
        }
    }
}