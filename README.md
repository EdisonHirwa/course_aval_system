# Course Evaluation Survey System

A web-based Course Evaluation Survey System built with **Spring MVC**, **JSP/JSTL**, and **MySQL**.

---

## Tech Stack

| Layer      | Technology                         |
|------------|------------------------------------|
| Framework  | Spring MVC 5.3                     |
| View       | JSP + JSTL                         |
| Database   | MySQL 8.x                          |
| Build      | Maven                              |
| Security   | BCrypt password hashing, Servlet Filter |
| Email      | Spring JavaMailSender (SMTP)       |
| Server     | Apache Tomcat 9+                   |

---

## Project Structure

```
course-eval-system/
├── pom.xml
└── src/main/
    ├── java/com/courseeval/
    │   ├── controller/
    │   │   ├── AuthController.java       # Login, logout, teacher registration
    │   │   ├── AdminController.java      # Admin operations
    │   │   ├── InitiatorController.java  # Survey management
    │   │   ├── TeacherController.java    # Teacher dashboard & results
    │   │   └── SurveyController.java     # Public survey responses
    │   ├── dao/
    │   │   ├── UserDAO.java
    │   │   ├── CourseDAO.java
    │   │   └── SurveyDAO.java
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Course.java
    │   │   ├── Survey.java
    │   │   ├── SurveyQuestion.java
    │   │   ├── SurveyOption.java
    │   │   ├── Respondent.java
    │   │   └── SurveyResponse.java
    │   ├── service/
    │   │   └── EmailService.java
    │   └── util/
    │       └── AuthFilter.java
    ├── resources/
    │   ├── db.properties               # DB & mail config
    │   └── schema.sql                  # Full DB schema + seed data
    └── webapp/
        ├── WEB-INF/
        │   ├── web.xml
        │   ├── spring-mvc.xml
        │   └── views/
        │       ├── common/             # header, footer, login, register
        │       ├── admin/              # Admin views
        │       ├── initiator/          # Survey initiator views
        │       ├── teacher/            # Teacher views
        │       └── respondent/         # Public survey response views
        ├── css/style.css
        └── js/main.js
```

---

## Setup Instructions

### 1. Database Setup

```sql
-- In MySQL:
SOURCE src/main/resources/schema.sql;
```

This creates the `course_eval_db` database, all tables, and seeds:
- Admin user: `admin` / `admin123`
- Initiator user: `initiator1` / `init123`
- 3 sample courses

### 2. Configure Database

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/course_eval_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD

mail.host=smtp.gmail.com
mail.port=587
mail.username=your-email@gmail.com
mail.password=your-app-password
```

> **Note:** For Gmail, use an [App Password](https://myaccount.google.com/apppasswords), not your regular password.  
> Email sending is non-blocking — the app works even if email is not configured.

### 3. Build

```bash
mvn clean package
```

### 4. Deploy

Copy the generated `target/course-eval-system.war` to your Tomcat `webapps/` directory, then start Tomcat.

Or run directly with Tomcat Maven plugin (add to pom.xml if needed):

```bash
mvn tomcat7:run
```

Access at: `http://localhost:8080/course-eval-system/`

---

## User Roles & Demo Accounts

| Role       | Username    | Password  | Notes                            |
|------------|-------------|-----------|----------------------------------|
| Admin      | `admin`     | `admin123`| Full system management           |
| Initiator  | `initiator1`| `init123` | Create/manage surveys            |
| Teacher    | (register)  | (set)     | Needs admin approval after signup|
| Respondent | (any)       | —         | Can respond to guest-access surveys without login |

---

## Functional Requirements Coverage

| ID   | Requirement              | Implemented |
|------|--------------------------|-------------|
| FR1  | Teacher Registration     | ✅           |
| FR2  | Teacher Approval         | ✅           |
| FR3  | Course Management        | ✅           |
| FR4  | Teacher Assignment       | ✅           |
| FR5  | Survey Creation          | ✅           |
| FR6  | Survey Questions         | ✅           |
| FR7  | Survey Options           | ✅           |
| FR8  | Survey Access Settings   | ✅           |
| FR9  | View Surveys             | ✅           |
| FR10 | Respond to Survey        | ✅           |
| FR11 | Email Confirmation       | ✅           |
| FR12 | Survey Results           | ✅           |
| FR13 | Survey Editing           | ✅           |
| FR14 | Survey Deletion          | ✅           |
| FR15 | Teacher Dashboard        | ✅           |
| FR16 | Initiator Dashboard      | ✅           |

---

## Database Schema (ERD Summary)

```
roles (id, name)
users (id, username, password, email, full_name, role_id→roles, status)
courses (id, code, title, description)
teacher_courses (id, teacher_id→users, course_id→courses)
surveys (id, title, description, course_id→courses, initiator_id→users, access_type, status)
survey_questions (id, survey_id→surveys, question_text, question_type, display_order)
survey_options (id, question_id→survey_questions, option_text, display_order)
respondents (id, survey_id→surveys, user_id→users?, guest_email)
survey_responses (id, respondent_id→respondents, question_id→survey_questions, option_id→survey_options?, text_answer)
```
