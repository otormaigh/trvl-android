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

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ie.elliot.trvl.ui.view.PassengerView

/**
 * @author Elliot Tormey
 * @since 11/06/2017
 */
internal class PassengerDetailAdapter(val passengerCount: Int) : RecyclerView.Adapter<PassengerDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(PassengerView(container.context))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, itemType: Int) {
    }

    override fun getItemCount(): Int = passengerCount

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}