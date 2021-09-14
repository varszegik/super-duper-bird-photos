### 2021.09.14.

A mai napon létrehoztam a GitHub repositoryt, ahova a kód is folyamatosan feltöltésre fog kerülni.

A projektet Jetpack Compose Activityvel hoztam létre, és első körben internetes példakódok segítségével Composable functionökkel készítettem egy MainActivityt, amelyben a telefon hátsó kamera képe látható.

Az engedélykéréssel még gondok vannak, ugyanis a Composable function nem renderelődik újra az engedély meglétének függvényében, így ez további utánajárást igényel
