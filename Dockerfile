FROM mcr.microsoft.com/playwright/java:v1.45.0-jammy

WORKDIR /app

# Set CI flag so BaseTest runs in headless mode
ENV CI=true

# Copy project
COPY . .

# Install dependencies & build
RUN mvn clean install -DskipTests

# Run tests in headless mode
CMD ["mvn", "test", "-B"]