### Hungarian Hamster Resque

A projekt az Együtt A Kisállatokért Alapítvány részeként Hörcsögmentés működését szimulálja. A fő entitás a `Hamster` 
osztály, ez van a legtöbb adattal feltöltve. Létrehozása függ a `Host` (ideiglenes befogadó) osztálytól, nélküle nem
adható az adatbázishoz új hörcsög. Jelenleg ID alapján adható hozzá létrehozott hörcsög az adott befogadóhoz. 
A `Hamster` entitás minden adata szabadon változtatható a `PUT` metóduson keresztül. A faj, a nem és az örökbefogadhatóság
megadására enumot használ, mivel ezek csak néhány előre meghatározott adatot tartalmazhatnak. Egy másik `PUT` metódussal
adható örökbe a hörcsög, ilyenkor az örökbefogadó ID-ját, az örökbefogadás dátumát kell megadni, a hörcsög státusza
pedig automatikusan `örökbefogadható`-ról (`ADOPTABLE`) `örökbeadott`-ra (`ADOPTED`) változik. 
Az adatbázishoz adott hörcsög nem törölhető, hibás felvitel esetén az adatok változtathatóak. 

A `Host` entitás alap adatokkal rendelkezik: név, lakcím, hörcsöghelyek száma, ez jelzi, hogy hány állatot tud gondozni.
Ezek megadása kötelező, nélkülük nem hozható létre új ideiglenes befogadó. Az entitás rendelkezik egy kezdetben üres 
listával, ami az aktuálisan és a korábban gondozott hörcsögöket is tárolja. Az `örökbefogadható` és 
`kezelés alatt áll, de örökbefogadható` státuszú hörcsögök száma nem haladhatja meg a szabad hörcsöghelyek számát. Egy 
repositoryban megírt sql lekérdezés kilistázza `Host` id alapján az örökbeadható hörcsögöket, és új hörcsög létrehozásakor
a service réteg leellenőrzi, hogy a Host capacity kisebb-e, mint a már gondozott hörcsögök száma. 
Ideiglenes befogadó nem törölhető az adatbázisból, az állapota változtatható meg `ACTIVE`-ról `INACTIVE`-ra. Ebben az 
esetben a gondozásában lévő hörcsöghz tartozó gondozó id null-ra állítódik és újat kell neki megadni. 

Az `Adoptive` entitás is alap adatokkal rendelkezik: név, lakcím, örökbefogadott hörcsögök listája. Az adatbázisból csak 
akkor törölhető, ha nem még hörcsöge, különben kivétel keletkezik és nem törlődik. 

### Hungarian Hamster Rescue

This project simulates the operation of the Hamster Rescue as part of the Together for Animals Foundation. The main entity 
is the `Hamster` class, which contains the most data. Its creation depends on the `Host` (temporary caregiver) class; without 
it, a new hamster cannot be added to the database. Currently, a hamster can be associated with a specific caregiver based 
on their ID. All data related to the Hamster entity can be freely modified using the `PUT` method. For specifying the species, 
gender, and adoptability, an enumeration is used since these can only contain a few predefined values. Another `PUT` method 
allows a hamster to be adopted, requiring the adopter's ID and adoption date to be provided, and the hamster's status 
automatically changes from adoptable (`ADOPTABLE`) to adopted (`ADOPTED`). Once added to the database, a hamster cannot be 
deleted, but incorrect entries can be modified.

The `Host` entity has basic information: name, address, contacts and the number of hamster spots, indicating how many animals they 
can care for. Providing these details is mandatory, and a new temporary caregiver cannot be created without them. 
The entity also has an initially empty list that stores both currently and previously cared for hamsters. The number of 
adoptable and in-care hamsters cannot exceed the number of available hamster spots. A SQL query written in a repository 
lists adoptable hamsters based on the Host ID, and when creating a new hamster, the service layer checks whether the 
Host's capacity is less than the number of hamsters already cared for. Temporary caregivers cannot be deleted from the 
database, but their status can be changed from `ACTIVE` to `INACTIVE`. In this case, the caregiver's ID associated with the 
hamster in their care is set to null, and a new one must be provided.

The `Adopter` entity also has basic information: name, address, contacts and a list of adopted hamsters. It can only be deleted 
from the database if it no longer has any hamsters. Otherwise, an exception is raised, and it cannot be deleted.
