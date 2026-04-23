> **Model czasu (domyślny):** Implementujemy własną klasę `FFDateTime` (pola `int`: `year`, `month`, `day`, `hour`, `minute`).
> **Opcjonalnie:** zamiast `FFDateTime` można użyć `java.time.LocalDateTime`. Wtedy proszę w krótkim komentarzu opisać różnice (walidacja, arytmetyka, czytelność).

---

## 1) Fabuła (kontekst)

Firma **ffWork** zarządza siecią coworkingów. Potrzebuje narzędzia do:
- rejestrowania **użytkowników** (osoby i firmy),
- dodawania **zasobów** (sale, biurka, urządzenia),
- tworzenia i obsługi **rezerwacji**, cen, płatności,
- wystawiania **faktur** (opcjonalnie: **raportów**).

Zadaniem jest zaprojektowanie obiektowego modelu oraz prostego CLI (REPL), które pozwoli testować logikę biznesową.

---

## 2) Kryteria OOP (co musi pojawić się w kodzie)

- **Klasy** — spójny model domeny (użytkownicy, zasoby, rezerwacje, płatności, faktury).
- **Dziedziczenie** — wspólne zachowania i specjalizacje (np. `Resource` + podklasy).
- **Dwie klasy abstrakcyjne** (min.):
    - `Resource`,
    - `Payment`.
- **Interfejsy** (min. 3), np.:
    - `PricingPolicy`,
    - `Billable`,
    - dowolny trzeci interfejs (np. `Repository<T>`, `Filter<T>`, `Discountable` jeśli robicie zniżki, itp.).
- **Overriding** (min. 5): np. różne `baseRatePerHour()` w zasobach, `describe()` w zasobach, implementacje `Payment#capture()`, itd.
- **Overloading** (min. 3): np. przeciążone konstruktory, metody `book(...)` w serwisie rezerwacji (start + end **lub** start + duration).

> Część bardziej zaawansowanych rzeczy (rozbudowany kalendarz, zniżki, portfel, raporty) jest oznaczona jako **opcjonalna** – nie jest wymagana do zaliczenia podstawowej wersji zadania.

---

## 3) Specyfikacja klas — **wymagania minimalne**

### 3.1. Czas — `FFDateTime` (domyślnie)

#### `FFDateTime` (final, immutable, implements Comparable)

- **Pola (private final):** `int year, month, day, hour, minute`.

- **Konstruktory / fabryki (overloading):**
    - `FFDateTime(int y, int m, int d, int h, int min)` — waliduje zakresy:
        - `1 <= month <= 12`,
        - `1 <= day <= 30` (można przyjąć uproszczony kalendarz: każdy miesiąc ma 30 dni),
        - `0 <= hour <= 23`,
        - `0 <= minute <= 59`.
    - `static FFDateTime of(int y, int m, int d, int h, int min)` — wywołuje konstruktor.
    - `static FFDateTime parse(String iso)` — akceptuje dokładnie format `YYYY-MM-DDTHH:MM` (np. `2025-09-15T10:00`), rzuca `IllegalArgumentException` przy błędnym formacie / zakresie.

- **Metody:**
    - `int toEpochMinutes()` — liczy minuty od stałej epoki (np. 2000-01-01T00:00), zakładając uproszczony kalendarz (30 dni w miesiącu).
    - `FFDateTime plusMinutes(int minutes)` — nowa instancja; korzysta z tej samej arytmetyki (minuty → godziny → dni → miesiące → lata przy założeniu 30 dni na miesiąc, 12 miesięcy na rok).
    - `int minutesUntil(FFDateTime other)` — różnica w minutach (`other - this`) na podstawie `toEpochMinutes()`.
    - `int compareTo(FFDateTime o)` — porównanie po `toEpochMinutes()`.
    - `String toString()` — `YYYY-MM-DDTHH:MM` (z zerami wiodącymi tam, gdzie trzeba).

- **Inwarianty:** obiekt po utworzeniu zawsze reprezentuje poprawną datę i godzinę w ramach przyjętego, uproszczonego kalendarza.

