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
import android.support.annotation.IdRes
import android.support.v7.widget.LinearLayoutManager
import ie.elliot.api.ApiIntentService
import ie.elliot.trvl.R
import ie.elliot.trvl.base.TrvlActivity
import ie.elliot.trvl.ui.activity.home.HomeActivity
import kotlinx.android.synthetic.main.activity_airport_search.*
import android.app.Activity


/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class AirportSearchActivity : TrvlActivity(), AirportSearchView {
    companion object {
        private val DESTINATION = "destination"
        private val ORIGIN = "origin"

        fun newInstance(activity: HomeActivity, @IdRes viewRes: Int) {
            val intent = Intent(activity, AirportSearchActivity::class.java)
            if (viewRes == R.id.avDestination) {
                intent.putExtra(DESTINATION, true)
            } else {
                intent.putExtra(ORIGIN, true)
            }
            activity.startActivityForResult(intent, 13)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_search)
        ApiIntentService.getAllAirports(this)

        if (intent.getBooleanExtra(DESTINATION, false)) {
            etSearch.setHint(R.string.where_to)
        } else {
            etSearch.setHint(R.string.depart_from)
        }

        rvAirports.layoutManager = LinearLayoutManager(this)
        rvAirports.adapter = AirportSearchAdapter(this)
    }

    override fun goHome() {
        val returnIntent = Intent()
        returnIntent.putExtra("result", 13)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}