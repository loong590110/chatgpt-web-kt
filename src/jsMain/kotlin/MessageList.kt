import androidx.compose.runtime.Composable
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
fun MessageList(
    attrs: AttrBuilderContext<HTMLDivElement>? = null
) {
    MainScope().launch {
        emitter.filterIsInstance<Message>().collect {
            val parent = document.getElementById("message_list")!!
            val child = document.createElement("div") as HTMLDivElement
            child.style.apply {
                marginLeft = "10px"
                marginRight = "10px"
                marginBottom = "10px"
                padding = "10px"
                color = "#fff"
                background = if (it.role == Role.USER) "#456" else "#555"
                borderRadius = "10px"
                fontSize = "10pt"
                cssFloat = if (it.role == Role.USER) "right" else "left"
                width = "fit-content"
                maxWidth = "80%"
                overflowWrap = "break-word"
                wordWrap = "break-word"
                whiteSpace = "pre-wrap"
                clear = "both"
                boxShadow = "0px 0px 10px 0px rgba(0,0,0,0.25)"
            }
            val content = it.content.replace("<", "&lt")
                .replace(">", "&gt")
            child.innerHTML = MessageParser.parse(content).html
            parent.appendChild(child)
            parent.scrollTop = parent.scrollHeight.toDouble()
        }
    }
    Div({
        id("message_list")
        style {
            overflowY("auto")
            paddingTop(10.px)
            paddingBottom(66.px)
        }
        attrs?.invoke(this)
    })
}

data class Message(val role: Role, val content: String)

enum class Role { USER, GPT }