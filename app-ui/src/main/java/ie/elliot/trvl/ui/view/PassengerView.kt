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
package ie.elliot.trvl.ui.view

import android.content.Context
import android.widget.LinearLayout
import ie.elliot.api.model.Passenger
import ie.elliot.trvl.ui.R

/**
 * @author Elliot Tormey
 * @since 11/06/2017
 */
class PassengerView(context: Context) : LinearLayout(context) {

    init {
        LinearLayout.inflate(getContext(), R.layout.passenger_view, this)
        layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun getPassenger(): Passenger {
        return Passenger()
    }
}