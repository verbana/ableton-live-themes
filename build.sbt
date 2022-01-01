enablePlugins(ScalaJSPlugin)

name := "Ableton Live Themes"
scalaVersion := "2.13.7"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-lang.modules" %%% "scala-xml" % "2.0.1"
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.0.0"

// Add support for the DOM in `run` and `test`
jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
