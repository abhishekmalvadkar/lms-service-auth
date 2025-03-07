curl -X POST "http://localhost:8080/api/auth/create-account" \
     -H "Content-Type: application/json" \
     -d '{
           "firstName": "John",
           "lastName": "Doe",
           "email": "john.doe@example.com"
         }'
