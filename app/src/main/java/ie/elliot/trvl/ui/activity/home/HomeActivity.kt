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

import android.content.Intent
import android.os.Bundle
import android.view.View
import ie.elliot.api.ApiIntentService
import ie.elliot.trvl.R
import ie.elliot.trvl.base.TrvlActivity
import ie.elliot.trvl.ui.activity.airport_confirm.AirportConfirmActivity
import ie.elliot.trvl.ui.fragment.AirportSearchFragment.AirportSearchFragment
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class HomeActivity : TrvlActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        avDestination.setOnClickListener(this)
        avOrigin.setOnClickListener(this)
        btnSearch.setOnClickListener(this)

        ApiIntentService.getAllAirports(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.avDestination -> replaceFrag(AirportSearchFragment.newInstance(view.id))
            R.id.avOrigin -> replaceFrag(AirportSearchFragment.newInstance(view.id))
            R.id.btnSearch -> startActivity(Intent(this, AirportConfirmActivity::class.java))
        }
    }
}
