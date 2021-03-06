# 8.hét

### 2021.10.31.

A héten az észlelés során érkező értesítéssel foglalkoztam. Első terv az volt, hogy a drive changes/watch API-jával feliratkozok a változásokra a mappában. Ez sajnos nem arra való, ő egy domainre tud visszahívni, amivel tud értesíteni. Persze ez a domain lehetett volna egy AWS Lambda, és úgy küldeni értesítést de:
mint kiderült a changes/list API használatából, hogy ez egyáltalán nem listázza az új fájlokat. Az új fájlokat sikerült listázni egy egyszerű files/list és createdTime DESC rendezéssel. Felmerült ekkor a kérdés, hogy milyen Android API-val lenne érdemes pollozni (JobScheduler, AlarmManager, WorkManager), de egyiket sem találtam megfelelőnek, ugyanis pontatlanul futnak, és nem is árulják el, mikor futottak legutoljára. Továbbá a dokumentációban kiemelve volt egy felhívás, hogy ha szerverről adatokat akarunk pollozni, akkor inkább ne tegyük, hanem használjunk Firebase Cloud Messaging-et. Az FCM-nek utánajárva viszonylag könnyedén tudtam már értesítéseket küldeni a Firebase webes felületéről, viszont másik telefonról egy fokkal nehezebb volt: REST apin keresztül kell, viszont nem biztos, hogy jó ötlet egy Android alkalmazásba a Credentialjeim belecsomagolni. Így csináltam egy AWS Lambdát, amit meg tud hívni az App, és a Lambad tovább tudja hívni az FCM Rest Apiját. Ez persze csak egy odébb helyezése a problémának, ugyanis még mindig szabadon elérhető egy endpoint, ami értesítéseket küld. Azonban egy köztes réteggel már könnyedén megvalósítható lenne egy ellenőrzés, hogy a felhasználó, aki meghívta, be van e lépve a Google fiókjába, és csak ekkor engedni. Az egyetlen hátramaradt probléma: nincs két telefonom, amin van Google szolgáltatás.

# 7.hét

### 2021.10.22

Megcsináltam azt, hogy a drive egy BirdPhotography nevű mappába pakolja a képeket, és a logikát hozzá, hogy ha létezik a mappa, akkor használja, ha nem létezik, akkor pedig hozza létre

### 2021.10.24.

A héten szerettem volna megcsinálni az értesítéseket, viszont a Drive API-ban és az új Android verziókban nem találtam meg a megfelelő lehetőséget erre. Ezért inkább a beállításokkal foglalkoztam, melyre ezt a libraryt tudtam felhasználni: https://github.com/alorma/Compose-Settings
Sajnos még nincs benne jó lehetőség számok bevitelére, de a következő verzióban úgy látom, hogy már lesz.
Mindenesetre könnyű vele kezelni a SharedPreferencest, és jól működik.

# 6. hét

### 2021.10.18

A mai napon a csoportosítással foglalkoztam. A LazyColumn nagyon jó API-t provide-ol sticky headerek létrehozására, viszont a LazyVerticalGrid nem, így attól megszabadultam, és inkább kiírtam a sorokba a madarak nevét. Így 1 kép/sor látható, de névvel együtt, és időpontra csoportosítva, jelenleg 5 percenként-re

Megcsináltam a képek teljes méretben megnyitása funkcionalitást is, itt csak a navigáció közötti paraméter átadás volt problémás, de azon túllendülve könnyedén ment.

### 2021.10.17

A héten a fájlok feltöltésével foglalkoztam a Google Drive-ba. Ennek úgy indultam neki, hogy a Drive kommunikáció lesz a legnehezebb, azonban arra a részére körülbelül 1 óra alatt researchöléssel és kódolással együtt volt egy működő Repository függvényem. A nehézségek ott kezdődtek, amikor az ImageCapture által mentett fájlt File osztályba csomagolva szerettem volna visszakapni. Ehhez az összes Stackoverflow postot elolvasva sem volt túl sok ötletem. Az onSuccess listener output-jába csomagolt uri egyszerűen konvertálható a toUri() függvényével Uri-vá, viszont ez nem működött, ugyanis érvénytelennek találta a visszakapott Urit. A getRealPathFromURI függvény a contentResolveren keresztül visszaolvassa a kiírt fájlt, így már világossá válik az igazi Uri, és az alapján már könnyedén létre lehet hozni a File-t.

