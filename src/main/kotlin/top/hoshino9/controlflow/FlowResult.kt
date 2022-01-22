package top.hoshino9.controlflow

sealed interface FlowResult<out T> {
    sealed interface FinalFlowResult<out T> : FlowResult<T>

    object Continue : FlowResult<Nothing>
    object Cancelled : FinalFlowResult<Nothing>
    data class Success<T>(val value: T) : FinalFlowResult<T>
}

fun susumu(): FlowResult.Continue = FlowResult.Continue
fun cancel(): FlowResult.Cancelled = FlowResult.Cancelled

fun <T> yield(value: T): FlowResult.Success<T> = FlowResult.Success(value)

inline fun <T> FlowResult.FinalFlowResult<T>.bind(block: () -> FlowResult.FinalFlowResult<T>): FlowResult.FinalFlowResult<T> {
    return when (this) {
        FlowResult.Cancelled -> this
        is FlowResult.Success -> block()
    }
}

inline fun <T> FlowResult.FinalFlowResult<T>.bindCancel(block: () -> FlowResult.FinalFlowResult<T>): FlowResult.FinalFlowResult<T> {
    return when (this) {
        FlowResult.Cancelled -> block()
        is FlowResult.Success -> this
    }
}

fun <T> FlowResult.FinalFlowResult<T>.unsafeGet(): T {
    return (this as FlowResult.Success).value
}