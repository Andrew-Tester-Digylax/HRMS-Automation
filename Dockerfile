FROM mcr.microsoft.com/playwright/java:v1.45.0-jammy

WORKDIR /app

# Copy project
COPY . .

# Install dependencies & build
RUN mvn clean install -DskipTests

# Run tests
CMD ["mvn", "test"]