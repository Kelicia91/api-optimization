package i.learn.apioptimization.exception

import i.learn.apioptimization.support.MessageKey

open class AppException(messageKey: MessageKey): RuntimeException(messageKey.name)

class NotFoundException(messageKey: MessageKey): AppException(messageKey)

