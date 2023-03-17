package hn.single.imageapp.common.networking

import com.google.gson.Gson
import hn.single.imageapp.common.utils.AppConstants
import hn.single.imageapp.common.utils.Logger
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object ErrorNetwork {

    fun Throwable.toBaseException(): BaseException {
        return when (val throwable = this) {
            is BaseException -> throwable

            is IOException -> BaseException.toNetworkError(throwable)

            is HttpException -> {
                val response = throwable.response()
                val httpCode = throwable.code()

                if (response?.errorBody() == null) {
                    return BaseException.toHttpError(httpCode = httpCode, response = response)
                }

                val serverErrorResponseBody = try {
                    response.errorBody()?.string() ?: AppConstants.EMPTY_STRING
                } catch (e: Exception) {
                    Logger.e("${e.message}")
                    AppConstants.EMPTY_STRING
                }

                val serverErrorResponse =
                    try {
                        Gson().fromJson(serverErrorResponseBody, ServerErrorResponse::class.java)
                    } catch (e: Exception) {
                        Logger.e("${e.message}")
                        ServerErrorResponse(message = serverErrorResponseBody)
                    }

                if (serverErrorResponse != null) {
                    BaseException.toServerError(
                        serverErrorResponse = serverErrorResponse,
                        response = response,
                        httpCode = httpCode
                    )
                } else {
                    BaseException.toHttpError(response = response, httpCode = httpCode)
                }
            }

            else -> BaseException.toUnexpectedError(throwable)
        }
    }

    class BaseException(
        val errorType: ErrorType,
        val serverErrorResponse: ServerErrorResponse? = null,
        val response: Response<*>? = null,
        val httpCode: Int = 0,
        cause: Throwable? = null,
    ) : RuntimeException(cause?.message, cause) {

        override val message: String?
            get() = when (errorType) {
                ErrorType.HTTP -> response?.message()

                ErrorType.NETWORK -> cause?.message

                ErrorType.SERVER -> serverErrorResponse?.errors?.getOrNull(0)

                ErrorType.UNEXPECTED -> cause?.message
            }

        companion object {
            fun toHttpError(response: Response<*>?, httpCode: Int) =
                BaseException(
                    errorType = ErrorType.HTTP,
                    response = response,
                    httpCode = httpCode
                )

            fun toNetworkError(cause: Throwable) =
                BaseException(errorType = ErrorType.NETWORK, cause = cause)

            fun toServerError(
                serverErrorResponse: ServerErrorResponse,
                response: Response<*>?,
                httpCode: Int,
            ) = BaseException(
                errorType = ErrorType.SERVER,
                serverErrorResponse = serverErrorResponse,
                response = response,
                httpCode = httpCode
            )

            fun toUnexpectedError(cause: Throwable) =
                BaseException(errorType = ErrorType.UNEXPECTED, cause = cause)
        }
    }

    enum class ErrorType {
        //[IOException] occurred while communicating to the server.
        NETWORK,

        //non-2xx HTTP status code was received from the server.
        HTTP,

        //An error server with code & message
        SERVER,

        /**
         * An internal error occurred while attempting to execute a request.
         * It is best practice to re-throw this exception so your application crashes.
         */
        UNEXPECTED,
    }

    data class ServerErrorResponse(
        val message: String? = null,
        val errors: List<String>? = null,
    )
}