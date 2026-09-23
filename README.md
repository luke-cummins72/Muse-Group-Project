# Muse

Muse is a portfolio web app built as a 3rd year group project. It lets students upload and showcase their work, and gives potential employers a place to browse student portfolios. The app is split into three roles — **Student**, **Admin**, and **Employer** — each with its own set of permissions and views.

> This was a group project. My role on the team was building out the **Student** side of the app.

## Roles

- **Student** — upload projects (with images and documents), build a portfolio, and showcase their work
- **Employer** — browse student portfolios and view showcased work
- **Admin** — manage users and content across the platform

## Features

- Upload projects with a short and long description, a project image, and supporting documents
- Edit and update existing projects (including replacing images/documents)
- Delete projects
- View portfolios by student
- Role based views for students, employers, and admins

## Built With

- **Backend:** Java, Spring Boot, Spring Data JPA
- **Frontend:** Thymeleaf (server-rendered HTML)
- **Database:** Relational database via JPA/Hibernate

## Getting Started

### Prerequisites
- Java (JDK) installed
- Maven or Gradle (whichever this project uses)
- A configured database (see `application.properties`/`application.yml`)

### Setup

1. Clone the repo:
   ```bash
   git clone <repo-url>
   cd <repo-folder>
   ```

2. Configure your database connection in `src/main/resources/application.properties`.

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   (or `./gradlew bootRun`, depending on your build tool)

4. Open your browser to `http://localhost:8080` (or whichever port is configured).

## Project Status

Complete — built as a 3rd year group project.

## Contributors

**Luke Cummins** — Student role
GitHub: [@luke-cummins72](https://github.com/luke-cummins72)

*Plus teammates who worked on the Admin and Employer roles.*
