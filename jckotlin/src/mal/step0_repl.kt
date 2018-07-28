package mal

fun read(): String = readLine()!!

fun eval(s: String): String = s

fun rep() = println(eval(read()))

fun main(args: Array<String>) {
    while (true) {
        print("user> ")
	rep()
    }
}