package mal

open class MalType {
    override fun toString(): String = ""
}

class MalSymbol(val value: String): MalType() {
    override fun toString() = value
}

class MalStr(val value: String): MalType() {
    override fun toString() = '"' + value + '"'
}

class MalLong(val value: Long): MalType() {
    override fun toString() = value.toString()
}

class MalDouble(val value: Double): MalType() {
    override fun toString() = value.toString()
}

class MalBool(val value: Boolean): MalType() {
    override fun toString() = value.toString()
}

interface MalAddable {
    fun add(t: MalType): Boolean
}

class MalList: MalAddable, MalType() {
    val vals: MutableList<MalType> = mutableListOf()

    override fun add(t: MalType) = vals.add(t)

    override fun toString(): String {
        val sb = StringBuilder("(")
        for (v in vals) {
            if (sb.length > 1) {
                sb.append(" ")
            }
            sb.append(v.toString())
        }
	sb.append(")")
        return sb.toString()
    }
}

class MalVector: MalAddable, MalType() {
    val vals: MutableList<MalType> = mutableListOf()

    override fun add(t: MalType) = vals.add(t)

    override fun toString(): String {
        val sb = StringBuilder("[")
        for (v in vals) {
            if (sb.length > 1) {
                sb.append(" ")
            }
            sb.append(v.toString())
        }
	sb.append("]")
        return sb.toString()
    }
}


