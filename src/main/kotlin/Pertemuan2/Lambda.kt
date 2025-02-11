package Pertemuan2

fun main() {
    val greet = {name : String -> greeting(name)}
    greet("Kotlin")
}

fun greeting(name: String) {
    println("Hello $name!")
}