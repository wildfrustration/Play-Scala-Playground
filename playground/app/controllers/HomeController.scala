package controllers

import actors.HelloChildActor._
import actors.{Actors, HelloParentActor2, MyWebSocketActor}
import akka.actor.ActorSystem
import akka.pattern._
import akka.stream.Materializer
import akka.util.Timeout
import dtos.ActorResultMessage
import dtos.JsonFormats._
import javax.inject.{Inject, _}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class HomeController @Inject()(actors: Actors, cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

	implicit val timeout: Timeout = 15.seconds
    implicit val ec: ExecutionContext = ExecutionContext.global

	def greeter() = Action.async(parse.json) { implicit request =>

		//val body: AnyContent = request.body
		val jsonBody: JsValue = request.body

		val parent = system.actorOf(HelloParentActor2.props, "HelloParentActor2")

		(parent ? SayHello((jsonBody \ "name").as[String])).mapTo[String].map { message =>
			Ok(Json.toJson(ActorResultMessage(message, true)))
		}

	}

	def config(path: String) = Action.async(parse.json) { implicit request =>

		(actors.helloParentActor ? GetConfig(path)).mapTo[String].map { message =>
			Ok(Json.toJson(ActorResultMessage(message, false)))
		}

	}

	implicit val messageFlowTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[ActorResultMessage, ActorResultMessage]

	def socket = WebSocket.accept[ActorResultMessage, ActorResultMessage] { request =>
		ActorFlow.actorRef { out =>
			MyWebSocketActor.props(out)
		}
	}

}
