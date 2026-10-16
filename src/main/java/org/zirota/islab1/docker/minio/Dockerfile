
FROM golang:1.24-bookworm AS builder

ARG MINIO_VERSION=RELEASE.2025-10-15T17-29-55Z

RUN CGO_ENABLED=0 go install github.com/minio/minio@${MINIO_VERSION}

FROM debian:bookworm-slim

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /go/bin/minio /usr/local/bin/minio

RUN useradd --system --uid 10001 --home-dir /data minio \
    && mkdir -p /data \
    && chown minio:minio /data

USER minio

EXPOSE 9000 9001

ENTRYPOINT ["minio"]
CMD ["server", "/data", "--console-address", ":9001"]
