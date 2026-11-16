# PROJECT_PROGRESS.md

Praćenje napretka na projektu TicTacToe NEON (repozitorijum `TicTacToe_XO`): šta je završeno, na čemu se radi i šta slijedi.
Ažurira se nakon svake završene logičke cjeline (fix, nova funkcionalnost, review).

## Trenutno stanje

- Aplikacija radi: meni, SOLO (protiv AI) i DUO mod, izbor težine, podešavanja, statistika.
- Prvi krug ispravki grešaka je završen (vidi tabelu ispod).
- Statistika je podijeljena na SOLO (čovjek/AI) i DUO (X/O) – pitanje #11 riješeno.
- Nova neon launcher ikonica (varijanta A "Dvoboj"), provjerena na tabletu.
- Unit testovi za čistu Java logiku: `SoloStatsTest` (8), `BoardTest` (15), `TicTacToeAITest` (15).
  Pokretanje: `gradlew :app:testDebugUnitTest` (svi prolaze).
- Aplikacija se zove **TicTacToe NEON** (`app_name`).
- Dokumentacija: `README.md` sa screenshot-ovima iz `docs/screenshots/`.
- Sve izmjene su commitovane i poslate na GitHub (`main`).

## Završeno

| Datum      | Commit    | Opis |
|------------|-----------|------|
| 2026-11-12 | `535c1cd` | Početni commit projekta |
| 2026-11-12 | `4d3517d` | Izabrani simbol igrača (X/O) se prenosi u `GameActivity` |
| 2026-11-12 | `8947031` | Undo više ne zamrzava SOLO igru kad AI igra prvi |
| 2026-11-12 | `85bdff4` | Odloženi AI potez se otkazuje kad počne nova igra |
| 2026-11-12 | `8b651e7` | Dugme NOVA IGRA povezano sa `GameViewModel.restartMatch()` |
| 2026-11-12 | `dba906e` | Uklonjen `MainActivity` iz šablona (dupla ikonica u launcheru) |
| 2026-11-12 | `d5ebf41` | `InsetsHelper`: sadržaj ne ide ispod sistemskih traka (Android 15+) |
| 2026-11-12 | `b312b7a` | Dodati `CLAUDE.md` i projektni skillovi |
| 2026-11-13 | —         | Dodat `PROJECT_PROGRESS.md` |
| 2026-11-16 | `d330179` | Nova klasa `SoloStats` (čovjek/AI) + prvi unit testovi (`SoloStatsTest`) |
| 2026-11-16 | `abe5d83` | SOLO rezultati se snimaju kao čovjek/AI; DUO zadržava stare X/O ključeve |
| 2026-11-16 | `87a0f3f` | Ekran statistike: odvojene sekcije "Protiv AI" i "Dva igrača" |
| 2026-11-16 | `7cbfcb3` | Adaptivna launcher ikonica (vektori): tamna pozadina + mreža, neon X/O, monochrome sloj |
| 2026-11-16 | `fbc83f4` | Legacy ikonice (API 24–25) kao PNG sa pravim glow-om; pregled `docs/icon/ic_launcher_preview_1024.png` |
| 2026-11-16 | `6e8ecfd` | Unit testovi za `Board` (polja, sve 8 linija, neriješeno, pobjeda u zadnjem potezu, kopija) |
| 2026-11-16 | `b4f6a07` | `findWinLine` vraća kopiju kombinacije (`clone()`) + test koji bez nje pada |
| 2026-11-16 | `c06094b` | Unit testovi za `TicTacToeAI`, uključujući iscrpnu provjeru da HARD nikad ne gubi |
| 2026-11-16 | `1aa26ee` | `TicTacToeAI(Random random)` – ubrizgavanje zavisnosti; EASY/MEDIUM testirani preko `FixedRandom` stub-a |
| 2026-11-16 | `7dd4701` | UI ispravke ("neriješeno", labela "Simbol igrača", margine i boje tabele rezultata), ime "TicTacToe NEON", screenshot-ovi u `docs/screenshots/` |
| 2026-11-16 | `4aefdd8` | `README.md`: funkcionalnosti, screenshot-ovi, tehnologije, arhitektura, AI, pokretanje, testovi |