> **Opcja:** zamiast `FFDateTime` można użyć `LocalDateTime`. Wtedy:
> - pola `start`, `end` w innych klasach przyjmują `LocalDateTime`,
> - daty są parsowane przez `LocalDateTime.parse(...)`,
> - w komentarzu opisujecie różnice vs własna klasa (np. kto waliduje, kto liczy minuty).

---

### 3.2. Pieniądze

#### `Money` (value object)

- **Pola:**
    - `BigDecimal amount` — waluta stała: PLN.

- **Konstruktory / fabryki:**
    - `Money(BigDecimal amount)` — ustawia skalę `2`, `RoundingMode.HALF_UP`.
    - `static Money of(String)` — tworzy obiekt z napisu (np. `"123.45"`).
        - (Opcjonalnie można dodać `of(double)`, ale trzeba uważać na dokładność).

- **Metody (minimum):**
    - `Money add(Money other)`
    - `Money subtract(Money other)`
    - `Money multiply(BigDecimal m)`
    - `String toString()` → np. `123.45 PLN`

- **Opcjonalnie (ale mile widziane):**
    - `Money multiply(double m)`
    - `int compareTo(Money other)`
    - `equals()` / `hashCode()`

- **Inwariant:** docelowo kwota nie powinna być ujemna (chyba że świadomie to dopuszczacie i dokumentujecie).

---

### 3.3. Użytkownicy

#### `class User`

- **Pola:**
    - `String email` (unikalne),
    - `String displayName`.
- **Metody:** akcesory, `toString()`.

**Podklasy:**
- `class IndividualUser extends User`
    - Można dodać pole `studentId` (opcjonalne).
- `class CompanyUser extends User`
    - **Pola:** `String companyName`, `String taxId` (NIP).

---

### 3.4. Zasoby (dziedziczenie i overriding)

#### `abstract class Resource`

- **Pola:**
    - `String name` (unikalne),
    - `Money customHourlyRate` (opcjonalna niestandardowa stawka).

- **Metody abstrakcyjne:**
    - `protected abstract Money baseRatePerHour();`
    - `public abstract String describe();` — krótki opis do listowania.

- **Metody konkretne:**
    - `public Money hourlyRate()` — jeśli `customHourlyRate != null` → zwróć ją; inaczej `baseRatePerHour()`.

**Podklasy:**
- `class Room extends Resource`
    - **Pola:** `int seats`, `Set<String> equipment` (np. `"projector"`, `"whiteboard"`).
    - **Overriding:** `baseRatePerHour()`, `describe()`.

- `class Desk extends Resource`
    - **Pola:** `enum DeskType { HOT, FIXED } type`.
    - **Overriding:** jw.

- `class Device extends Resource`
    - **Pola:** `int quantity` (ile sztuk można równolegle zarezerwować).
    - **Overriding:** jw.

---

### 3.5. Rezerwacje

#### `enum BookingStatus { PENDING, CONFIRMED, CANCELLED, COMPLETED }`

#### `class Booking`

- **Pola:**
    - `String id` (np. `BK-<yyyyMMdd>-<counter>`),
    - `User user`,
    - `Resource resource`,
    - `FFDateTime start`, `FFDateTime end` (lub `LocalDateTime`, jeśli używacie opcji z biblioteką),
    - `BookingStatus status`,
    - `Money calculatedPrice` (cena wyliczona na podstawie polityki cen),
    - `Payment payment` (może być `null`).

- **Inwarianty:**
    - `end` > `start`,
    - `status` zmienia się tylko dozwolonymi przejściami:
        - `PENDING → CONFIRMED` lub `CANCELLED`,
        - `CONFIRMED → COMPLETED` lub `CANCELLED`.

- **Metody pomocnicze:**
    - `int durationMinutes()` — liczba minut pomiędzy `start` i `end` (np. na podstawie `toEpochMinutes()`).

---

### 3.6. Polityki cen (interfejsy + implementacje)

#### `interface PricingPolicy`

