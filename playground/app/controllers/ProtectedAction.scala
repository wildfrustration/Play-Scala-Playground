package controllers

import javax.inject.Inject
import play.api.{Configuration, Logging}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

trait ProtectedAction extends BaseController with Logging

class Protected @Inject()(parser: BodyParsers.Default, configuration: Configuration)(implicit ec: ExecutionContext, val controllerComponents: ControllerComponents) extends ActionBuilderImpl(parser) with ProtectedAction {

    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {

        val validate = configuration.getOptional[Boolean]("validate.requests").getOrElse(true)

        if(validate) {
            request.headers.get("token").map(token => {
                logger.warn("authenticated")
                block(request)
            }).getOrElse({
                logger.warn("not authenticated")
                Future.successful(Unauthorized("missing token"))
            })
        } else {
            logger.warn("not validated")
            block(request)
        }
    }

}

