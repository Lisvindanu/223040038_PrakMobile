package Pertemuan2

fun main() {
    //loop for
    for(i in 4..9) {
        println("pointer ke - $i")
    }

    //loop while
    var a = 1
    while(a <= 5) {
        println(a)
        a++
    }

    //loop do-while
    var b = 0
    do {
        println(b)
        b++
    } while (b <= 5)
}