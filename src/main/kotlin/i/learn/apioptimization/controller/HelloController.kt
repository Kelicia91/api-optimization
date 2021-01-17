package i.learn.apioptimization.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HelloController {
    @GetMapping("/hello")
    fun hello(): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}