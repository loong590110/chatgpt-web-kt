import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Services {
    companion object {
        private val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
        private val token by lazy {
            val c = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            var s = ""
            (0 until 16).forEach { _ ->
                s += c.random()
            }
            s
        }
    }

    object GPT {
        private const val url = "https://chat.landray.vip/msg"
        suspend fun send(message: Message): String {
            return httpClient.post(url) {
                headers {
                    set("Content-Type", "application/json")
                }
                setBody(Payload(token, message.content, "60"))
            }.body()
        }

        @Serializable
        data class Payload(
            @SerialName("token")
            val token: String,
            @SerialName("msg")
            val message: String,
            @SerialName("timeout")
            val timeout: String
        )
    }
}