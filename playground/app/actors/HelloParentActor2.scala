package actors

import akka.actor._
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


class HelloParentActor2 extends Actor {

	import actors.HelloChildActor._
	implicit val timeout: Timeout = 15.seconds
	implicit val ec: ExecutionContext = ExecutionContext.global

	def receive = {

		case SayHello(name: String) => {
			sender() ! s"Hello $name"
			self ! PoisonPill
		}

		case GetConfig(path: String) => {
			sender() ! s"Config: $path"
			self ! PoisonPill
		}
	}
}

object HelloParentActor2 {

	def props = Props[HelloParentActor2]

}



