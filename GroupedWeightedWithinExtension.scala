package akka.kafka

package object scaladsl {

  implicit class GWW(
      flow: akka.stream.scaladsl.Flow[akka.kafka.ConsumerMessage.Committable,
                                      akka.kafka.ConsumerMessage.Committable,
                                      akka.NotUsed]
  ) {
    def groupedWeightedWithin(maxWeight: Long, d: scala.concurrent.duration.FiniteDuration)(
        costFn: akka.kafka.ConsumerMessage.Committable ⇒ Long
    ): akka.stream.scaladsl.Flow[akka.kafka.ConsumerMessage.Committable, scala.collection.immutable.Seq[
      akka.kafka.ConsumerMessage.Committable
    ], akka.NotUsed] =
      flow.via(
        new akka.stream.impl.fusing.GroupedWeightedWithin[akka.kafka.ConsumerMessage.Committable](maxWeight, costFn, d)
      )
  }

}
