package com.weatheriza.core.base

abstract class NonSuspendingUseCase<ResultType : Any, in Params> {

    protected abstract fun build(params: Params?): ResultType

    open operator fun invoke(params: Params? = null): ResultType {
        return build(params)
    }
}

abstract class BaseUseCase<ResultType : Any, in Params> {

    protected abstract suspend fun build(params: Params?): ResultType

    open suspend operator fun invoke(params: Params? = null): ResultType {
        return build(params)
    }
}
