package i.learn.apioptimization.controller.interfaces

data class WrappedResponse<T>(
    val data: T
) {
    companion object {
        fun <T> of(value: T): WrappedResponse<T> {
            return WrappedResponse(data = value)
        }
    }
}