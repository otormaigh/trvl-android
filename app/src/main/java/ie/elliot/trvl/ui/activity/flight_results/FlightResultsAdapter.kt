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

package ie.elliot.trvl.ui.activity.flight_results

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.elliot.api.model.FlightResult
import ie.elliot.trvl.R
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_item_flight_result.view.*

/**
 * @author Elliot Tormey
 * @since 05/06/2017
 */
internal class FlightResultsAdapter()
    : RealmRecyclerViewAdapter<FlightResult, FlightResultsAdapter.ViewHolder>(Realm.getDefaultInstance().where(FlightResult::class.java).findAllAsync(), true) {

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        if (data != null) {
            viewHolder?.bind(data?.get(position))
        }
    }

    override fun onCreateViewHolder(container: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(container?.context).inflate(R.layout.list_item_flight_result, container, false))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(flightResult: FlightResult?) {
            if (flightResult != null) {
                itemView.tvAirlineName.text = flightResult.airline.name
                itemView.tvPrice.text = flightResult.price
                // TODO : Elliot -> Convert timestamp to hh:mmZ e.g. 18:30 IST
                // itemView.tvDepartAt.text = flightResult.departAt
                // itemView.tvArriveAt.text = flightResult.arriveAt
                itemView.tvFlightTime.text = flightResult.flightTime

                val stopCount = itemView.resources.getQuantityString(R.plurals.num_stop, flightResult.stopCount)
                itemView.tvStops.text = stopCount
            }
        }
    }
}