package id.parking.core.other

data class Resource<out T>(
    val status: Status,
    val response: T?,
    val message:String?,
    val errorCode: Int? = 1
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg:String?, errorCode: Int? = 0): Resource<T> {
            return Resource(Status.ERROR, null, msg, errorCode)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }

    fun onLoading(listener: (Boolean) -> Unit) {
        if (status == Status.LOADING) listener(true)
        else listener(false)
    }

    fun onError(listener: (message: String?) -> Unit) {
        if (status == Status.ERROR) listener(message)
    }

    fun onSuccess(listener: (T) -> Unit) {
        if (status == Status.SUCCESS) response?.let { listener(it) }
    }

}
