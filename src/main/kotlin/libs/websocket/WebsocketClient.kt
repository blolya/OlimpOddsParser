package libs.websocket

import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebsocketClient {
    private val websocketClient: WebSocketClient

    constructor(url: String,
                httpHeaders: Map<String, String> = mapOf(),
                onMessage: WebSocket.(String?) -> Unit = { message -> Unit },
                onOpen: WebSocket.(ServerHandshake?) -> Unit = { handshake -> Unit },
                onClose: WebSocket.(Int, String?, Boolean) -> Unit = { code, reason, remote -> Unit },
                onError: WebSocket.(Exception?) -> Unit = { exception -> Unit }) {
        this.websocketClient = object : WebSocketClient(URI(url), httpHeaders) {
            override fun onOpen(handshake: ServerHandshake?) {
                onOpen(this, handshake)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                onClose(this, code, reason, remote)
            }

            override fun onMessage(message: String?) {
                onMessage(this, message)
            }

            override fun onError(exception: Exception?) {
                onError(this, exception)
            }
        }
    }

    fun connect() {
        this.websocketClient.connect()
    }

    fun close() {
        this.websocketClient.close()
    }

    fun send(message: String?) {
        this.websocketClient.send(message)
    }

    fun reconnect() {
        this.websocketClient.reconnect()
    }

    fun ping() {
        this.websocketClient.sendPing()
    }
}