package akka.util

import scala.compat.java8.DurationConverters

object JavaDurationConverters {

  implicit final class DurationOps(val duration: java.time.Duration) extends AnyVal {
    def asScala: scala.concurrent.duration.FiniteDuration = DurationConverters.toScala(duration)
  }

  implicit final class FiniteDurationops(val duration: scala.concurrent.duration.FiniteDuration) extends AnyVal {
    def asJava: java.time.Duration = DurationConverters.toJava(duration)
  }

}
