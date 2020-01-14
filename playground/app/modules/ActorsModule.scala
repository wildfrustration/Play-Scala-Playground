package modules

import actors._
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class ActorsModule extends AbstractModule with AkkaGuiceSupport {

	override def configure = {
		//bindActor[HelloChildActor]("HelloChildActor")
		bindActor[HelloParentActor]("HelloParentActor")
		bindActorFactory[HelloChildActor, HelloChildActor.Factory]
	}
}