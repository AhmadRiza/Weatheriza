package com.weatheriza.core.model

import com.weatheriza.core.entities.ErrorNetworkResult
import com.weatheriza.core.entities.NetworkResult
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ResultKtTest : BehaviorSpec({

    given("Map NetworkResult to result") {
        When("success") {
            and("with data") {
                and("an empty list") {
                    then("should return empty result") {
                        NetworkResult.Success
                            .WithData(emptyList<String>())
                            .toResult { it } shouldBe
                                Result.Success.Empty

                    }
                }
                and("not an empty list") {
                    then("should return correct result") {
                        NetworkResult.Success
                            .WithData("Success Result")
                            .toResult { it.lowercase() } shouldBe
                                Result.Success.WithData("success result")
                    }
                }

            }
            and("with no data") {
                then("should return empty result") {
                    NetworkResult.Success
                        .EmptyData(500)
                        .toResult { it } shouldBe
                            Result.Success.Empty
                }
            }
            and("error") {
                and("network error") {
                    then("should return correct error result") {
                        ErrorNetworkResult
                            .NetworkError(errorMessage = "server down", httpCode = 500)
                            .toResult { it } shouldBe
                                Result.Error("server down", 500)
                    }
                }
                and("no internet error") {
                    then("should return correct error result") {
                        ErrorNetworkResult
                            .NoInternetConnection
                            .toResult { it } shouldBe
                                Result.Error(DefaultErrorMessage.NO_INTERNET)
                    }
                }
                and("unknown error") {
                    then("should return correct error result") {
                        ErrorNetworkResult
                            .UnknownError("json invalid at line 123")
                            .toResult { it } shouldBe
                                Result.Error(DefaultErrorMessage.UNKNOWN)
                    }
                }
            }

        }

    }

})
