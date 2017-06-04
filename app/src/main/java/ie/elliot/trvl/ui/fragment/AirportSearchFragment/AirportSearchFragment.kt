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
package ie.elliot.trvl.ui.fragment.AirportSearchFragment

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.elliot.trvl.R
import ie.elliot.api.model.Airport
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_airport_search.*

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class AirportSearchFragment : Fragment() {
    companion object {
        private val DESTINATION = "destination"
        private val ORIGIN = "origin"

        fun newInstance(@IdRes viewRes: Int): AirportSearchFragment {
            val bundle = Bundle()
            if (viewRes == R.id.tvDestination) {
                bundle.putBoolean(DESTINATION, true)
            } else {
                bundle.putBoolean(ORIGIN, true)
            }

            val fragment = AirportSearchFragment().apply {
                arguments = bundle
            }

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_airport_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments.getBoolean(DESTINATION)) {
            etSearch.setHint(R.string.where_to)
        } else {
            etSearch.setHint(R.string.depart_from)
        }

        rvAirports.layoutManager = LinearLayoutManager(context)
        rvAirports.adapter = AirportSearchAdapter()
    }
}