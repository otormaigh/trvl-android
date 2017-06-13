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

package ie.elliot.trvl.ui.activity.airport_confirm

import android.content.Intent
import android.os.Bundle
import ie.elliot.api.model.Booking
import ie.elliot.trvl.R
import ie.elliot.trvl.base.TrvlActivity
import ie.elliot.trvl.ui.activity.flight_results.FlightResultsActivity
import ie.elliot.trvl.ui.activity.home.HomeActivity
import kotlinx.android.synthetic.main.activity_airport_confirm.*

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class AirportConfirmActivity : TrvlActivity<AirportConfirmPresenter>(), AirportConfirmView {
    companion object {
        private val BOOKING_ID = "booking_id"

        fun newInstance(activity: HomeActivity, bookingId: Long) {
            Intent(activity, AirportConfirmActivity::class.java).apply {
                putExtra(BOOKING_ID, bookingId)
                activity.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_confirm)
        presenter = AirportConfirmPresenter(this)

        btnSearchFlights.setOnClickListener {
            startActivity(Intent(this, FlightResultsActivity::class.java))
        }

        presenter!!.getBooking(intent.getLongExtra(BOOKING_ID, 0))
    }

    override fun setBookingToViews(booking: Booking) {
        avArrive.airport = booking.flight.arrive_airport
        avDepart.airport = booking.flight.depart_airport
    }
}