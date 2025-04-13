# WebAppsJava

## DB structure

### Clients
- client_id - string uuid - pk
- first_name - string 
- last-name - string
- email - string 
- phonenumber
- address

### cars
- car-plate - string - pk
- client_id - string - fk
- make - string
- model - string
- year - int
- vin - string

### Insurer
- insurer_id - string uuid - pk
- name - string
- email - string
- phone_number - string
- address

### insurances
- insurance_id - string uuid - pk
- client_id - string - fk
- car-plate - string - fk
- insurer_id - string uuid - fk
- policy-number - long
- start-date - date
- end-date - date
- amount - int
- details - string

### payments
- payment_id - string uuid - pk
- insurance-id - string - fk
- payment_date - date
- amount - int
- payment-method - enum { credit, debit, transfer }


## Table Relationships
-	Client → Cars (One-to-Many)
-	Client → Insurances (One-to-Many)
-	Cars → Insurances (One-to-Many)
-	Insurer → Insurances (One-to-Many)
-	Insurances → Payments (One-to-Many)



 


