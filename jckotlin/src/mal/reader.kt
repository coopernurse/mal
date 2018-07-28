package mal

import java.util.regex.Pattern
import java.util.regex.Matcher

class Reader(val tokens: List<String>) {

    var pos: Int = 0

    fun next(): String? {
        val t = peek()
        if (t != null) {
	    pos++
        }
        return t
    }

    fun peek(): String? = if (pos < tokens.size) tokens.get(pos) else null
}


// Add a function tokenizer in reader.qx. This function will take a single string and return
// an array/list of all the tokens (strings) in it. The following regular expression (PCRE) 
// will match all mal tokens.
fun tokenizer(s: String): List<String> {
    val re = """[\s,]*(~@|[\[\]{}()'`~^@]|"(?:\\.|[^\\"])*"|;.*|[^\s\[\]{}('"`,;)]*)"""
    val pat = Pattern.compile(re)
    val matcher = pat.matcher(s.trim())
    val tokens: MutableList<String> = mutableListOf()
    while (matcher.find()) {
        tokens.add(matcher.group(1))
    }
    return tokens
}

fun read_form(r: Reader): MalType {
    val p = r.peek()
    if (p == null) {
        return MalType()
    }
    else {
        // consume token
        r.next()
	val tok = p.trim()
        if (tok.equals("(")) {
            return read_list(r)
        }
        else if (tok.equals("[")) {
            return read_vector(r)
        }
        else if (tok == "'") {
            return read_wrapped(r, MalSymbol("quote"))
        }
        else if (tok == "`") {
            return read_wrapped(r, MalSymbol("quasiquote"))
        }
        else if (tok == "~") {
            return read_wrapped(r, MalSymbol("unquote"))
        }
        else if (tok == "~@") {
            return read_wrapped(r, MalSymbol("splice-unquote"))
        }
        else if (tok == "@") {
            return read_wrapped(r, MalSymbol("deref"))
        }
        else {
            return read_atom(tok)
        }
    }
}

fun read_wrapped(r: Reader, initial: MalType): MalList {
    val ml: MalList = MalList() 
    ml.add(initial)
    ml.add(read_form(r))
    return ml
}

fun read_list(r: Reader): MalList {
    val ml: MalList = MalList()
    read_list_or_vector(r, ml, ")")
    return ml    
}

fun read_vector(r: Reader): MalVector {
    val ml: MalVector = MalVector()
    read_list_or_vector(r, ml, "]")
    return ml
}

fun read_list_or_vector(r: Reader, ml: MalAddable, end: String): Unit {
    while (true) {
        val tok = r.peek()
        if (tok == null) {
            throw IllegalStateException("expected '$end', got EOF")
        }
        else if (tok.trim().equals(end)) {
            // consume ) token
            r.next()
            return
        }
        else {
            ml.add(read_form(r))
        }
    }
}

fun read_atom(tok: String): MalType {
    return when {
        tok == "true"        -> MalBool(true)
        tok == "false"       -> MalBool(false)
        tok.startsWith("\"") && tok.endsWith("\"") -> MalStr(tok.substring(1, tok.length-1))
        else -> {
            val l = tok.toLongOrNull()
            if (l != null) {
                return MalLong(l)
            }
            val d = tok.toDoubleOrNull()
            return if (d == null) MalSymbol(tok) else MalDouble(d)
        }
    }
}

fun read_str(s: String): MalType = read_form(Reader(tokenizer(s)))