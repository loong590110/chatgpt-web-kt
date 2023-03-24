import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

fun main() {
    renderComposable(rootElementId = "root") {
        Div({
            style {
                width(100.vw)
                height(100.vh)
                backgroundColor(Color("#333"))
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
            }
        }) {
            val showMessageHint = { show: Boolean ->
                val h = document.getElementById("message_hint") as HTMLElement
                h.style.display = if (show) "flex" else "none"
            }
            MessageList {
                style {
                    flex(1)
                }
            }
            Div({
                style {
                    height(auto)
                    position(Position.Fixed)
                    left(20.px)
                    right(20.px)
                    bottom(20.px)
                    padding(6.px)
                    backgroundColor(Color("#555"))
                    borderRadius(8.px)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    alignItems(AlignItems.Center)
                    property("box-shadow", "0px 20px 20px 20px rgba(0,0,0,0.9)")
                }
            }) {
                val send = {
                    MainScope().launch {
                        val box = document.getElementById("message_box")!! as HTMLDivElement
                        if (box.innerText.isNullOrBlank()) {
                            return@launch
                        }
                        val content = box.innerText!!
                        box.innerText = ""
                        showMessageHint(true)
                        emitter.emit(Message("user", content))
                        val resp = Services.GPT.send(Message("user", content))
                        emitter.emit(Message("gpt", resp))
                    }
                }
                Div({
                    id("message_box")
                    contentEditable(true)
                    style {
                        height(auto)
                        maxHeight(180.px)
                        flex(1)
                        overflowY("auto")
                        backgroundColor(Color("#0000"))
                        outline("none")
                        border(0.px)
                        color(Color("#fff"))
                        fontSize(12.pt)
                        whiteSpace("pre-wrap")
                    }
                    onKeyUp {
                        if (it.ctrlKey && it.key == "Enter") {
                            send()
                        }
                    }
                    onFocusIn {
                        showMessageHint(false)
                    }
                    onFocusOut {
                        val div = it.target as HTMLDivElement
                        if (div.innerText.isNullOrBlank()) {
                            showMessageHint(true)
                        }
                    }
                })
                H4({
                    id("message_hint")
                    style {
                        height(36.px)
                        position(Position.Absolute)
                        display(DisplayStyle.Flex)
                        alignItems(AlignItems.Center)
                        property("margin-top", "auto")
                        marginBottom(0.px)
                        color(Color("#999"))
                        property("pointer-events", "none")
                    }
                }) {
                    Text("按下\"Ctrl+Enter\"发送")
                }
                Button(attrs = {
                    style {
                        width(56.px)
                        height(36.px)
                        marginLeft(2.px)
                        property("margin-top", "auto")
                        backgroundColor(Color("#456"))
                        border(0.px)
                        borderRadius(6.px)
                        color(Color("#fff"))
                        fontSize(10.pt)
                    }
                    onClick {
                        send()
                    }
                }) {
                    Text("发送")
                }
            }
        }
    }
}

