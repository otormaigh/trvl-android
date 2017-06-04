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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.elliot.trvl.R
import ie.elliot.trvl.model.Airport
import kotlinx.android.synthetic.main.list_item_airport.view.*

/**
 * @author Elliot Tormey
 * @since 04/06/2017
 */
internal class AirportSearchAdapter(val airports: List<Airport>) : RecyclerView.Adapter<AirportSearchAdapter.ViewHolder>() {
    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder?.bind(airports[position])
    }

    override fun onCreateViewHolder(container: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(container?.context).inflate(R.layout.list_item_airport, container))
    }

    override fun getItemCount(): Int = airports.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(airport: Airport) {
            itemView.tvAirport.text = airport.name
        }
    }
}