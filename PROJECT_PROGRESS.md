# PROJECT_PROGRESS.md

Praćenje napretka na projektu TicTacToe XO: šta je završeno, na čemu se radi i šta slijedi.
Ažurira se nakon svake završene logičke cjeline (fix, nova funkcionalnost, review).

## Trenutno stanje

- Aplikacija radi: meni, SOLO (protiv AI) i DUO mod, izbor težine, podešavanja, statistika.
- Prvi krug ispravki grešaka je završen (vidi tabelu ispod).
- Statistika je podijeljena na SOLO (čovjek/AI) i DUO (X/O) – pitanje #11 riješeno.
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