## Trenutni zadatak

- Nema aktivnog zadatka. Sve stavke iz code review-a (#1–#10) i pitanje #11 su završeni.

## Sljedeći koraci (code review, 2026-11-13)

Redosljed rada: #1 → #3 → #2 → sitne stavke. Svaka ispravka = poseban commit.

### Kritično
- [x] **#1** Dijalog rezultata se otvara 480 ms nakon kraja partije bez provjere: pad
  (`BadTokenException`) ako se Activity zatvori/rotira u međuvremenu, ili stari dijalog
  preko nove partije nakon NOVA IGRA (`GameActivity.showResultDialog`).

### Važno
- [x] **#2** Sticky LiveData: rotacija ponavlja zvuk/vibraciju/animaciju posljednjeg poteza
  i zvuk pobjede (`lastMove`, `gameStatus`). Ideja: `Event<T>` wrapper.
- [x] **#3** Process death: novi ViewModel ostaje u DUO modu jer se `configure()` poziva samo
  kad je `savedInstanceState == null`. Ideja: `viewModel.isConfigured()`.

### Sitno
- [x] **#4** Suvišni `redrawEntireBoard()` nakon undo i "Igraj ponovo" (to radi `boardReset` observer).
- [x] **#5** Neiskorišćeni Android importi u `model/Board.java` i `ai/TicTacToeAI.java`.
- [x] **#6** `resolveHumanPlayer()` duplira parsiranje; `GameMode.valueOf` bez `try/catch`.
- [x] **#7** Alokacije (`BlurMaskFilter`, `RectF`) u `SymbolView.onDraw()`.
- [x] **#8** Hardkodovan `"-"` u `StatsActivity`.
- [x] **#9** `SettingsViewModel` vraća `MutableLiveData` umjesto `LiveData`.
- [x] **#10** Mrtav kod, neiskorišćeni importi, prazni `catch`, greške u imenima (`oposite`, `findWinnLine`...).

## Otvorena pitanja / ideje za kasnije

- [x] **#11** Statistika broji pobjede X/O, a ne čovjek/AI – u SOLO modu "Win rate X" nema jasno značenje.
  Riješeno (opcija A): `SoloStats` za SOLO, `GameStats` ostaje za DUO i skor meča.
  Napomena: stari X/O podaci (prije 2026-11-16) sadrže i SOLO partije – zadržani su kao DUO statistika.
- [x] **#12** Launcher ikonica iz šablona (zeleni robot) zamijenjena neon ikonicom.
  Napomena: glow u vektoru je imitiran poluprozirnim linijama (VectorDrawable nema blur).
  Geometrija je ista u vektorima i PNG-ovima – kod izmjene dizajna mijenjati oba.
- [ ] Ideja: legacy PNG ikonice konvertovati u WebP (Android Studio → Convert to WebP), oko 120 KB → manje.
- [x] Unit testovi za `Board` i `TicTacToeAI`.
  Usput nađeno: `findWinLine` je dijelio statički niz `WIN_COMBINATIONS` sa `WinLine` – ispravljeno (`b4f6a07`).
- [x] Konstruktor `TicTacToeAI(Random random)` (ubrizgavanje zavisnosti), da se EASY/MEDIUM mogu
  pouzdano testirati. Izabran stub (`FixedRandom`) umjesto fiksnog seed-a, jer test ne zavisi od
  internog redoslijeda poziva `Random`-a. `GameViewModel` i dalje koristi `new TicTacToeAI()`.
- [x] Napravljen `README.md` (`4aefdd8`). Kod promjene ekrana ili funkcionalnosti ažurirati i
  screenshot-ove u `docs/screenshots/` (snimani na telefonu/emulatoru 1440×2880).
- [ ] Sljedeće: u `app/build.gradle.kts` su `appcompat`, `material` i `constraintlayout` navedeni dva puta
  (iz `libs.versions.toml` i ručno sa starijom verzijom). Ostaviti samo verzije iz kataloga.