- `Money price(Booking booking)` — liczy cenę **bazową** za czas trwania na podstawie `booking.resource.hourlyRate()`.

  **Reguła minutowa (sugerowana):**
    - `pricePerMinute = hourlyRate / 60`,
    - `price = minutes × pricePerMinute`,
    - zaokrąglanie do 2 miejsc, `RoundingMode.HALF_UP`.

**Implementacje (wersja podstawowa):**
- `class StandardPricing implements PricingPolicy`
- `class HappyHoursPricing implements PricingPolicy`
    - **Założenia:** –30% w godzinach 14:00–16:00 (np. w każdy dzień).
    - Najprościej: jeśli **godzina startu rezerwacji** zawiera się w tym przedziale → rabat 30% na całą rezerwację.

> ⭐ **Opcjonalnie:** można rozbudować logikę Happy Hours (np. liczyć rabat tylko za część rezerwacji w HH).

> ⭐ **System zniżek (`Discountable`, `StudentDiscount`, itd.) jest przeniesiony do sekcji „Opcjonalne rozszerzenia”.**  
> W wersji podstawowej wystarczy `PricingPolicy`.

---

### 3.7. Płatności i faktury (wersja podstawowa)

#### `enum PaymentStatus { INITIATED, CAPTURED }`

#### `abstract class Payment`

- **Pola:**
    - `Money amount`,
    - `String paymentId`,
    - `PaymentStatus status`.

- **Metody abstrakcyjne:**
    - `void capture()` — realizuje płatność i ustawia status na `CAPTURED` (przy błędnym stanie może rzucać `IllegalStateException`).

#### `class CardPayment extends Payment`

- **Pola:** `String last4` (ostatnie 4 cyfry karty).
- **Metody:**
    - `capture()` — symuluje autoryzację płatności (zmiana statusu na `CAPTURED`).

> ⭐ **Opcjonalne (zaawansowane):** dodać `WalletPayment`, dodatkowy status `REFUNDED`, metodę `refund()` i logikę zwrotów — opisane w sekcji „Opcjonalne rozszerzenia”.

#### `interface Billable`

- `Invoice toInvoice(Booking booking)`.

#### `class Invoice`

- **Pola:**
    - `String invoiceNumber`,
    - `FFDateTime issueDate` (lub `LocalDateTime` w wersji z biblioteką),
    - `User buyer`,
    - `Money total`,
    - `String itemDescription` (np. `"Rezerwacja <resource> <start–end>"`).

---

### 3.8. Repozytoria (in-memory)

Prosty wzorzec repozytorium (w pamięci). Można użyć Tablic [], `List`, `Map`, itp.

- `interface UserRepository {
      void add(User u);
      Optional<User> findByEmail(String email);
      User[] findAll(); albo List<User> findAll();
  }`

- `interface ResourceRepository {
      void add(Resource r);
      Optional<Resource> findByName(String name);
      Resource[] findAll(); albo List<Resource> findAll();
      // ⭐ Opcjonalnie: List<Resource> findByType(Class<? extends Resource> t);
  }`

- `interface BookingRepository {
      void add(Booking b);
      Optional<Booking> findById(String id);
      Booking[] findAll(); albo List<Booking> findAll(); 
      // ⭐ Opcjonalnie: List<Booking> findByResource(Resource r);
      // ⭐ Opcjonalnie: List<Booking> findByUser(User u);
  }`

**Implementacje:**  
`InMemoryUserRepository`, `InMemoryResourceRepository`, `InMemoryBookingRepository` — trzymają dane w kolekcjach/tablicach Javy.

---

### 3.9. Serwisy (logika biznesowa)

#### `class BookingService`

- **Zależności:** repozytoria (`UserRepository`, `ResourceRepository`, `BookingRepository`) + aktualne `PricingPolicy` (np. `StandardPricing` albo `HappyHoursPricing`).

- **Metody (overloading):**
    - `Booking book(User u, Resource r, FFDateTime start, FFDateTime end)`
    - `Booking book(User u, Resource r, FFDateTime start, int durationMinutes)` — deleguje do wersji z `end` poprzez `start.plusMinutes(durationMinutes)`.

