---
name: explain-code
description: Detaljno objašnjava Java ili Kotlin kod (fajl, klasu, metodu ili zalijepljeni isječak) na srpskom, liniju po liniju. Koristi kad korisnik pita "šta radi ovaj kod", "objasni mi", "kako ovo funkcioniše" ili navede fajl/metodu koju želi da razumije.
---

# Objašnjavanje koda

Ovo je skill samo za čitanje. **Ne mijenjaj, ne pravi i ne briši fajlove.** Ako uočiš grešku, samo je pomeni.

## Koraci
1. Odredi šta se objašnjava (fajl, klasa, metoda, isječak). Ako nije jasno, pitaj.
2. Pročitaj cijeli fajl, ne samo traženi dio. Po potrebi pročitaj i klase koje kod koristi
   (npr. ViewModel → Repository, model klase).
3. **Pregled prvo:** u 2-4 rečenice reci čemu kod služi, u kom je sloju (model / repository / viewmodel / ui / util)
   i ko ga poziva i kada (npr. `onCreate()` pri otvaranju ekrana).
4. **Liniju po liniju ili blok po blok:** citiraj kod sa `fajl:linija`, pa objasni.
   Trivijalne linije (importi, getteri) grupiši u jednu rečenicu.
5. Svaki Java/Android koncept objasni kad se prvi put pojavi (npr. `LiveData`, `Handler`, lambda, `enum`,
   `@Override`, lifecycle): šta je, zašto se koristi ovdje i koja je alternativa.
6. Ako postoji tok izvršavanja (klik → ViewModel → LiveData → observer), prikaži ga kao numerisan niz koraka.
7. Uočene probleme ili sumnjiva mjesta navedi u posebnom odjeljku "Primijećeno" (bez ispravljanja).
8. Završi sa 2-3 kratka pitanja za provjeru razumijevanja ili prijedlogom šta dalje proučiti.

## Pravila
- Srpski jezik, latinica; programerski termini na engleskom (ViewModel, State, callback...).
- Prilagodi dubinu početniku: radije jedan primjer više nego jedan manje.
