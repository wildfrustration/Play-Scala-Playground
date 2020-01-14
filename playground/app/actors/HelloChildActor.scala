package actors

import akka.actor._
import com.google.inject.assistedinject.Assisted
import javax.inject.Inject
import play.api.Configuration


class HelloChildActor @Inject()(@Assisted param: String, config: Configuration) extends Actor {

	import HelloChildActor._

	def receive = {
		case SayHello(name: String) =>
			sender() ! s"Hello, $name,  My name is: ${HelloChildActor.getClass.getName}, random string: $param"
			self ! PoisonPill

		case GetConfig(path: String) =>
			sender() ! s"Config ${path}:${config.getOptional[String](path).getOrElse("")}, random string: $param"
			self ! PoisonPill
	}
}

object HelloChildActor {

	def props = Props[HelloChildActor]

	case class SayHello(name: String)

	case class GetConfig(path: String)

	trait Factory {
		def apply(param: String): Actor
	}

}