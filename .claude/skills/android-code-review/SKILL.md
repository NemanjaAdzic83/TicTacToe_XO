---
name: android-code-review
description: Analizira kvalitet Java/Kotlin Android koda (fajl, paket, necommitovane izmjene ili posljednji commit) i daje nalaze sa objašnjenjem na srpskom, bez mijenjanja fajlova. Koristi kad korisnik traži review, provjeru kvaliteta ili "pogledaj da li je ovo dobro".
---

# Code review

Ovo je skill samo za čitanje. **Ne mijenjaj fajlove.** Ispravke idu tek nakon odobrenja, svaka u poseban commit.

## Koraci
1. Odredi obim: navedeni fajl/paket, necommitovane izmjene (`git diff`, `git diff --cached`)
   ili posljednji commit (`git show HEAD`). Ako nije rečeno, pitaj.
2. Pročitaj cijele fajlove, ne samo diff, da vidiš kontekst.
3. Provjeri redom:
   - **Ispravnost:** logičke greške, granični slučajevi, null vrijednosti, pogrešni ključevi u Intent extras.
   - **Android lifecycle:** rotacija, process death, `binding` nakon `onDestroy()`, odloženi `post`/`postDelayed`
     na uništenoj Activity, curenje Context-a, sticky LiveData koji ponavlja jednokratne događaje.
   - **Threading:** dugi poslovi na main thread-u, zastarjeli (stale) callback-ovi.
   - **Resursi:** hardkodovan tekst/boje, `px` umjesto `dp`, edge-to-edge insets.
   - **Performanse:** alokacije u `onDraw()`, nepotrebno ponovno iscrtavanje.
   - **Čitljivost:** imena, greške u kucanju, mrtav kod, dupliranje, neiskorišćeni importi.
4. Za svaki nalaz napiši:
   - ozbiljnost: **Kritično / Važno / Sitno**
   - `fajl:linija`
   - problem i **konkretan scenario** kada se dešava
   - zašto je bitno
   - prijedlog rješenja sa isječkom koda **u odgovoru** i bar jednu alternativu
5. Poređaj nalaze po ozbiljnosti. Navedi i šta je dobro urađeno.
6. Završi pitanjem koju ispravku korisnik želi prvu.

## Pravila
- Srpski jezik, latinica; termini na engleskom.
- Ne izmišljaj nalaze: ako nisi siguran, reci da je pretpostavka i kako se može provjeriti.
