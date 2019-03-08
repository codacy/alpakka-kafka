ALPAKKA_KAFKA_VERSION=1.0.1
AKKA_VERSION=2.5.21

all: build

.PHONY: build
build: ## prepare the environment and cross-compile
	mkdir -p bin
	wget -nc -O bin/coursier https://git.io/coursier-cli && chmod +x bin/coursier
	bin/coursier bootstrap -J "-Xss100m" ch.epfl.scala:scalafix-cli_2.12.8:0.9.4 -f --main scalafix.cli.Cli -o bin/scalafix
	# TODO: fix the branch from env vars or something
	git clone --branch v$(ALPAKKA_KAFKA_VERSION) https://github.com/akka/alpakka-kafka.git
	git clone --branch  v$(AKKA_VERSION) --depth 1 https://github.com/akka/akka
	mkdir -p alpakka-kafka/core/src/main/scala/akka/util
	mkdir -p alpakka-kafka/core/src/main/scala/akka/actor/dungeon
	mkdir -p alpakka-kafka/core/src/main/scala/akka/stream/impl/fusing
	cp codacy-plugin.sbt alpakka-kafka/project/codacy-plugin.sbt
	cp GroupedWeightedWithinExtension.scala alpakka-kafka/core/src/main/scala/akka/kafka/scaladsl/GroupedWeightedWithinExtension.scala
	cp JavaDurationConverters.scala alpakka-kafka/core/src/main/scala/akka/util/JavaDurationConverters.scala
	cp DefaultAttributes.scala alpakka-kafka/core/src/main/scala/akka/stream/impl/fusing/DefaultAttributes.scala
	cp akka/akka-actor/src/main/scala/akka/actor/Timers.scala alpakka-kafka/core/src/main/scala/akka/actor/Timers.scala
	cp akka/akka-actor/src/main/scala/akka/actor/dungeon/TimerSchedulerImpl.scala alpakka-kafka/core/src/main/scala/akka/actor/dungeon/TimerSchedulerImpl.scala
	cp akka/akka-stream/src/main/scala/akka/stream/impl/fusing/Ops.scala alpakka-kafka/core/src/main/scala/akka/stream/impl/fusing/GroupedWeightedWithin.scala
	bin/scalafix -r "file://$(PWD)/ChangeAkkaVersion.scala" --files alpakka-kafka/build.sbt
	bin/scalafix -r "file://$(PWD)/AddJava8Compat.scala" --files alpakka-kafka/build.sbt
	bin/scalafix -r "file://$(PWD)/CleanAkkaStreamOps.scala" --files alpakka-kafka/core/src/main/scala/akka/stream/impl/fusing/GroupedWeightedWithin.scala
	bin/scalafix -r "file://$(PWD)/MoreSbtFixes.scala" --files alpakka-kafka/build.sbt
	bin/scalafix -r "file://$(PWD)/RemoveScalafmt.scala" --files alpakka-kafka/project/plugins.sbt
	(cd alpakka-kafka && sbt ";retrieveGPGKeys ;++2.11.12 ; set ThisBuild / isSnapshot := false; set ThisBuild / version := \"$(ALPAKKA_KAFKA_VERSION)\"; core/compile; core/publishSigned; sonatypeRelease")

.PHONY: clean
clean: ## clean target directories
	rm -rf bin
	rm -rf akka
	rm -rf alpakka-kafka

.PHONY: help
help:
	@echo "make help"
	@echo "\n"
	@grep -E '^[a-zA-Z_/%\-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo "\n"


curl -Lo bin/coursier https://git.io/coursier-cli && chmod +x bin/coursier