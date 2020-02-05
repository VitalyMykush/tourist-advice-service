# tourist-advice-service

REST API

Получить все города 
GET     /cities


Создать город 
POST    /cities  
{
  "id": 0,
  "name": "string"
  "advices": [
    {
      "advice": "string"
    }
  ],
}

Получить город по ID
GET     /cities/id


Изменить город с ID
PUT     /cities/id
{
  "advices": [
    {
      "advice": "string"
    }
  ],
  "id": 0,
  "name": "string"
}

Удалить город с ID
DELETE  /cities/id 


Изменить название города с ID
PUT     /cities/id/edit/name
{
  "name": "string"
}

Создать совет для города с ID
POST    /cities/id/edit/advices
{
  "advice": "string"
}


Изменить совет с INDEX города с ID
PUT     /cities/id/edit/advices/index
{
  "advice": "string"
}

Удалить совет с INDEX  для города с ID
DELETE  /cities/id/edit/advices/index


BOT TOKKEN : 1001031741:AAFE_0no7KD8muGxAxdqh_MYySpW1OQf644

BOT NAME : Tourist Advice Bot @TouristAdviceBot
