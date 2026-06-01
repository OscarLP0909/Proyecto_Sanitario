# 🏥 Proyecto Sanitario — Sistema de Gestión de Citas Médicas

> **Backend** de un sistema de gestión de citas médicas desarrollado con **Spring Boot**, diseñado para ser utilizado por clínicas y centros de salud.

---

## 🇪🇸 Español

### Descripción

Aplicación REST API que permite gestionar citas médicas entre pacientes y médicos, con control de disponibilidad horaria, notificaciones por email y autenticación segura mediante JWT.

### ✨ Funcionalidades principales

- **Autenticación JWT** — Login seguro con tokens, roles diferenciados (Admin, Médico, Paciente)
- **Gestión de médicos** — Alta con generación automática de contraseña temporal enviada por email
- **Gestión de pacientes** — Registro con credenciales auto-generadas
- **Disponibilidad horaria** — Configuración de franjas horarias por médico y día de la semana
- **Generación automática de slots** — Al configurar la disponibilidad, el sistema genera los huecos de cita automáticamente
- **Reserva de citas** — Los pacientes pueden reservar slots disponibles
- **Gestión de especialidades** — Asignación de múltiples especialidades por médico
- **Notificaciones por email** — Envío de credenciales de acceso al crear usuarios
- **Paginación** — Todos los listados incluyen paginación y ordenación

### 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 + Spring Boot 4 | Framework principal |
| Spring Security + JWT | Autenticación y autorización |
| Spring Data JPA + Hibernate | Persistencia de datos |
| PostgreSQL | Base de datos |
| RabbitMQ | Mensajería asíncrona |
| JavaMailSender | Envío de emails |
| Flyway | Migraciones de base de datos |
| Docker Compose | Orquestación de servicios |
| Lombok | Reducción de boilerplate |

### 🏗️ Arquitectura

El proyecto sigue una **arquitectura por módulos**, organizando el código por funcionalidad en lugar de por capas técnicas:

```
src/main/java/org/gestion/proyecto_sanitario/
├── auth/               # Autenticación y JWT
├── medico/             # Gestión de médicos y slots
├── paciente/           # Gestión de pacientes
├── cita/               # Gestión de citas
├── especialidad/       # Gestión de especialidades
├── disponibilidad/     # Configuración de horarios
└── shared/             # Configuración global, excepciones
```

Cada módulo contiene sus propias capas: `controller`, `service`, `repository`, `dto`, `mapper`, `model`.

### 🔐 Seguridad

- Autenticación stateless con **JWT**
- Control de acceso por rol con `@PreAuthorize`
- Contraseñas encriptadas con **BCrypt**
- Endpoints públicos: `/api/auth/**`
- Endpoints protegidos por rol: Admin, Médico, Paciente

### 🚀 Cómo ejecutar el proyecto

#### Requisitos previos
- Java 21+
- Docker y Docker Compose
- Maven

#### Pasos

1. Clona el repositorio:
```bash
git clone https://github.com/OscarLP0909/Proyecto_Sanitario.git
cd Proyecto_Sanitario
```

2. Copia el archivo de configuración y rellena los valores:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Arranca la aplicación (Docker levanta PostgreSQL y RabbitMQ automáticamente):
```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`

### 📋 Variables de entorno necesarias

```properties
spring.mail.username=tu_email@gmail.com
spring.mail.password=tu_app_password
jwt.secret=tu_clave_secreta_larga
jwt.expiration=86400000
```

---

## 🇬🇧 English

### Description

REST API backend for a medical appointment management system, designed for clinics and healthcare centers. Built with Spring Boot, it handles doctor scheduling, patient management, and secure JWT-based authentication.

### ✨ Main Features

- **JWT Authentication** — Secure login with tokens, role-based access control (Admin, Doctor, Patient)
- **Doctor Management** — Registration with auto-generated temporary password sent via email
- **Patient Management** — Registration with auto-generated credentials
- **Schedule Availability** — Configure time slots per doctor and day of the week
- **Automatic Slot Generation** — When availability is set, the system automatically generates appointment slots
- **Appointment Booking** — Patients can book available slots
- **Specialty Management** — Multiple specialties assignable per doctor
- **Email Notifications** — Access credentials sent by email when users are created
- **Pagination** — All listings include pagination and sorting

### 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 + Spring Boot 4 | Main framework |
| Spring Security + JWT | Authentication & authorization |
| Spring Data JPA + Hibernate | Data persistence |
| PostgreSQL | Database |
| RabbitMQ | Async messaging |
| JavaMailSender | Email sending |
| Flyway | Database migrations |
| Docker Compose | Service orchestration |
| Lombok | Boilerplate reduction |

### 🏗️ Architecture

The project follows a **modular architecture**, organizing code by business domain rather than technical layers:

```
src/main/java/org/gestion/proyecto_sanitario/
├── auth/               # Authentication & JWT
├── medico/             # Doctor & slot management
├── paciente/           # Patient management
├── cita/               # Appointment management
├── especialidad/       # Specialty management
├── disponibilidad/     # Schedule configuration
└── shared/             # Global config, exceptions
```

Each module contains its own layers: `controller`, `service`, `repository`, `dto`, `mapper`, `model`.

### 🔐 Security

- Stateless authentication with **JWT**
- Role-based access control with `@PreAuthorize`
- Passwords encrypted with **BCrypt**
- Public endpoints: `/api/auth/**`
- Protected endpoints by role: Admin, Doctor, Patient

### 🚀 How to run

#### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven

#### Steps

1. Clone the repository:
```bash
git clone https://github.com/OscarLP0909/Proyecto_Sanitario.git
cd Proyecto_Sanitario
```

2. Copy the config file and fill in the values:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Run the application (Docker automatically starts PostgreSQL and RabbitMQ):
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### 📋 Required environment variables

```properties
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
jwt.secret=your_long_secret_key
jwt.expiration=86400000
```

---

## 👨‍💻 Autor / Author

**Oscar** — [GitHub](https://github.com/OscarLP0909)
