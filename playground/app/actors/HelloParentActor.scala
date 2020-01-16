package actors

import akka.actor._
import javax.inject.Inject
import play.api.libs.concurrent.InjectedActorSupport
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._



class HelloParentActor @Inject()(helloChildFactory: HelloChildActor.Factory) extends Actor with InjectedActorSupport {

	import actors.HelloChildActor._
	implicit val timeout: Timeout = 15.seconds
	implicit val ec: ExecutionContext = ExecutionContext.global

	override def preStart(): Unit = {
		println("PRE START")
	}

	def receive = {

		case SayHello(name: String) => {
			val senderRef = sender()
			val child:ActorRef = injectedChild(helloChildFactory("SayHelloParam"), "HelloChildActor")
			(child ? SayHello(name)).mapTo[String].map { message =>
				senderRef ! message
			}
		}

		case GetConfig(path: String) => {
			val senderRef = sender()
			val child:ActorRef = injectedChild(helloChildFactory("GetConfigParam"), "HelloChildActor")
			(child ? GetConfig(path)).mapTo[String].map { message =>
				senderRef ! message
			}
		}
	}
}

object HelloParentActor {

	def props = Props[HelloParentActor]

}
