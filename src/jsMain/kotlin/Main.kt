import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.rows
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLTextAreaElement

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
                        val box = document.getElementById("message_box")!! as HTMLTextAreaElement
                        val content = box.clear()
                        if (content.isBlank()) {
                            return@launch
                        }
                        emitter.emit(Message(Role.USER, content))
                        val resp = Services.GPT.send(content)
                        emitter.emit(Message(Role.GPT, resp))
                    }
                }
                TextArea {
                    id("message_box")
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
                        property("overflow-wrap", "break-word")
                        property("resize", "none")
                    }
                    attr("placeholder", "按下\"Ctrl+Enter\"发送")
                    rows(1)
                    onInput {
                        it.target.resize()
                    }
                    onKeyDown {
                        if (it.ctrlKey && it.key == "Enter") {
                            it.preventDefault()
                            send()
                        }
                    }
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

