---
name: test-assistant
description: Pomaže pri planiranju, pisanju, pokretanju i objašnjavanju testova (JUnit 4 unit testovi i Espresso instrumented testovi) za Android projekat, sa objašnjenjima na srpskom. Koristi kad korisnik želi da napiše test, razumije postojeći test ili pokrene testove.
---

# Test assistant

Fajlove testova piši **tek nakon odobrenja** korisnika; svaki novi test ili grupa testova ide u poseban commit.
Produkcioni kod ne mijenjaj radi testova bez posebnog objašnjenja i odobrenja.

## Kontekst projekta
- `app/src/test/`: **unit testovi** na JVM-u (JUnit 4), brzi, bez uređaja.
- `app/src/androidTest/`: **instrumented testovi** na uređaju/emulatoru (AndroidJUnit4, Espresso).
- Trenutno postoje samo testovi iz šablona.
- `model` paket i `ai/TicTacToeAI` su čista Java, idealni za unit testove.
- `GameViewModel` koristi `Handler(Looper.getMainLooper())` i `LiveData`: za unit test treba
  `InstantTaskExecutorRule` (`androidx.arch.core:core-testing`) i rješenje za Looper (Robolectric ili
  ubacivanje Handler-a/Executor-a izvana). Objasni opcije prije nego što se doda zavisnost.

## Koraci
1. Utvrdi šta se testira i pročitaj tu klasu cijelu.
2. Objasni koji tip testa odgovara (unit ili instrumented) i zašto.
3. Predloži listu test slučajeva: normalan tok, granični slučajevi, greške.
   Za svaki slučaj jedna rečenica šta provjerava.
4. Pokaži kod testa **u odgovoru**, po šablonu Arrange-Act-Assert, sa imenima tipa
   `evaluateStatus_fullBoardWithoutWinner_returnsDraw`. Objasni ga liniju po liniju
   (`@Test`, `assertEquals`, `@Before`...).
5. Nakon odobrenja upiši fajl i pokreni:
   - svi unit testovi: `gradlew :app:testDebugUnitTest`
   - jedna klasa: `gradlew :app:testDebugUnitTest --tests "com.example.tictactoe_xo.model.BoardTest"`
   - instrumented: `gradlew :app:connectedDebugAndroidTest` (instalira aplikaciju na uređaj, pa prvo pitaj)
6. Objasni rezultat. Ako test padne, razjasni da li je greška u testu ili u kodu, i ne "popravljaj" test
   samo da bi prošao.

## Pravila
- Srpski jezik, latinica; termini i komentari u kodu na engleskom.
- Kada dodaješ zavisnost u `build.gradle.kts` / `libs.versions.toml`, prvo objasni zašto i čekaj odobrenje.