- **Algorytm `book` (wersja podstawowa):**
    1. Waliduj czasy (`end > start`).
    2. Sprawdź kolizje dla zasobu (na podstawie minut od epoki lub porównań):
        - dla `Room`/`Desk` — żadne `CONFIRMED`/`PENDING` rezerwacje nie mogą się **nakładać** (zakresy `[start, end)`):
            - nakładanie: `startA < endB && startB < endA`.
        - dla `Device` — dozwolonych jest `quantity` równoległych rezerwacji (zlicz nakładające się).
    3. Utwórz `Booking` ze statusem `PENDING` i policz cenę:
        - `base = pricingPolicy.price(booking)`.
    4. Zapisz w repo, nadaj `id` (format `BK-<yyyyMMdd>-<counter>` po **dacie startu**).

- **Inne metody (minimum):**
    - `confirm(String bookingId)` — zmiana statusu na `CONFIRMED`,
    - `cancel(String bookingId)` — zmiana statusu na `CANCELLED`, jeśli to dozwolone,
    - `complete(String bookingId)` — zmiana statusu na `COMPLETED`,
    - `list(...)` — zwraca listę rezerwacji.
        - **Filtry po użytkowniku/zasobie/statusie mogą być zrobione w prosty sposób (np. osobne metody lub parametry).**

#### `class PaymentService`

- `Payment pay(String bookingId, String cardLast4)` — tworzy `CardPayment`, woła `capture()`, przypina do `Booking` i zwraca płatność.

> ⭐ **Opcjonalne (zaawansowane):** obsługa portfela (`WalletPayment`), zwrotów (`refund`) i dodatkowych walidacji — w sekcji rozszerzeń.

#### `class BillingService implements Billable`

- `Invoice toInvoice(Booking booking)` — numer faktury `INV-<yyyyMMdd>-<counter>` po **dacie wystawienia**.

---

## 3.10. Raporty (⭐ opcjonalne)

> **Ta sekcja jest opcjonalna – dla chętnych / na dodatkowe 'punkty'.**

#### `class ReportingService`

- `Map<Resource, Double> utilization(FFDateTime from, FFDateTime to)` (lub jakas swoja klasa z polami lub talica zamiast Mapy) — obłożenie w %:
    - Licz minuty z rezerwacji o statusie **CONFIRMED/COMPLETED** w przedziale `[from, to)`,
    - Podziel przez łączną liczbę minut w tym przedziale (zasób dostępny 24/7),
    - Zaokrąglij do dwóch miejsc po przecinku.

- `Map<String, Money> revenueByResource(FFDateTime from, FFDateTime to)` (lub jakas swoja klasa z polami lub talica zamiast Mapy) oraz `Money totalRevenue(...)` — suma z **opłaconych** rezerwacji (po `capture`).

---

## 4) CLI (REPL) — komendy i format

### Użytkownicy

- `ADD_USER INDIVIDUAL <email> <fullName>`
- `ADD_USER COMPANY <email> <companyName> <nip>`
- `LIST_USERS`

### Zasoby

- `ADD_ROOM <name> <seats> <hourlyRate>`
- `ADD_DESK <name> <hot|fixed> <hourlyRate>`
- `ADD_DEVICE <name> <quantity> <hourlyRate>`
- `LIST_RESOURCES`
  > ⭐ **Opcjonalnie:** `LIST_RESOURCES [TYPE=<ROOM|DESK|DEVICE>]`

### Rezerwacje

- `BOOK <userEmail> <resourceName> <startIso> <endIso>`
- `BOOK <userEmail> <resourceName> <startIso> <durationMinutes>`
- `CONFIRM <bookingId>`
- `CANCEL <bookingId>`
- `LIST_BOOKINGS`
  > ⭐ **Opcjonalnie:** `LIST_BOOKINGS [USER=<email>] [RESOURCE=<name>] [STATUS=<PENDING|CONFIRMED|CANCELLED|COMPLETED>]`

### Polityki cen

- `SET_PRICING STANDARD|HAPPY_HOURS`

