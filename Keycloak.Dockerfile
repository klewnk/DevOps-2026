FROM quay.io/keycloak/keycloak:23.0.7

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

ENV KC_DB=dev-file

WORKDIR /opt/keycloak

RUN /opt/keycloak/bin/kc.sh build

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]