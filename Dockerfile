FROM ubuntu:latest

COPY target/bgg-api /bgg-api

ENTRYPOINT ["/bgg-api"]