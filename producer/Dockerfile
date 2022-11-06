FROM golang:alpine AS builder

ENV GO111MODULE=on \
    CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64

WORKDIR /build

COPY go.mod go.sum ./
COPY cmd ./cmd
COPY pkg ./pkg

RUN go mod download
RUN go build -o producer ./cmd

FROM scratch

COPY --from=builder /build/producer /producer

ENTRYPOINT ["/producer"]