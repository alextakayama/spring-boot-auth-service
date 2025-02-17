# 🔐 Spring Boot Auth Service

**A minimalist, open-source REST API authentication service designed as an alternative to Auth0, Cognito and other similar authentication solutions.**

***Warning**: This project is a **minimal sample implementation** of an authentication REST API and **is not a fully secure production-ready service**. It is intended for educational purposes and as a foundation for further development. Use with caution and implement necessary security measures before deploying in a real environment.* 

## ✨ Features
- ✅ Email / Password Authentication
- 🔄 Refresh Tokens & Token Revocation
- ⚙️ Admin Endpoints

---

## 🚀 Tech Stack
- **Programming Language**: Java 17
- **Build Tool**: Gradle 8.11.1
- **Framework**: Spring Boot 3.4.2
- **Persistence**: Redis
- **Tokens**: JWT (Java Web Tokens)
- **Validation**: Bean Validation
- **Testing**: JUnit + Mockito
- **Containerization**: Docker + Docker Compose

---

## 🏗️ Running the Application with Docker Compose

To quickly set up and run the application using Docker Compose, follow these steps:

1. **Ensure Docker and Docker Compose are installed**
   - [Install Docker](https://docs.docker.com/get-docker/)
   - [Install Docker Compose](https://docs.docker.com/compose/install/)


2. **Clone the repository**
```sh
git clone https://github.com/alextakayama/spring-boot-auth-service.git
cd spring-boot-auth-service
```

3. **Start the application**
```sh
docker-compose up -d
```

This will start the Spring Boot application along with Redis in detached mode (`-d`).

**To stop the application:**
```sh
docker-compose stop
```

**And to delete the containers:**
```sh
docker-compose down
```

---

## 🧪 API Testing with Postman

A Postman collection is available under the `docs` folder, with all the endpoints required to test the API. You can import it into Postman and easily interact with the authentication service.

---

## 🚀 Why I Built This
Not long ago, in the early days of the internet, building your own Identity and Access Management (IAM) system was standard practice for developers. Today, with convenient services like Auth0 and Cognito, this fundamental knowledge isn't as widespread among developers who have grown accustomed to these abstractions. I built this project to explore and share these critical security and architectural concepts that shaped the web.

---

## 🛠️ Future Improvements & TODOs
- Email Verification
- Password Reset via Email
- Logging
- Rate Limiting
- Multi-Factor Authentication (MFA)
- Improve Test Coverage
- Social Login (OAuth2)
- Passwordless login

---

## 👋 About Me

<img alt="Foto de Alex Takayama" src="https://alextakayama.com/images/alex_takayama.jpg" style="border-radius: 50%; height: 100px; width: 100px">

Hi, I'm **Alex Takayama**, a builder at heart, passionate about technology and problem-solving. I have experience crafting scalable applications and driving innovation for enterprises and startups. Always eager to collaborate—let’s build something great together!

### 💻 Skills
- **Languages**: C++, C#, Java, JavaScript, Lua, Node.JS, Perl, PHP, Python, Typescript
- **Backend**: ASP.NET, Express, FastAPI, Flask, Laravel, Nest.JS, Phalcon, Play Framework, Slim, Spring Boot, Symfony
- **Frontend**: Angular, Next.js, React, Vue.js
- **Mobile**: Flutter, React Native
- **Databases**: DynamoDB, MariaDB, MongoDB, MySQL, SQL Server, PostgreSQL, SQLite
- **DevOps**: Apache, AWS, CI/CD, GCP, Docker, Memcached, NginX, RabbitMQ, Redis, Terraform, Varnish

### 🌐 Connect

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/alextakayama) [![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&labelColor=181717)](https://github.com/alextakayama)

### 📫 Contact

You can also reach me by email: [alex.takayama@gmail.com](mailto:alex.takayama@gmail.com).

---

## 📄 License

Distributed under the [MIT License](https://opensource.org/license/MIT).
