package ru.scarlet.rabbit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.scarlet.rabbit.event.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID


const val NEW_CUSTOMER_ROUTING_KEY = "store.customer.created"
const val NEW_ORDER_ROUTING_KEY = "store.order.created"
const val NEW_SESSION_ROUTING_KEY = "store.session.created"
const val NEW_POST_ROUTING_KEY = "store.post.created"
const val NEW_COMMENT_ROUTING_KEY = "store.comment.created"

@Service
@Slf4j
class EventProducer(private val rabbitTemplate: RabbitTemplate) {

    // create new Order and return it with random fields
    private fun createNewOrder(): Order {
        val mapper: ObjectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build()
        mapper.registerModule(com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
        val totalQuantity = (1..10).random()
        return Order(

            orderId = UUID.randomUUID(),
            orderName = "Order ##",
            totalQuantity = totalQuantity,
            isSameDayDelivery = totalQuantity == 1,
            createdAt = Instant.now().epochSecond,
            product = if (totalQuantity>5) listOf(
                Product(
                    name = "Product ##",
                    quantity = totalQuantity
                )
            ) else listOf()

        )
    }

    private fun createNewCustomer():Customer{
        return Customer(
            customerId = UUID.randomUUID(),
            customerName = "Customer ##",
            address = Address(
                streetName = "Street ##",
                city = "City ##",
                isHomeAddress = (0..1).random() == 1
            ),
            email = "email"
        )
    }

    fun sendNewCustomerDetails() {
        println("Sending new customer details")
        val customer = createNewCustomer()
        rabbitTemplate.convertAndSend(NEW_CUSTOMER_ROUTING_KEY, customer)
        println("$customer")
    }

    fun sendNewOrderDetails() {
        println("Sending new order details")
        val order = createNewOrder()
        rabbitTemplate.convertAndSend(NEW_ORDER_ROUTING_KEY, order)
        println("$order")
    }

    @Scheduled(fixedDelay = 100L)
    fun execute() {
        sendNewSessionDetails()
        sendNewCustomerDetails()
        sendNewOrderDetails()
        sendsNewPostDetails()
        sendNewCommentDetails()
    }

    fun sendNewSessionDetails() {
        println("Sending new session details")
        val session = CurrentSession(
            sessionId = UUID.randomUUID(),
            sessionName = "Session ##",
            startedAt = Instant.now().epochSecond,
            customerId =  createNewCustomer().customerId
        )
        rabbitTemplate.convertAndSend(NEW_SESSION_ROUTING_KEY, session)
    }

    fun sendsNewPostDetails() {
        println("Sending new post details")
        val post = Post(
            id = UUID.randomUUID(),
            title = "Post ##",
            createdAt = Instant.now().epochSecond,
            author = "Author ${(2..100).random()}",
            updatedAt = null,
            body = "Body ##",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sed nisl vitae nisl luctus lacinia. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed auctor, nisl vitae luctus lacinia, nisl nisl aliquam nisl, nec aliquam nisl nisl nec nisl. Sed a"
        )
        rabbitTemplate.convertAndSend(NEW_POST_ROUTING_KEY, post)
    }

    fun sendNewCommentDetails(){
        println("Sending new comment details")
        val comment = Comment(
            id = UUID.randomUUID(),
            postId = UUID.randomUUID(),
            author = "Author ${(2..100).random()}",
            createdAt = Instant.now().epochSecond,
            updatedAt = null,
            body = "Comment ##",
            content = "Comment ${LocalDateTime.now()}"
        )
        rabbitTemplate.convertAndSend(NEW_COMMENT_ROUTING_KEY, comment)
    }
}