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
package ie.elliot.trvl.ui.activity.airport_search

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import ie.elliot.api.ApiIntentService
import ie.elliot.trvl.R
import ie.elliot.trvl.base.TrvlActivity
import ie.elliot.trvl.base.TrvlPresenter
import ie.elliot.trvl.ui.activity.flight_search.FlightSearchActivity
import kotlinx.android.synthetic.main.activity_airport_search.*
import timber.log.Timber

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class AirportSearchActivity : TrvlActivity<TrvlPresenter<AirportSearchView>>(), AirportSearchView {
    companion object {
        private val HINT_RES_ID = "hint_res_id"

        fun newInstance(activity: FlightSearchActivity, @StringRes hintResId: Int) {
            val intent = Intent(activity, AirportSearchActivity::class.java)
            intent.putExtra(HINT_RES_ID, hintResId)
            activity.startActivityForResult(intent, 13)
        }
    }

    private val hintResId by lazy { intent.getIntExtra(HINT_RES_ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_search)
        ApiIntentService.getAllAirports(this)

        etSearch.setHint(hintResId)

        rvAirports.layoutManager = LinearLayoutManager(this)
        rvAirports.adapter = AirportSearchAdapter(this)
    }

    override fun goHome(airportIcao: String) {
        FlightSearchActivity.resultFromAirportSearch(this, hintResId, airportIcao)
    }
}