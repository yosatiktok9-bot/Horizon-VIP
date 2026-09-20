#!/bin/sh
# Horizon VIP Gradle wrapper launcher
APP_HOME=$( cd "$(dirname "$0")" && pwd -P ) || exit 1
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD=java
fi
exec "$JAVACMD" -Xmx64m -Xms64m ${JAVA_OPTS} ${GRADLE_OPTS} \
  -Dorg.gradle.appname=gradlew \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
