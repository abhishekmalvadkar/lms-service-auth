curl -X POST "http://localhost:9091/api/auth/verify-otp" \
     -H "Content-Type: application/json" \
     -H "device: web" \
     -d '{
           "email": "john.doe@example.com",
           "otp": "123456"
         }'
