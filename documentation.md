# Vizsgaremek

## Leírás

Néhány éve önkéntesként részt veszek az Együtt A Kisállatokért Alapítvány, azon belül is a 
Hörcsögmentés tevékenységében. Intézzük az alapítványhoz kerülő hörcsögök adatlapjainak elkészítését, 
ideiglenes befogadóhoz adását, majd az örökbe adást. A projektemben az alapítvány működésének egy szeletét szeretném szimulálni.

---

## Felépítés

### Hörcsög entitás (Hamster)

A `Hamster` entitás a következő attribútumokkal rendelkezik:

* `id` - a hörcsög egyedi azonosítója, a program osztja ki.
* `name` - tetszőlegesen választott String típusú név, nem muszáj egyedinek lennie,de nem lehet üres.
* `hamsterSpecies` - enum a háziállatként tartható fajokkal.
* `gender` - a hörcsög neme.
* `dateOfBirth` - születési dátum.
* `hamsterStatus` - enum az örökbeadhatóság jelzésére.
* `host` - az ideiglenes befogadóhoz tartozó entitás, nem hozható létre új hörcsög nélküle. 
* `startOfFostering` - hozzánk kerülés dátuma.
* `adopter` - az örökbefogadó entitása, később kerül megadásra.
* `dateOfAdoption` - az örökbefogadás dátuma. 


Végpontok:

| HTTP metódus | Végpont                             | Leírás                                                               |
|--------------|-------------------------------------|----------------------------------------------------------------------|
| GET          | `"/api/hamsters"`                   | lekérdezi az összes Hamster entitást                                 |
| GET          | `"/api/hamsters/fostering"`         | lekérdezi az örökbefogadható Hamster entitásokat                     |
| GET          | `"/api/hamsters/{id}"`              | lekérdez egy entitást `id` alapján                                   |
| POST         | `"/api/hamsters"`                   | létrehoz új Hamster-t és hozzárendeli meglévő ideiglenes befogadóhoz |
| PUT          | `"/api/hamsters/{id}"`              | frissíti a Hamster entitás tetszőleges adatait                       |
| PUT          | `"/api/hamsters/{id}/adopted"`      | átállítja a Hamster entitást örökbeadott státuszra                   |

---

### Ideiglenes befogadó entitás (Host)

A `Host` entitás a következő attribútumokkal rendelkezik:

* `id` - az ideiglenes befogadó egyedi azonosítója, a program osztja ki. 
* `name` - az ideiglenes befogadó neve. Nem lehet üres. 
* `address` - az ideiglenes befogadó lakcíme. 
* `capacity` - helyek száma, ahol hörcsögöt tud befogadni. A `hamsters` lista nem lehet nagyobb, mint az itt megadott szám.
* `hamsters` - lista az aktuálisan gondozott hörcsögökről.
* `hostStatus` - az örökbefogadó tud-e hörcsögöt fogadni
* 
A `Hamster` és a `Host` entitások között kétirányú, 1-n kapcsolat van.

Végpontok:

| HTTP metódus | Végpont                      | Leírás                                                                       |
|--------------|------------------------------|------------------------------------------------------------------------------|
| GET          | `"/api/hosts"`               | lekérdezi az összes ideiglenes befogadót, vagy név szerint szűrhető          |
| GET          | `"/api/hosts/{id}"`          | lekérdez egy ideiglenes befogadót `id` alapján                               |
| GET          | `"/api/hosts/{id}/bycity"`   | szűri az ideiglenes befogadókat megadott település alapján                   |
| GET          | `"/api/hosts/{id}/hamsters"` | lekérdezi egy ideiglenes befogadó aktuális hörcsögeit                        |
| POST         | `"/api/hosts"`               | létrehoz egy új ideiglenes befogadót                                         |
| PUT          | `"/api/hosts/{id}"`          | frissíti az adott ideiglenes befogadó entitás tetszőleges adatait            |
| PUT          | `"/api/hosts/{id}/inactive"` | átállítja az adott `id`-jú ideiglenes befogadó állapotát. Törölni nem lehet. |



---

### Örökbefogadó entitás (Adoptive) 

Az `Adoptive` entitás a következő attribútumokkal rendelkezik:

* `id` - az örökbefogadó egyedi azonosítója, a program osztja ki.
* `name` - az örökbefogadó neve. Nem lehet üres.
* `address` - az örökbefogadó lakcíme.

A `Hamster` és az `Adoptive` entitások között kétirányú, 1-n kapcsolat van.

Végpontok:

| HTTP metódus | Végpont                            | Leírás                                                         |
|--------------|------------------------------------|----------------------------------------------------------------|
| GET          | `"/api/adopters"`                 | lekérdezi az összes örökbebefogadót, vagy név szerint szűrhető |
| GET          | `"/api/adopters/adoptivesbycity"` | lekérdezi örökbebefogadókat város alapján                      |
| GET          | `"/api/adopters/{id}"`            | lekérdez egy örökbebefogadót `id` alapján                      |
| GET          | `"/api/adopters/{id}/hamsters"`   | lekérdezi egy örökbebefogadó hörcsögeit `id` alapján           |
| POST         | `"/api/adopters"`                 | létrehoz egy új örökbebefogadót                                |
| PUT          | `"/api/adopters/{id}"`            | frissíti az adott entitás tetszőleges adatait                  |
| DELETE       | `"/api/adopters/{id}"`            | törli az örökbebefogadót a rendszerből                         |

A törlést csak akkor engedélyezi a rendszer, ha a jelentkező végül mégsem fogadott örökbe, így nincs hörcsög a listájában.

---

## Technológiai részletek

Egy háromrétegű alkalmazást készítettem, ami Docker-ből indítható MariaDB adatbázist használ az adatok tárolására. A teszt
osztályok külön adatbázist használnak. 
Minden entitásnak (Hamster, Host, Adoptive) külön Controller, Service, Repository rétege van a könnyebb elkülöníthetőség
érdekében. Az adatbázis-séma létrehozására Liquibase-t használja, minden entitás külön sql migrációs fájlt kapott. 
A bemenő adatok ellenőrzését a beépített validációk végzik el, a keletkezett kivételeket pedig saját kivételek 
létrehozásával oldottam meg. Ezek hibaüzenet kiírása után nem engedélyezik a műveletek elvégzését a hibák javításáig.
Az alkalmazás rendelkezik kitöltött SwaggerUI felülettel, ahol az alapvető funkciók kipróbálhatók. 

Készítettem néhány HTTP request fájlt a különböző controllerek különböző metódusaihoz, illetve az éles adatbázis 
feltölthető előre megadott adatokkal.

---