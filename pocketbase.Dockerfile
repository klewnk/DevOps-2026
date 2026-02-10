FROM alpine:latest

# Ορίζουμε την έκδοση
ARG PB_VERSION=0.22.0

# Εγκατάσταση απαραίτητων εργαλείων
RUN apk add --no-cache \
    unzip \
    ca-certificates

# Κατέβασμα του PocketBase
ADD https://github.com/pocketbase/pocketbase/releases/download/v${PB_VERSION}/pocketbase_${PB_VERSION}_linux_amd64.zip /tmp/pb.zip
RUN unzip /tmp/pb.zip -d /pb/

# Εκθέτουμε τη θύρα 8090
EXPOSE 8090

# Εκκίνηση του server
ENTRYPOINT ["/pb/pocketbase", "serve", "--http=0.0.0.0:8090"]