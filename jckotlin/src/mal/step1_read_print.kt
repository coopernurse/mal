package mal

fun read(): MalType = read_str(readLine()!!)

fun eval(s: MalType): MalType = s

fun rep() = println(pr_str(eval(read())))

fun main(args: Array<String>) {
    while (true) {
        print("user> ")
        try {
            rep()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
