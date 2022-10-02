FROM golang:alpine AS builder

RUN apk update \
    && apk --no-cache --update add build-base 

RUN apk add pkgconfig git

WORKDIR /go/src/app

COPY go.mod ./
COPY go.sum ./

RUN go mod download

COPY ./pkg ./pkg
COPY ./cmd ./cmd

RUN CGO_ENABLED=1 GOOS=linux go build -o producer ./cmd/

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /root/

COPY --from=builder /go/src/app/producer .

CMD ["./producer"]
