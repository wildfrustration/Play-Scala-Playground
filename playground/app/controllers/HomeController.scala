package controllers

import actors.Actors
import actors.HelloChildActor._
import akka.actor.ActorSystem
import akka.pattern._
import akka.util.Timeout
import javax.inject._
import play.api.libs.json.JsValue
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


@Singleton
class HomeController @Inject()(actors: Actors, cc: ControllerComponents) extends AbstractController(cc) {

	implicit val timeout: Timeout = 15.seconds
    implicit val ec: ExecutionContext = ExecutionContext.global

	def greeter() = Action.async(parse.json) { implicit request =>

		//val body: AnyContent = request.body
		val jsonBody: JsValue = request.body

		(actors.helloParentActor ? SayHello((jsonBody \ "name").as[String])).mapTo[String].map { message =>
			Ok(message)
		}

	}

	def config(path: String) = Action.async(parse.json) { implicit request =>

		(actors.helloParentActor ? GetConfig(path)).mapTo[String].map { message =>
			Ok(message)
		}

	}
}
