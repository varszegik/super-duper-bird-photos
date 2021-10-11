### 2021.10.10

A héten erős időhiány és betegség hátráltatott, így amit sikerült megcsinálni, az a képek automatikus elkészítése. Jelenleg a képek az alkalmazás belső könyvtárába kerülnek mentésre, ezt majd később szeretném áthelyezni a Pictures/BirdPhotography könyvtárba, viszont ehhez jogosultságokat kell kérni a felhasználótól. Amennyiben nem adja meg, használható lehet továbbra is a belső könyvtár, vagy akár csak a Drive feltöltés.

### 2021.10.03.

Ezt a hetet első körben azzal kezdtem, hogy a már meglévő részeit az alkalmazásnak refaktoráljam úgy, hogy az MVVM architektúrát kövesse. Ehhez létrehoztam a Login screen-hez egy ViewModelt, illetve egyet a Home Screen-nek. A ViewModeleket Hilt segítségével, függőséginjektálással kapják meg a különböző Composable Screenek.

A hét további részében a madarak felismerését megvalósító programrésszel foglalkoztam. Ez viszonylag egyszerű volt, a TF Hub-on elérhető modell, ami képes madarakat fajta szerint is felismerni. A BirdRecognizerImageAnalyzer osztály így, ezt az alkalmazásba csomagolt modellt egyszerűen fel tudja használni, a madarak felismerésére

### 2021.09.26.

Ezen a héten kísérleteztem az accompanist jogosultság kérésével, és azzal, hogy hogyan tud működni az egész a Jetpack Compose-al.

Ezek után a hét további részében a JustInMind alapján építettem fel a szükséges képernyőket, illetve a navigációt valósítottam meg.

A Drive hozzáféréshez szükséges lesz a Google bejelentkezés, így az alkalmazás kezdőképernyőjének a Login screent állítottam be, és megvalósítottam a bejelentkezést.
Itt viszonylag sok idő volt rájönni, hogy a Google Cloud OAuth client ID-nál Web Application típusú alkalmazás Client ID-ja fogadható el csak, az Android típusú nem

### 2021.09.19.

A mai napon létrehoztam a GitHub repositoryt, ahova a kód is folyamatosan feltöltésre fog kerülni.

A projektet Jetpack Compose Activityvel hoztam létre, és első körben internetes példakódok segítségével Composable functionökkel készítettem egy MainActivityt, amelyben a telefon hátsó kamera képe látható.

Az engedélykéréssel még gondok vannak, ugyanis a Composable function nem renderelődik újra az engedély meglétének függvényében, így ez további utánajárást igényel

A hét további részében az alkalmazás specifikációján dolgoztam, wireframet készítettem Justinmind-al illetve egy ütemtervet készítettem a félévre.
