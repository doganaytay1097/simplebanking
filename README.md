# SimpleBanking

Basit bir bankacılık servisi — hesap oluşturma, para yatırma (`credit`) ve çekme (`debit`) işlemlerini REST API üzerinden yönetir.

## 🚀 Teknolojiler
- Java 11 
- Spring Boot 2.5.6 (Web, Data JPA, Validation)
- H2 Database (in-memory)
- Swagger UI (API dokümantasyonu)
- Gradle

---

## 📦 Projeyi Çalıştırma

### 1️⃣ Lokal Çalıştırma (Gradle ile)
```bash
# Testleri çalıştır
./gradlew clean test

# JAR oluştur (testleri atlayarak)
./gradlew clean bootJar -x test

# Uygulamayı başlat
java -jar build/libs/*.jar
```

- Uygulama `http://localhost:8080` adresinde çalışır.
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### 2️⃣ Docker ile Çalıştırma
Önce JAR oluştur:
```bash
./gradlew clean bootJar -x test
```

Docker image oluştur ve çalıştır:
```bash
docker build -t simplebanking:latest .
docker run -p 8080:8080 simplebanking:latest
```

---

## 🧪 Testler
Tüm testleri çalıştırmak için:
```bash
./gradlew clean test
```
Testler **MockMvc entegrasyon testleri** içerir ve API uç noktalarının doğru çalıştığını doğrular.

---

## 📚 Örnek API Kullanımı

**Para Yatırma (Credit)**
```bash
curl -X POST "http://localhost:8080/account/v1/credit/12345"   -H "Content-Type: application/json"   -d '{"amount": 100}'
```

**Para Çekme (Debit)**
```bash
curl -X POST "http://localhost:8080/account/v1/debit/12345"   -H "Content-Type: application/json"   -d '{"amount": 50}'
```

---

## 📌 Notlar
- H2 veritabanı in-memory çalışır, uygulama kapanınca veri sıfırlanır.
- Varsayılan H2 konsolu: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:bank`
