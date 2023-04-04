## Validate

Use the following commands to validate the app:

Get an access-token from keycloak:

ACCESS_TOKEN="$(curl -s -X POST "http://localhost:48080/realms/lifesaving/protocol/openid-connect/token" 
  -H "Content-Type: application/x-www-form-urlencoded" 
  -d "username=dennis" -d "password=dennis" -d "grant_type=password" 
  -d "client_id=lifesaving-results-webapp" | jq -r .access_token)"
echo $ACCESS_TOKEN

# HTTP/1.1 401 -> denied

curl -i -X POST "http://localhost:9001/api/competition" -H "Content-Type: application/json" -d '{ "name":"Alphabet", "acronym": "abc" }'

# HTTP/1.1 200 -> ok

curl -i -X POST "http://localhost:9001/api/competition" -H "Authorization: Bearer $ACCESS_TOKEN" -H "Content-Type:application/json" -d '{ "name":"Alphabet", "acronym": "abc" }'
