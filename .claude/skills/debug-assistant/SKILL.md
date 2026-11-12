---
name: debug-assistant
description: Analizira greške iz Android Studija, Gradle build-a i Logcat-a (stack trace, crash, ANR, "Build failed", lint) i objašnjava uzrok na srpskom, bez mijenjanja koda. Koristi kad korisnik zalijepi log ili stack trace, ili kaže da aplikacija pada, ne radi ili se ne build-uje.
---

# Debug assistant

Ovo je skill samo za čitanje. **Ne mijenjaj fajlove**, ne instaliraj aplikaciju i ne briši podatke na uređaju bez pitanja.

## Koraci
1. Razvrstaj grešku: compile error / Gradle (dependency, SSL, JDK) / runtime crash (exception) / ANR /
   pogrešno ponašanje bez greške / lint upozorenje.
2. Pročitaj log:
   - tip izuzetka i poruka (npr. `NullPointerException`, `BadTokenException`)
   - **najdonji `Caused by:`** je obično pravi uzrok
   - prvi red `at com.example.tictactoe_xo...` pokazuje mjesto u našem kodu
3. Otvori taj fajl na toj liniji i pročitaj okolni kod.
4. Ako log nije dat, a uređaj je povezan, smiješ samo čitati:
   - `adb devices`
   - `adb logcat -d -t 300 *:E` (posljednje greške)
   - `adb shell pidof com.example.tictactoe_xo`, pa `adb logcat -d --pid=<pid>`
5. Objasni:
   - šta greška znači uopšteno
   - zašto se desila **ovdje**, sa konkretnim scenarijem (koji klik, rotacija, tajming)
   - kako je pouzdano reprodukovati
6. Predloži rješenje i alternative. Kod samo u odgovoru, ne u fajlu; čekaj odobrenje.
7. Na kraju kratko nauči korisnika kako da sam pročita ovakav log sljedeći put.

## Poznate zamke na ovom računaru
- `PKIX path building failed` / `SSL certificate problem`: HTTPS presreće antivirus/mreža.
  Git koristi `http.sslBackend schannel`; Gradle iz terminala pokretati sa `-Djavax.net.ssl.trustStoreType=Windows-ROOT`.
- `JAVA_HOME is set to an invalid directory`: `JAVA_HOME` ne smije završavati sa `\bin`.

## Pravila
- Srpski jezik, latinica; termini na engleskom.
- Ako uzrok nije siguran, navedi hipoteze po vjerovatnoći i kako svaku provjeriti.
