package actors

import akka.actor.{ActorRef, ActorSystem}
import javax.inject.{Inject, Named, Singleton}
import play.api.Configuration

@Singleton
class Actors @Inject()(
	system: ActorSystem,
	config: Configuration,
	@Named("HelloParentActor") helloParentActorRef: ActorRef
) {

	val helloParentActor = helloParentActorRef

}