openssl genpkey -algorithm RSA -out ./id_rsa -pkeyopt rsa_keygen_bits:2048
openssl rsa -in ./id_rsa -pubout -out ./id_rsa.pub