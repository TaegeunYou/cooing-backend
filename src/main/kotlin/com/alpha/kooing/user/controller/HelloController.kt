import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/test")
    fun hello():String{
        println("hello")
        return "Hello"
    }
}