> ⭐ **Zniżki (SET_DISCOUNT ...) są w całości opcjonalne – patrz sekcja rozszerzeń.**

### Płatności / faktury

- `PAY <bookingId> CARD <last4>`
- `INVOICE <bookingId>`

> ⭐ **Opcjonalne:** `PAY <bookingId> WALLET` (jeśli implementujecie `WalletPayment`).

### Raporty (⭐ opcjonalne)

- `REPORT UTILIZATION <fromIso> <toIso>`
- `REPORT REVENUE <fromIso> <toIso>`

### Pomoc / wyjście

- `HELP` — drukuje krótką ściągę,
- `QUIT` — kończy program.

**Zachowanie I/O:**
- Sukces: `OK: <krótki opis>` + ewentualne dane (ID, kwoty).
- Błąd: `ERROR: <treść>` (np. walidacja dat, brak użytkownika/zasobu, kolizja).

---

## 5) Dokładne reguły i założenia (ważne przy ocenie)

1. **Format daty/godziny** w CLI: `YYYY-MM-DDTHH:MM`. Parsowane przez `FFDateTime.parse(...)` (lub `LocalDateTime.parse(...)` — jeśli wybierzecie alternatywę).
2. **Kolizje rezerwacji**: zakresy `[start, end)` nakładają się, jeśli `startA < endB && startB < endA` (porównania po minutach od epoki lub bezpośrednio).
3. **Cennik**: bazowo `pricePerMinute = hourlyRate / 60`. Całość `minutes × pricePerMinute` → skala 2, `RoundingMode.HALF_UP`.
4. **Happy Hours**: –30% (najprościej: cała rezerwacja ma rabat, gdy `start` ∈ HH).
5. **Płatności (wersja podstawowa)**: `capture()` zmienia `status` na `CAPTURED`. Zwroty są **opcjonalne**.
6. **Raporty (opcjonalne)**: upraszczamy dostępność zasobów do 24/7 (brak kalendarza świąt/godzin otwarcia).
7. **Identyfikatory**:
    - Rezerwacje: `BK-<yyyyMMdd>-<counter>` na podstawie `booking.start`,
    - Faktury: `INV-<yyyyMMdd>-<counter>` na podstawie `issueDate`.
8. **Pakiety (sugerowane):**
    - `time` (FFDateTime),
    - `money`,
    - `domain` (user/resource/booking),
    - `pricing`,
    - `payment`,
    - `billing`,
    - `repo`,
    - `service`,
    - `cli`,
    - ⭐ `report`, `discount` — jeśli implementujecie rozszerzenia.

---

## 6) Plan pracy (subtaski)

1. `time/` — `FFDateTime` (+ proste testy jednostkowe parsera i arytmetyki).
2. `money/` — `Money`.
3. `domain/` — `User` + podklasy; `Resource` + podklasy; `BookingStatus`, `Booking`.
4. `repo/` — repozytoria in-memory.
5. `pricing/` — `PricingPolicy`, `StandardPricing`, `HappyHoursPricing`.
6. `service/` — `BookingService`, `PaymentService`, `BillingService`.
7. `cli/` — REPL: parser komend, ładne komunikaty, formatowanie kwot.
8. ⭐ `discount/` — zniżki (`Discountable`, itd.).
9. ⭐ `report/` — `ReportingService`.
10. ⭐ Wersja z `LocalDateTime` zamiast `FFDateTime` (dla chętnych).

---

## 7) Testy ręczne

### Testy **podstawowe (must pass)**

**Test 0 — Dane startowe**
- `ADD_ROOM "Sala Alfa" 12 80`
- `ADD_DESK "Hot-1" hot 25`
- `ADD_DEVICE "Projektor-1" 2 40`
- `ADD_USER INDIVIDUAL anna@ex.com "Anna Nowak"`
- `ADD_USER COMPANY biuro@acme.pl "ACME Sp. z o.o." 5211234567`
- `SET_PRICING STANDARD`
- **Spodziewane:** `LIST_RESOURCES`, `LIST_USERS` zwracają powyższe pozycje.

