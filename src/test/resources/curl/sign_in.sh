curl -X POST "http://localhost:9091/api/auth/sign-in" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com"
         }'
