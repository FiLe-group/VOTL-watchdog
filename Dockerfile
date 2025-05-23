# Build container

FROM eclipse-temurin:21-alpine AS build
# Add tools to run gradle wrapper
RUN apk add --no-cache bash unzip coreutils

WORKDIR /app

COPY . .

RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

RUN ./gradlew build --no-daemon

# Run container

FROM eclipse-temurin:21-alpine AS runtime

WORKDIR /opt/wdvotl

ARG UID=10102
RUN adduser \
    --disabled-password \
    --gecos "" \
    --uid "${UID}" \
    wdvotl && \
    chown wdvotl:wdvotl -R /opt/wdvotl && \
    chmod u+w /opt/wdvotl && \
    chmod 0755 -R /opt/wdvotl

USER wdvotl

COPY --from=build /app/VOTL-watchdog.jar /bin/

CMD ["java","-jar","/bin/VOTL-watchdog.jar"]