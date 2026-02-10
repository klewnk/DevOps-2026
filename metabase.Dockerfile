# Χρησιμοποιούμε το επίσημο ελαφρύ image του Metabase
FROM metabase/metabase:latest

# Ορίζουμε τη θύρα που ακούει το Metabase εσωτερικά
EXPOSE 3000

# Μπορείς να προσθέσεις δικές σου ρυθμίσεις αν χρειαστεί στο μέλλον
# Για τώρα, απλά εκκινούμε την εφαρμογή
ENTRYPOINT ["/app/run_metabase.sh"]