object Deps {
  const val discord4j = "com.discord4j:discord4j-core:${Versions.discord4j}"
  const val guice = "com.google.inject:guice:${Versions.guice}"
  const val guava = "com.google.guava:guava:${Versions.guava}"
  const val reactor_extra = "io.projectreactor.addons:reactor-extra"
  const val reactor_core = "io.projectreactor:reactor-core"
  const val classgraph = "io.github.classgraph:classgraph:${Versions.classgraph}"
  const val mbassador = "net.engio:mbassador:${Versions.mbassador}"
  const val intake = "com.sk89q.intake:intake:${Versions.intake}"

  object Jackson {
    const val databind = "com.fasterxml.jackson.core:jackson-databind:${Versions.Jackson.databind}"

    object Dataformat {
      const val yaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.Jackson.Dataformat.yaml}"
      const val xml = "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${Versions.Jackson.Dataformat.xml}"

    }
  }

  object Test {
    object JUnit {
      const val api = "org.junit.jupiter:junit-jupiter-api:${Versions.Test.JUnit.api}"
      const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.Test.JUnit.engine}"
    }

    const val assertj = "org.assertj:assertj-core:${Versions.Test.assertj}"
  }
}

object Versions {
  const val discord4j = "3.0.6"
  const val guice = "4.2.2"
  const val guava = "27.1-jre"
  const val reactor_bom = "Dysprosium-M2"
  const val classgraph = "4.8.29"
  const val mbassador = "1.3.2"
  const val intake = "4.0-SNAPSHOT"

  object Jackson {
    const val databind = "2.9.9"

    object Dataformat {
      const val yaml = "2.9.9"
      const val xml = "2.9.9"
    }
  }

  object Test {
    object JUnit {
      const val api = "5.3.1"
      const val engine = "5.3.1"
    }

    const val assertj = "3.11.1"
  }
}