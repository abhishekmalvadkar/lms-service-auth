curl -X POST "http://localhost:8080/api/auth/verify-account" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com",
           "verificationToken": "123456"
         }'
