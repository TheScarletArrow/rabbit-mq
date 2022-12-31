package ru.scarlet.rabbit.event

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Order(
    @JsonProperty("orderId") val orderId: UUID,
    @JsonProperty("orderName") val orderName: String,
    @JsonProperty("product") val product: List<Product>,
    @JsonProperty("totalQuantity") val totalQuantity: Int,
    @JsonProperty("isSameDayDelivery") val isSameDayDelivery: Boolean,
    val createdAt: Long
)

data class Product(
        @JsonProperty("name") val name: String,
        @JsonProperty("quantity") val quantity: Int
)