**Test 1 — Rezerwacja i płatność (overloading)**
- `BOOK biuro@acme.pl "Sala Alfa" 2025-09-15T10:00 2025-09-15T12:00` → `PENDING`, cena `160.00 PLN`.
- `CONFIRM <id>` → `CONFIRMED`.
- `PAY <id> CARD 4242` → `Payment captured method=CARD last4=4242` (lub podobny komunikat).
- `INVOICE <id>` → `Invoice total=160.00 PLN buyer=ACME Sp. z o.o.`.
- `BOOK biuro@acme.pl "Sala Alfa" 2025-09-16T09:00 90` → `120.00 PLN`.

**Test 2 — Kolizje**
- Mając `CONFIRMED` `10:00–12:00`, próba `11:00–13:00` → `ERROR: resource not available`.
- Równoległa rezerwacja innego zasobu — przechodzi.

**Test 3 — Happy Hours**
- `SET_PRICING HAPPY_HOURS`
- `BOOK anna@ex.com "Hot-1" 2025-09-17T14:00 2025-09-17T16:00` → około `35.00 PLN` (–30% od `50.00`).

**Test 6 — Ilość urządzeń**
- Dwie rezerwacje `Projektor-1` w tym samym czasie przy `quantity=2` — przechodzą; trzecia → błąd.

### Testy ⭐ opcjonalne (dla chętnych)

**Test 4 — Zniżki** (wymaga systemu zniżek)
- `SET_DISCOUNT STUDENT`
- `BOOK anna@ex.com "Sala Alfa" 2025-09-18T09:00 2025-09-18T11:00` → np. `128.00 PLN` (–20%).
- `BOOK biuro@acme.pl "Sala Alfa" 2025-09-18T12:00 2025-09-18T14:00` → `160.00 PLN` (bez zniżki).

**Test 5 — Anulowanie i refund** (wymaga `WalletPayment` + `refund()`)
- Po `PAY ... WALLET`, `CANCEL <id>` → `CANCELLED` + `refund processed method=WALLET`.

**Test 7 — Raporty** (wymaga `ReportingService`)
- `REPORT UTILIZATION 2025-09-15T00:00 2025-09-20T00:00` → procent obłożenia per zasób.
- `REPORT REVENUE 2025-09-01T00:00 2025-09-30T23:59` → suma i podział przychodów.

---

## 8) Opcjonalne rozszerzenia (dla chętnych)

W tej sekcji są elementy, które **podnoszą poziom trudności**, ale nie są wymagane do podstawowego zaliczenia:

1. **Rozbudowany `FFDateTime`** — np. prawdziwe długości miesięcy, lata przestępne, bardziej szczegółowa walidacja.
2. **System zniżek** (`Discountable` + `NoDiscount`, `StudentDiscount`, `CompanyTierDiscount`) + komenda `SET_DISCOUNT` + Test 4.
3. **Portfel i refundy** (`WalletPayment`, `refund()`, status `REFUNDED`) + Test 5.
4. **Raporty** (`ReportingService`, komendy `REPORT ...`) + Test 7.
5. Dodatkowe filtry w CLI (`LIST_BOOKINGS` z parametrami, `LIST_RESOURCES TYPE=...`).
6. Wersja modelu oparta bezpośrednio na `LocalDateTime` zamiast `FFDateTime` + opis różnic.

---

## 9) Definicja ukończenia (DoD)

- Komendy podstawowe działają zgodnie ze specyfikacją, komunikaty są czytelne.
- Spełnione minimalne wymagania OOP (abstrakcje, interfejsy, overriding, overloading).
- `FFDateTime` poprawnie parsuje i porównuje czasy (w ramach przyjętego uproszczonego kalendarza).
- Cennik, rezerwacje, płatności kartą i faktury funkcjonują zgodnie z opisem.
- Kod ma rozsądny podział na pakiety, jest czytelny i skomentowany tam, gdzie to potrzebne.
- ⭐ Dodatkowe punkty / plusy za zaimplementowane rozszerzenia z sekcji 8.

Powodzenia! 💪
