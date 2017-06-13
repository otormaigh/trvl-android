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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import ie.elliot.trvl.R
import ie.elliot.trvl.base.TrvlActivity
import ie.elliot.trvl.ui.activity.airport_confirm.AirportConfirmActivity
import ie.elliot.trvl.ui.activity.airport_search.AirportSearchActivity
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class HomeActivity : TrvlActivity<HomePresenter>(HomePresenter()), View.OnClickListener {
    companion object {
        private val HINT_RES_ID = "hint_res_id"
        private val AIRPORT_ICAO = "airport_icao"

        fun resultFromAirportSearch(activity: AirportSearchActivity, @StringRes hintResId: Int, airportIcao: String) {
            Intent().apply {
                putExtra(HINT_RES_ID, hintResId)
                putExtra(AIRPORT_ICAO, airportIcao)
                activity.setResult(Activity.RESULT_OK, this)
            }
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        avArrive.setOnClickListener(this)
        avDepart.setOnClickListener(this)
        btnSearch.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == RESULT_OK) {
                val airport = presenter?.getAirport(data.getStringExtra(AIRPORT_ICAO))
                when (data.getIntExtra(HINT_RES_ID, 0)) {
                    avArrive.hint -> avArrive.airport = airport
                    avDepart.hint -> avDepart.airport = airport
                    else -> Timber.e("hintRes has no match")
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.avArrive -> AirportSearchActivity.newInstance(this, avArrive.hint)
            R.id.avDepart -> AirportSearchActivity.newInstance(this, avDepart.hint)
            R.id.btnSearch -> {
                if (avArrive.airport != null && avDepart.airport != null) {
                    startActivity(Intent(this, AirportConfirmActivity::class.java))
                }
            }
        }
    }
}
