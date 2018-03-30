FROM maven:3-jdk-8

RUN apt-get update && apt-get install -y --no-install-recommends \
		openjfx \
&& rm -rf /var/lib/apt/lists/*

VOLUME "$USER_HOME_DIR/.m2"
