# tourist-advice-service

REST API

�������� ��� ������ 
GET     /cities


������� ����� 
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

�������� ����� �� ID
GET     /cities/id


�������� ����� � ID
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

������� ����� � ID
DELETE  /cities/id 


�������� �������� ������ � ID
PUT     /cities/id/edit/name
{
  "name": "string"
}

������� ����� ��� ������ � ID
POST    /cities/id/edit/advices
{
  "advice": "string"
}


�������� ����� � INDEX ������ � ID
PUT     /cities/id/edit/advices/index
{
  "advice": "string"
}

������� ����� � INDEX  ��� ������ � ID
DELETE  /cities/id/edit/advices/index


BOT TOKKEN : 1001031741:AAFE_0no7KD8muGxAxdqh_MYySpW1OQf644

BOT NAME : Tourist Advice Bot @TouristAdviceBot
