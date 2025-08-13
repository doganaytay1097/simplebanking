# Java 17 Alpine imajını kullanıyoruz (hafif ve hızlı)
FROM eclipse-temurin:17-jdk-alpine

# Çalışma dizini
WORKDIR /app

# Gradle ile build ettiğimiz jar dosyasını container içine kopyala
COPY build/libs/*.jar app.jar

# Uygulama 8080 portunda çalışacak
EXPOSE 8080

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]
