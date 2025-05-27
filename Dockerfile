# Build container
FROM --platform=$BUILDPLATFORM eclipse-temurin:21-alpine AS build
ARG TARGETOS
ARG TARGETARCH

# Add tools to run gradle wrapper
RUN apk add --no-cache bash unzip coreutils

WORKDIR /app
COPY . .

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew build --no-daemon

# Run container
FROM eclipse-temurin:21-alpine AS runtime

WORKDIR /opt/wdvotl

ARG GID=10102
ARG UID=10101

RUN addgroup \
    --gid "${GID}" \
    wdvotl
RUN adduser \
    --disabled-password \
    --gecos "" \
    --ingroup wdvotl \
    --uid "${UID}" \
    votl && \
    chown ":${GID}" -R /opt/wdvotl && \
    chmod u+w /opt/wdvotl && \
    chmod 0775 -R /opt/wdvotl

USER votl

COPY --from=build /app/VOTL-watchdog.jar /opt/wdvotl/

ENTRYPOINT ["java","-jar","/opt/wdvotl/VOTL-watchdog.jar"]