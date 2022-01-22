package top.hoshino9.controlflow

import top.hoshino9.controlflow.FlowResult.FinalFlowResult

inline fun <T> flowing(block: () -> FlowResult<T>): FinalFlowResult<T> {
    var result: FinalFlowResult<T>? = null

    while (result == null) {
        when (val ret = block()) {
            FlowResult.Continue -> Unit
            is FlowResult.Cancelled -> result = FlowResult.Cancelled
            is FlowResult.Success -> result = ret
        }
    }

    return result
}

/**
 * the basic function:
 */
inline fun <T> moshi(condition: Boolean, block: () -> FlowResult<T>): FinalFlowResult<T> {
    return if (condition) {
        flowing(block)
    } else cancel()
}

inline infix fun <T> FinalFlowResult<T>.hoka(block: () -> FlowResult<T>): FinalFlowResult<T> {
    return this.bindCancel {
        flowing(block)
    }
}

inline fun <T> FinalFlowResult<T>.hokaMoshi(condition: Boolean, block: () -> FlowResult<T>): FinalFlowResult<T> {
    return this hoka {
        moshi(condition, block)
    }
}

inline fun <T> suru(block: () -> FlowResult<T>): FinalFlowResult<T> {
    return moshi(true, block)
}

inline fun <T> loop(condition: () -> Boolean, block: () -> FlowResult<T>): FinalFlowResult<T> {
    return suru {
        val command = moshi (condition()) {
            yield(block())
        } hoka {
            yield(cancel())
        }

        command.unsafeGet()
    }
}