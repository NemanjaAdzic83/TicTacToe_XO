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

- Code review cijelog projekta (`android-code-review`) radi pravljenja novog spiska problema.

## Sljedeći koraci

- Na osnovu review-a napraviti spisak problema po prioritetu i ispravljati ih jedan po jedan
  (svaka ispravka = poseban commit).

## Otvorena pitanja / ideje za kasnije

- (prazno)
