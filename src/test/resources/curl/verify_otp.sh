curl -X POST "http://localhost:8080/api/auth/verify-otp" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com",
           "otp": "123456"
         }'
