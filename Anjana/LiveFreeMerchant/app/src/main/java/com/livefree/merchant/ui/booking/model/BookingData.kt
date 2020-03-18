package com.livefree.merchant.ui.booking.model

import com.google.gson.annotations.SerializedName
import com.livefree.merchant.ui.section.model.SectionData
import com.livefree.merchant.ui.server.model.ServerData
import com.livefree.merchant.ui.table.model.TableData

data class BookingData(
    val categorytag_id: String,
    val guestDate: String,
    val guestTime: String,
    val booking_code: String,

    @SerializedName("section_id")
    val section_id: SectionData,

    @SerializedName("server_id")
    val server_id: ServerData,

    @SerializedName("tableset_id")
    val tableset_id: TableData,

    @SerializedName("createdById")
    val customerBooking: CustomerBooking,

    val _id: String
) {

}
