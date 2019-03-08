package fix

import scalafix.v1._
import scala.meta._

class ChangeAkkaVersion extends SyntacticRule("ChangeAkkaVersion") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case _ @ Defn.Val(_, _ @ List(Pat.Var(Term.Name("akkaVersion"))), _, version) =>
        Patch.replaceTree(version, """"2.4.20"""")
    }.asPatch
  }
}
