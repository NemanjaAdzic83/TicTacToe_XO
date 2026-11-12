---
name: architecture-review
description: Provjerava MVVM arhitekturu Android projekta, odnosno da li su slojevi (model, repository, viewmodel, ui, util) pravilno razdvojeni i povezani, i objašnjava nalaze na srpskom bez mijenjanja koda. Koristi kad korisnik pita za arhitekturu, MVVM, slojeve, zavisnosti ili "gdje bi ovo trebalo da stoji".
---

# Architecture review

Ovo je skill samo za čitanje. **Ne mijenjaj fajlove.**

## Koraci
1. Napravi mapu: za svaki paket (`model`, `repository`, `viewmodel`, `ui`, `util`, `ai`) navedi klase i njihovu ulogu.
2. Nacrtaj dijagram zavisnosti (mermaid ili tekst): ko koga importuje/poziva.
3. Provjeri pravila:
   - Smjer zavisnosti: `ui → viewmodel → repository/model`; nikad obrnuto.
   - `model` nema `android.*` importe, pa se može testirati na JVM-u.
   - Activity ne pristupa Repository-ju direktno i ne sadrži pravila igre.
   - ViewModel ne drži referencu na View/Activity (`Application` u `AndroidViewModel` je u redu).
   - ViewModel izlaže `LiveData`, ne `MutableLiveData`.
   - **State vs event:** jednokratni događaji (zvuk, dijalog, animacija) nisu modelovani kao sticky LiveData.
   - Single source of truth: ista provjera/podatak nije dupliran u Activity i ViewModel-u.
   - Preživljavanje stanja: rotacija (ViewModel) i process death (`SavedStateHandle`).
4. Za svaki prekršaj: gdje je (`fajl:linija`), zašto je bitno, kako ispraviti i koji su tradeoff-ovi.
   Navedi alternative (npr. Event wrapper / SingleLiveEvent u Javi; StateFlow/SharedFlow ako se pređe na Kotlin;
   Hilt za dependency injection).
5. Završi tabelom: prekršaj | ozbiljnost | trud za ispravku, i preporučenim redoslijedom.

## Pravila
- Srpski jezik, latinica; termini na engleskom.
- Objasni *zašto* pravilo postoji (testabilnost, lifecycle, čitljivost), ne samo da je prekršeno.
