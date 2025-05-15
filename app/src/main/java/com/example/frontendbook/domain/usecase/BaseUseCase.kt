package com.example.frontendbook.domain.usecase

abstract class BaseUseCase<in Params, out Result> {
    abstract suspend fun execute(params: Params): Result
}