Ezek után a csoportosítással kezdtem foglalkozni, sajnos megállapítottam, hogy a Title és Description kiírások nem kódolódnak bele a file-ba, a MediaStore valamiért nem csinálja meg, csak figyelmeztet, hogy Android 11 alatt már ignorálja. Így visszaolvasáskor a Title maga a file neve lett, a Description pedig null. Ezért a madár típusát a fájlnév elejébe, \_ -al elválasztva kódolom bele, ami már korábban is terv volt, hogy a fájlnév is tükrözze a fajtát. Így a fájlok közötti eligazodás is könnyebb, illetve ez alapján csoportosíthatóak az észlelések

# 5. hét

### 2021.10.10

A héten erős időhiány és betegség hátráltatott, így amit sikerült megcsinálni, az a képek automatikus elkészítése. Jelenleg a képek az alkalmazás belső könyvtárába kerülnek mentésre, ezt majd később szeretném áthelyezni a Pictures/BirdPhotography könyvtárba, viszont ehhez jogosultságokat kell kérni a felhasználótól. Amennyiben nem adja meg, használható lehet továbbra is a belső könyvtár, vagy akár csak a Drive feltöltés.

#### UPDATE 10.11:

Android 9 óta az alap könyvtárak írásához, és a saját fájlok olvasásához nem kell jogosultságot kérni, így ez viszonylag könnyedén megoldható. Android Q-s feature, hogy RELATIVE_PATH-t is meg lehet adni, egy almappát a Pictures mappában.

- Kérdés, hogy van-e értelme a korábbi verziókat kezelni?
- Mi lenne az alkalmazás céleszköze az Android Things megszűnik? Hogyan lehet Raspberryn futtatni?

Ezek mellett sikerült a LazyVerticalGrid segítségével megfelelő galéria elhelyezést kapnom, ami jól működik, viszont experimental api.

Jelenleg így néz ki a Main Activitym onCreate függvénye:

```
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
```

# 4. hét

### 2021.10.03.

Ezt a hetet első körben azzal kezdtem, hogy a már meglévő részeit az alkalmazásnak refaktoráljam úgy, hogy az MVVM architektúrát kövesse. Ehhez létrehoztam a Login screen-hez egy ViewModelt, illetve egyet a Home Screen-nek. A ViewModeleket Hilt segítségével, függőséginjektálással kapják meg a különböző Composable Screenek.

A hét további részében a madarak felismerését megvalósító programrésszel foglalkoztam. Ez viszonylag egyszerű volt, a TF Hub-on elérhető modell, ami képes madarakat fajta szerint is felismerni. A BirdRecognizerImageAnalyzer osztály így, ezt az alkalmazásba csomagolt modellt egyszerűen fel tudja használni, a madarak felismerésére

# 3. hét

### 2021.09.26.

Ezen a héten kísérleteztem az accompanist jogosultság kérésével, és azzal, hogy hogyan tud működni az egész a Jetpack Compose-al.

Ezek után a hét további részében a JustInMind alapján építettem fel a szükséges képernyőket, illetve a navigációt valósítottam meg.

A Drive hozzáféréshez szükséges lesz a Google bejelentkezés, így az alkalmazás kezdőképernyőjének a Login screent állítottam be, és megvalósítottam a bejelentkezést.
Itt viszonylag sok idő volt rájönni, hogy a Google Cloud OAuth client ID-nál Web Application típusú alkalmazás Client ID-ja fogadható el csak, az Android típusú nem

# 2. hét

### 2021.09.19.

A mai napon létrehoztam a GitHub repositoryt, ahova a kód is folyamatosan feltöltésre fog kerülni.

A projektet Jetpack Compose Activityvel hoztam létre, és első körben internetes példakódok segítségével Composable functionökkel készítettem egy MainActivityt, amelyben a telefon hátsó kamera képe látható.

Az engedélykéréssel még gondok vannak, ugyanis a Composable function nem renderelődik újra az engedély meglétének függvényében, így ez további utánajárást igényel

A hét további részében az alkalmazás specifikációján dolgoztam, wireframet készítettem Justinmind-al illetve egy ütemtervet készítettem a félévre.
