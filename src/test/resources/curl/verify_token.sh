curl -X POST "http://localhost:8080/api/auth/verify-token" \
     -H "Content-Type: application/json" \
     -d '{
           "token": "your_jwt_token_here"
         }'
