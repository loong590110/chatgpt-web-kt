object MessageParser {
    fun parse(message: String): Element {
        val codeIdentify = "```"
        val codeStartIndex = message.indexOf(codeIdentify) + codeIdentify.length
        if (codeStartIndex >= codeIdentify.length) {
            val codeEndIndex = message.indexOf(codeIdentify, codeStartIndex)
            if (codeEndIndex != -1) {
                console.log("($codeStartIndex, $codeEndIndex)")
                val codeOrigin = message.substring(codeStartIndex, codeEndIndex)
                val restOrigin = message.substring(codeEndIndex + codeIdentify.length)
                val firstElement = TextElement(message.substring(0, codeStartIndex - codeIdentify.length))
                val lastElement = CodeElement(codeOrigin)
                firstElement.next = lastElement
                return parse(restOrigin, firstElement, lastElement)
            }
        }
        return TextElement(message)
    }

    private fun parse(message: String, firstElement: Element, lastElement: Element): Element {
        val codeIdentify = "```"
        val codeStartIndex = message.indexOf(codeIdentify) + codeIdentify.length
        if (codeStartIndex >= codeIdentify.length) {
            val codeEndIndex = message.indexOf(codeIdentify, codeStartIndex)
            if (codeEndIndex != -1) {
                console.log("($codeStartIndex, $codeEndIndex)")
                val codeOrigin = message.substring(codeStartIndex, codeEndIndex)
                val restOrigin = message.substring(codeEndIndex + codeIdentify.length)
                val firstElement2 = TextElement(message.substring(0, codeStartIndex - codeIdentify.length))
                val lastElement2 = CodeElement(codeOrigin)
                lastElement.next = firstElement2
                firstElement2.next = lastElement2
                return parse(restOrigin, firstElement, lastElement2)
            }
        }
        lastElement.next = TextElement(message)
        return firstElement
    }
}

class CodeElement(origin: String, next: Element? = null) : Element(origin, next) {
    override val html: String
        get() = "<pre><div class=\"code\">${origin.trim()}</div></pre>${nextHtml}"
}

class TextElement(origin: String, next: Element? = null) : Element(origin, next) {
    override val html: String
        get() = origin.trim() + nextHtml
}

abstract class Element(val origin: String, var next: Element?) {
    abstract val html: String
    val nextHtml: String get() = if (next == null) "" else next!!.html
}

