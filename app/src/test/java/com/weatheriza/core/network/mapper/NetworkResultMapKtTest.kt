package com.weatheriza.core.network.mapper

import com.weatheriza.core.entities.ErrorNetworkResult
import com.weatheriza.core.entities.NetworkResult
import com.weatheriza.core.network.exception.NoInternetConnectionException
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class NetworkResultMapKtTest : BehaviorSpec(
    {
        given("safeApiCall") {
            When("no error") {
                then("should return network result") {
                    safeApiCall {
                        NetworkResult.Success.WithData("Halo")
                    } shouldBe NetworkResult.Success.WithData("Halo")
                }
            }
            When("error") {
                and("throw no internet exception") {
                    then("shold return error no internet network result") {
                        safeApiCall<String> {
                            throw NoInternetConnectionException()
                        } shouldBe ErrorNetworkResult.NoInternetConnection
                    }
                }
                and("throw unknown exception") {
                    then("shold return unknown error network result") {
                        safeApiCall<String> {
                            throw Exception("error")
                        } shouldBe ErrorNetworkResult.UnknownError("error")
                    }
                }

            }
        }

    }
)
