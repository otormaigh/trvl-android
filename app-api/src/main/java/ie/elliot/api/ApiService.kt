/*
 * Copyright (C) 2017 Elliot Tormey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ie.elliot.api

import ie.elliot.api.model.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author Elliot Tormey
 * @since 05/06/2017
 */
internal interface ApiService {
    @GET("airports/")
    fun getAirports(): Observable<List<Airport>>

    @GET("airlines/")
    fun getAirlines(): Observable<List<Airline>>

    @GET("flights/")
    fun getFlights(): Observable<List<Flight>>

    @POST("booking/")
    fun postBooking(@Body booking: Booking): Observable<BookingResponse>
}