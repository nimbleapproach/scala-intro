#!/usr/bin/env bash

# This script is a means to compile/test the code via docker (so no need to install Mill)
docker run --rm --mount type=bind,source="$(pwd)",target=/opt  nightscape/scala-mill /bin/sh -c 'cd /opt; mill _.test'
