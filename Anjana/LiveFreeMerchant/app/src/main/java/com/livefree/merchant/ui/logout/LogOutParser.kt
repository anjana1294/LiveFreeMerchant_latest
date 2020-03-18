package com.livefree.merchant.ui.logout

internal object LogOutParser {


    /*@Throws(IOException::class, NullPointerException::class)
    fun parse(response: Response<LogoutResponse>): String {

        if (response.isSuccessful()) {
            val body = response.body()

            return if (body!!.isStatus()) {
                if (!TextUtils.isEmpty(body!!.getMsg())) {
                    body!!.getMsg()
                } else {
                    throw RuntimeException("Response payload is empty!")
                }
            } else {
                throw RuntimeException(body!!.getMsg())
            }
        } else {
            throw RuntimeException(response.message())
        }
    }*/
}