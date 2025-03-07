curl -X POST "http://localhost:8080/api/auth/sign-in" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com"
         }'
