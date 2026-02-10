FROM alpine:latest

RUN apk add --no-cache unzip ca-certificates

# Κατεβάζουμε το PocketBase
ADD https://github.com/pocketbase/pocketbase/releases/download/v0.22.0/pocketbase_0.22.0_linux_amd64.zip /tmp/pb.zip
RUN unzip /tmp/pb.zip -d /pb/

EXPOSE 8090

# Το τρέχουμε σε HTTP mode χωρίς περιορισμούς
ENTRYPOINT ["/pb/pocketbase", "serve", "--http=0.0.0.0:8090"]