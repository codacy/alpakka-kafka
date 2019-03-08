package fix

import scalafix.v1._
import scala.meta._

class CleanAkkaStreamOps extends SyntacticRule("CleanAkkaStreamOps") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case obj @ Defn.Object(_, Term.Name("GroupedWeightedWithin"), _) =>
        Seq()
      case obj @ Defn.Object(_, _, _) =>
        Seq(Patch.replaceTree(obj, ""))
      case cls @ Defn.Class(_, Type.Name("GroupedWeightedWithin"), _, _, _) =>
        Seq()
      case cls @ Defn.Class(_, _, _, _, _) =>
        Seq(Patch.replaceTree(cls, ""))
    }.flatten.asPatch
  }
}
