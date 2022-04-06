# FoodDelivery
Sistem de gestionare pentru o aplicatie de livrat mancare 

### Obiecte
- Afacere
- - Restaurant (au in plus taguri ex: sushi, coffee, fastfood, etc)
- - Magzin alimentar
- Useri (Adresa)
- Soferi
- Comenzi
Afacerile, persoanele sunt derivate din AccountBalanceHolder (au un cont cu o suma de bani si pot transfera sume intre ei)
Soferii si userii sunt derivate din Person (nume, email, numar de telefon)
Soferii si Afacerile au un rating

### Ciclul vietii unei comenzi
Comenzile au un OrderStatus care se modifica de la etapa la etapa. Etapele trebuie realizate in aceasta ordine:
- Comenzile sunt create OrderFactory, adaugand produse
- Odata finalizata, comanda poate fi plasata (placeOrder)
- Comanda trebuie preparata de restaurantul care a primit-o si este gata pentru a fi livrata.
- Un sofer liber (care nu are o alta comanda curenta) poate accepta sa livreze comanda.
- Odata livrata, comanda este gata pentru a fi ridicata de user.
- Userul ridica comanda si poate oferi un tip pentru sofer.
- Comanda este finalizata.

### Actiuni
- Adauga user
- Adauga afacere
- Adauga sofer
- Afiseaza suma din contul unui user
- Adauga bani in contul unui user
- Adauga un nou produs pentru o afacere
- Plaseaza o comanda
- Prepara o comanda
- Gaseste un sofer liber care sa livreze o comanda
- Livreaza o comanda
- Ridica comanda si ofera un tip pentru sofer

