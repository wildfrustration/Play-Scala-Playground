package dtos

import play.api.libs.json._

case class ActorResultMessage(result: String, success: Boolean)

object JsonFormats {

	implicit val actorResultMessageFormat = Json.format[ActorResultMessage]

}