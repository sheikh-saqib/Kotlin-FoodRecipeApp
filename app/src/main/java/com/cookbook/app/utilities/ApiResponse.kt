package com.cookbook.app.utilities

sealed class ApiResponse<T>(
    val responseData:T? = null,
    val responseMessage:String? = null
) {
    class ResponseSuccess<T>(responseData:T):ApiResponse<T>(responseData)
    class ResponseError<T>(responseMessage:String?,responseData:T?=null):ApiResponse<T>(responseData,responseMessage)
    class ResponseLoading<T>:ApiResponse<T>()
}