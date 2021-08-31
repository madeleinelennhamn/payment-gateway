# Payment Gate Way
Hakim har anställt en arme av konsulter för att bygga en payment gateway. Pengarna tog slut och detta var det som teamet producerade och ni har fått ta över tjänsten och göra lite vad ni vill med den.

Denna tjänst tar hand om betalningar. Paymentteamet har inte kommit så långt utan markerar bara betalningarna automatiskt som betalda efter 10 sekunder. 

# API
Apiet är beskrivet via Swagger som ni kan hitta direkt under `<host>/swagger-ui/`

# Hur det fungerar

1. Du skapar en betalning och tillhandahåller en summa och en referens.
2. När betalningen har gått igenom, så skickas ett meddelande på kafka på topicen `payment`

# Testa

1. `docker-compose up -d` för att starta appen, databasen och kafka
2. Kör `kafkacat -b localhost:29092 -C -t payment` och installera kafkacat om det inte funkar
3. Gå till swagger och trigga en betalning
4. Kolla vad kafkacat skriver ut
5. kolla i loggarna vad tjänsten skriver ut `docker-compose logs -f payment-gw`

# Arkitektur

![alt text](./PGW.jpg)

Rest finns dokumenterat i swagger men här kommer ett exempel.
```
POST /payment
{
    "reference": "your-reference-eg-the-orderId", 
    "amount": 100
}

Returns 204 if successfull with no body.
```

Messaging:
På topicen "payment" skickas alla olika state-förändringar på betalningen. Idag har teamet lyckats att implementera två olika
* CREATED: Skickas när en betalning har registrerats
* PAID: Skickas när en betalning är utförd.

Message format
```json
{
    "reference":"your-reference-eg-the-orderId",
    "paymentId":"123",
    "status":"CREATED"
}
```

# Notes from the team
Det finns ett bra bibliotek spring-boot-kafka som kan hjälpa till om man inte vill skriva sina kafka consumers från scratch. Annars så finns det ett exempel om hur man gör det i vårat integrationstest.
