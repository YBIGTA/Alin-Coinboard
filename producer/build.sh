#! /bin/sh

# This script is used to build the producer application


# for Mac OS
env GOOS="darwin" GOARCH="arm64" CGO_ENABLED="1" go build -o ./bin/start-producer-darwin-arm64 ./cmd/. 
env GOOS="darwin" GOARCH="amd64" CGO_ENABLED="1" go build -o ./bin/start-producer-darwin-amd64 ./cmd/. 

# for windows
# need mingw-w64 to build windows binary in macOS (@see https://stackoverflow.com/a/36916044)
env GOOS="windows" GOARCH="amd64" CGO_ENABLED="1" CC="x86_64-w64-mingw32-gcc" go build -o ./bin/start-producer-windows-amd64.exe ./cmd/.

# for linux
# env GOOS="linux" GOARCH="arm64" CGO_ENABLED="1" go build -o ./bin/start-producer-linux-arm64 ./cmd/.
# env GOOS="linux" GOARCH="amd64" CGO_ENABLED="1" go build -o ./bin/start-producer-linux-amd64 ./cmd/.
