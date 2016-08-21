package xyz.jadonfowler.pabot

import org.spacehq.packetlib.event.session.*

class PacketHandler : SessionAdapter() {

    override fun connected(event: ConnectedEvent?) {
        super.connected(event)
    }

    override fun disconnected(event: DisconnectedEvent?) {
        super.disconnected(event)
    }

    override fun disconnecting(event: DisconnectingEvent?) {
        super.disconnecting(event)
    }

    override fun packetReceived(event: PacketReceivedEvent?) {
        super.packetReceived(event)
    }

    override fun packetSent(event: PacketSentEvent?) {
        super.packetSent(event)
    }

}