package ru.scarlet.rabbit.service

import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.scarlet.rabbit.event.Address
import ru.scarlet.rabbit.event.Customer
import ru.scarlet.rabbit.event.Order
import ru.scarlet.rabbit.event.Product

const val NEW_CUSTOMER_ROUTING_KEY = "store.customer.created"
const val NEW_ORDER_ROUTING_KEY = "store.order.created"
@Service
@Slf4j
class EventProducer(private val rabbitTemplate: RabbitTemplate) {
    fun sendNewCustomerDetails() {
        println("Sending new customer details")
        val customer = Customer(customerId = 1, customerName = "John", address = Address(1, "Main Street", "New York", true), email = "abcdef")
        rabbitTemplate.convertAndSend(NEW_CUSTOMER_ROUTING_KEY, customer)
        println("$customer")
    }

    fun sendNewOrderDetails(){
        println("Sending new order details")
        val order = Order(1, "Order 1", listOf(Product("Product 1", 1)), 1, true)
        rabbitTemplate.convertAndSend(NEW_ORDER_ROUTING_KEY, order)
        println("$order")
    }

    @Scheduled(fixedDelay = 1000L)
    fun execute() {
        sendNewCustomerDetails()
        sendNewOrderDetails()
    }
}