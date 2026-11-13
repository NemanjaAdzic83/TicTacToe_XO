# PROJECT_PROGRESS.md

Praćenje napretka na projektu TicTacToe XO: šta je završeno, na čemu se radi i šta slijedi.
Ažurira se nakon svake završene logičke cjeline (fix, nova funkcionalnost, review).

## Trenutno stanje

- Aplikacija radi: meni, SOLO (protiv AI) i DUO mod, izbor težine, podešavanja, statistika.
- Prvi krug ispravki grešaka je završen (vidi tabelu ispod).
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

## Trenutni zadatak

- #3 iz spiska ispod.

## Sljedeći koraci (code review, 2026-11-13)

Redosljed rada: #1 → #3 → #2 → sitne stavke. Svaka ispravka = poseban commit.

### Kritično
- [x] **#1** Dijalog rezultata se otvara 480 ms nakon kraja partije bez provjere: pad
  (`BadTokenException`) ako se Activity zatvori/rotira u međuvremenu, ili stari dijalog
  preko nove partije nakon NOVA IGRA (`GameActivity.showResultDialog`).

### Važno
- [ ] **#2** Sticky LiveData: rotacija ponavlja zvuk/vibraciju/animaciju posljednjeg poteza
  i zvuk pobjede (`lastMove`, `gameStatus`). Ideja: `Event<T>` wrapper.
- [ ] **#3** Process death: novi ViewModel ostaje u DUO modu jer se `configure()` poziva samo
  kad je `savedInstanceState == null`. Ideja: `viewModel.isConfigured()`.

### Sitno
- [ ] **#4** Suvišni `redrawEntireBoard()` nakon undo i "Igraj ponovo" (to radi `boardReset` observer).
- [ ] **#5** Neiskorišćeni Android importi u `model/Board.java` i `ai/TicTacToeAI.java`.
- [ ] **#6** `resolveHumanPlayer()` duplira parsiranje; `GameMode.valueOf` bez `try/catch`.
- [ ] **#7** Alokacije (`BlurMaskFilter`, `RectF`) u `SymbolView.onDraw()`.
- [ ] **#8** Hardkodovan `"-"` u `StatsActivity`.
- [ ] **#9** `SettingsViewModel` vraća `MutableLiveData` umjesto `LiveData`.
- [ ] **#10** Mrtav kod, neiskorišćeni importi, prazni `catch`, greške u imenima (`oposite`, `findWinnLine`...).

## Otvorena pitanja / ideje za kasnije

- **#11** Statistika broji pobjede X/O, a ne čovjek/AI – u SOLO modu "Win rate X" nema jasno značenje.
