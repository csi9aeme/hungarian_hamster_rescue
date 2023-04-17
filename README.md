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
