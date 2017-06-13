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
package ie.elliot.trvl.ui.activity.home

import ie.elliot.api.model.Airport
import ie.elliot.trvl.base.TrvlPresenter
import ie.elliot.trvl.base.TrvlView

/**
 * @author Elliot Tormey
 * @since 13/06/2017
 */
internal class HomePresenter : TrvlPresenter<TrvlView>() {

    fun getAirport(airportIcao: String): Airport {
        return model.getAirport(airportIcao)
    }

    fun getBookingId(arriveAirport: String, departAirport: String): Long {
        return model.createBooking(arriveAirport, departAirport)
    }
}