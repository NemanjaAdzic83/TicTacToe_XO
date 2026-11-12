# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Projekat
- Android aplikacija TicTacToe XO: **Java 11**, XML Views + ViewBinding (bez Compose-a), jedan modul `:app`.
- U projektu nema Kotlina; ne uvoditi Kotlin ni nove biblioteke bez dogovora.
- UI tekstovi su na srpskom u `res/values/strings.xml`; ne hardkodovati tekst u layout/Java.

## Arhitektura (MVVM)
- `model` je čista Java (bez `android.*`) – tu je logika table i pravila igre.
- Logika igre živi u ViewModel-u; Activity samo javlja namjeru (`viewModel.xxx()`) i posmatra LiveData.
  Tablu ne crtati ručno nakon akcije – to radi observer `boardReset`.
- Svaka nova Activity mora pozvati `InsetsHelper.applySystemBarPadding(binding.getRoot())` odmah nakon
  `setContentView()` (targetSdk 36 → obavezan edge-to-edge na Android 15+; testni tablet ima 3-button navigaciju).

## Način rada
- Korisnik uči Java/Kotlin/Android: ponašaj se kao mentor. Prije svake izmjene objasni problem, rješenje
  i alternative, pa **sačekaj odobrenje**. Odobrenje važi samo za tu jednu izmjenu.
- Objašnjenja na srpskom (latinica, ijekavica); programerski termini i komentari u kodu na engleskom.
  Postojeće srpske komentare ne mijenjati osim ako opisuju ponašanje koje više ne važi.
- Svaka ispravka = poseban commit sa jasnim opisom, pa `git push`.
- Prije commita provjeri `git diff --cached --stat`: Android Studio sam stage-uje `.idea` fajlove.
  `.idea/deploymentTargetSelector.xml` se ne commituje.
- Prije commita provjeri build: `gradlew :app:assembleDebug` (ili `:app:compileDebugJavaWithJavac` za samo Java izmjene).
