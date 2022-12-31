package ru.scarlet.rabbit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.scarlet.rabbit.event.Address
import ru.scarlet.rabbit.event.Customer
import ru.scarlet.rabbit.event.Order
import ru.scarlet.rabbit.event.Product
import java.time.Instant
import java.util.*


const val NEW_CUSTOMER_ROUTING_KEY = "store.customer.created"
const val NEW_ORDER_ROUTING_KEY = "store.order.created"

@Service
@Slf4j
class EventProducer(private val rabbitTemplate: RabbitTemplate) {

    // create new Order and return it with random fields
    private fun createNewOrder(): Order {
        val mapper: ObjectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build()
        mapper.registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        return Order(

            orderId = UUID.randomUUID(),
            orderName = "Order ##",
            totalQuantity = (1..10).random(),
            isSameDayDelivery = (0..1).random() == 1,
            createdAt = Instant.now().epochSecond,
            product = listOf(
                Product(
                    name = "Product ##",
                    quantity = (1..10).random()
                )
            )

        )
    }

    fun sendNewCustomerDetails() {
        println("Sending new customer details")
        val customer = Customer(
            customerId = UUID.randomUUID(),
            customerName = "John",
            address = Address("Main Street", "New York", true),
            email = "abcdef"
        )
        rabbitTemplate.convertAndSend(NEW_CUSTOMER_ROUTING_KEY, customer)
        println("$customer")
    }

    fun sendNewOrderDetails() {
        println("Sending new order details")
        val order = createNewOrder()
        rabbitTemplate.convertAndSend(NEW_ORDER_ROUTING_KEY, order)
        println("$order")
    }

    @Scheduled(fixedDelay = 1000L)
    fun execute() {
        sendNewCustomerDetails()
        sendNewOrderDetails()
    }
}