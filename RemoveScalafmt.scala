package fix

import scalafix.v1._
import scala.meta._

class RemoveScalafmt extends SyntacticRule("RemoveScalafmt") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case addSbtPlugin @ Term.Apply(Name("addSbtPlugin"), List(Term.ApplyInfix(Term.ApplyInfix(Lit.String("com.geirsson"),Term.Name("%"),List(),List(Lit.String("sbt-scalafmt"))), _, _, _))) =>
        Patch.replaceTree(addSbtPlugin, "")
    }.asPatch
  }
}
