import org.w3c.dom.HTMLTextAreaElement

inline fun HTMLTextAreaElement.resize() {
    style.height = "auto"
    if (value.lines().size > 1) {
        style.height = "${scrollHeight}px"
    }
}

inline fun HTMLTextAreaElement.clear(): String = value.let {
    value = ""
    resize()
    it
}
