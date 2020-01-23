package actors

import akka.actor._
import dtos.ActorResultMessage

object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
    def receive = {
        case ActorResultMessage(messageIn, successId) =>
            out ! ActorResultMessage(s"Got $messageIn", successId)
    }
}
