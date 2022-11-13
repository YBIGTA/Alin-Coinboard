FROM golang:latest AS builder

ENV GO111MODULE=on \
    CGO_ENABLED=1 \
    GOOS=linux \
    GOARCH=amd64

WORKDIR /go/src/producer

RUN apt-get update && apt-get install -y \
    build-essential 

COPY go.mod go.sum ./
COPY cmd ./cmd
COPY pkg ./pkg

RUN go mod download

RUN go build -o producer ./cmd

FROM scratch

COPY --from=builder /go/src/producer/producer /producer

ENTRYPOINT ["/producer"]