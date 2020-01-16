package actors

import akka.actor.ActorRef
import javax.inject.{Inject, Named, Singleton}

@Singleton
class Actors @Inject()(
	@Named("HelloParentActor") helloParentActorRef: ActorRef
) {

	lazy val helloParentActor = helloParentActorRef

}