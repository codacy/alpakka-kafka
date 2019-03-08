package fix

import scalafix.v1._
import scala.meta._

class AddJava8Compat extends SyntacticRule("AddJava8Compat") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case _ @ Defn.Val(_, _ @ List(Pat.Var(Term.Name("core"))), _, body) =>
        Patch.addRight(body, """.settings(publicMvnPublish).settings(libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0")""")
    }.asPatch
  }
}
