package i.learn.apioptimization.support

enum class MessageKey constructor(
    private val messageSourceKey: String
) {
    EXCEPTION("exception"),
    NOT_FOUND_EXCEPTION("not-found-exception");

    fun getMessageSourceKey(): String {
        return messageSourceKey
    }
}