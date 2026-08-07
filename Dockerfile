# syntax=docker/dockerfile:1.7

# ---------- Build container ----------
# Pinned to BUILDPLATFORM and deliberately free of any TARGET* ARG, so BuildKit
# treats this stage as identical for every target platform and runs it ONCE.
# The shadow jar is pure JVM bytecode and therefore architecture independent.
FROM --platform=$BUILDPLATFORM eclipse-temurin:25-alpine AS build

# Add tools to run gradle wrapper
RUN apk add --no-cache bash unzip coreutils

WORKDIR /app

# Resolve dependencies first: this layer only changes when the build scripts do.
COPY gradlew gradle.properties settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN --mount=type=cache,target=/root/.gradle,sharing=locked \
    ./gradlew --no-daemon -q dependencies --configuration runtimeClasspath

# Sources change most often, so they come last.
COPY src ./src
RUN --mount=type=cache,target=/root/.gradle,sharing=locked \
    ./gradlew --no-daemon shadowJar

# ---------- Run container ----------
# JRE instead of full JDK: same runtime, roughly half the image size.
FROM eclipse-temurin:25-jre-alpine AS runtime

LABEL org.opencontainers.image.title="VOTL-watchdog" \
      org.opencontainers.image.description="Watchdog process that keeps the VOTL bot updated and running" \
      org.opencontainers.image.source="https://github.com/FiLe-group/votl-watchdog" \
      org.opencontainers.image.licenses="Apache-2.0"

WORKDIR /opt/wdvotl

ARG GID=10102
ARG UID=10101

RUN addgroup --gid "${GID}" wdvotl && \
    adduser \
      --disabled-password \
      --gecos "" \
      --ingroup wdvotl \
      --uid "${UID}" \
      votl && \
    mkdir -p /opt/wdvotl/data /opt/wdvotl/logs && \
    chown -R "${UID}:${GID}" /opt/wdvotl && \
    chmod -R 0775 /opt/wdvotl

COPY --from=build --chown=root:root --chmod=0444 \
     /app/build/libs/VOTL-watchdog-*.jar /opt/wdvotl/VOTL-watchdog.jar

USER votl

# Exec form keeps java as PID 1, so signals (and therefore `docker stop`) work.
# MaxRAMPercentage makes the heap follow the container limit instead of the host.
ENTRYPOINT ["java", \
            "-XX:MaxRAMPercentage=75", \
            "-XX:+ExitOnOutOfMemoryError", \
            "-jar","/opt/wdvotl/VOTL-watchdog.jar"]