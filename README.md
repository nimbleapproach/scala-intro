# Scala

## About

[Scala](https://www.scala-lang.org/) stands for "scalable language" - the goal being both a great object-oriented and functional language.

You can use scala as just a better Java (or better Kotlin! ;-) ), but with its advanced pattern-matching/type system, it really flurishes when
used with a functional programming style.

## Getting Started

Scala runs on the JVM, so you first will need a Java JDK installed, perhaps using:
 * brew
 * sdkman
 * basic download/install
 * use a zero-install container build

# Building a Scala Project
With Java installed and on your command-line, you can then install scala in a number of ways.

The [Scala website](https://www.scala-lang.org/) has good instructions on how to install

The scala ecosystem has 
 * [scala-cli](https://scala-cli.virtuslab.org/): A great, simple command-line interface
 * [Mill](https://github.com/com-lihaoyi/mill)
 * [Maven](https://docs.scala-lang.org/tutorials/scala-with-maven.html)
```
     mvn archetype:generate -DarchetypeGroupId=net.alchim31.maven -DarchetypeArtifactId=scala-archetype-simple
```
 * [SBT](https://www.scala-sbt.org/)
 * using a [docker-build](mill-example/dockerBuild.sh)
```
    docker run --rm --mount type=bind,source="$(pwd)",target=/opt  nightscape/scala-mill /bin/sh -c 'cd /opt; mill _.test'
```