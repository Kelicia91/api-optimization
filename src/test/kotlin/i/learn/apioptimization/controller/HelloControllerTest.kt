package i.learn.apioptimization.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelloControllerTest(
    @Autowired val restTemplate: TestRestTemplate
){
    @Test
    fun hello() {
        val response = restTemplate.getForEntity("/hello", Unit.javaClass)
        assertEquals(response.statusCode, HttpStatus.OK)
    }
}