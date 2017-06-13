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
package ie.elliot.trvl.ui.activity.passenger_detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import ie.elliot.trvl.R
import ie.elliot.trvl.base.TrvlActivity
import ie.elliot.trvl.base.TrvlPresenter
import ie.elliot.trvl.base.TrvlView
import kotlinx.android.synthetic.main.activity_passenger_detail.*

/**
 * @author Elliot Tormey
 * @since 11/06/2017
 */
internal class PassengerDetailActivity : TrvlActivity<TrvlPresenter<TrvlView>>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_detail)

        // TODO : Elliot -> Test data
        rvPassengers.adapter = PassengerDetailAdapter(7)
        rvPassengers.layoutManager = LinearLayoutManager(this)
    }
}