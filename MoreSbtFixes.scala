package fix

import scalafix.v1._
import scala.meta._

class MoreSbtFixes extends SyntacticRule("MoreSbtFixes") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case bench @ Defn.Val(_, _ @ List(Pat.Var(Term.Name("benchmarks"))), _, body) =>
        Seq(Patch.replaceTree(body, """project"""))
      case sfmt @ Term.Name("scalafmtOnCompile") =>
        Seq(Patch.replaceTree(sfmt, """scalacOptions -= "-Xfatal-warnings", //"""))
      case commonSettings @ Defn.Val(_, _ @ List(Pat.Var(Term.Name("commonSettings"))), _, body) =>
        body.collect {
          case org @ Term.Name("organization") =>
            Patch.replaceTree(org, """organization := "com.codacy", pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE")).map(_.toCharArray), //""")
        }
    }.flatten.asPatch
  }
}
