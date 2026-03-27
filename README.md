# 📖 ReadIt

A full-stack community platform for students and developers to share interview experiences, career advice, and study notes.

---

## 🚀 Tech Stack

### Backend
- Java 21
- Spring Boot 3
- Spring Security + JWT Authentication
- JPA / Hibernate
- MySQL

### Frontend
- React 18 (Create React App)
- React Router v6
- Axios
- CSS Modules

---

## ✨ Features

- 🔐 User registration and login with JWT
- 📝 Create, view, and delete career posts
- 📚 Upload and download study notes (PDF, DOC, PPT, etc.)
- 👍 Upvote and downvote posts and notes
- 💬 Comments with threaded replies on posts and notes
- 🗂️ Filter posts by category (Interview Experience, Career Advice, General)
- 🔍 Search notes by title or subject
- 📄 Paginated feed and notes list

---

## 📁 Project Structure

```
readit/
├── readit-backend/          # Spring Boot backend
│   └── src/main/java/com/readit/
│       ├── config/          # SecurityConfig
│       ├── controller/      # REST controllers
│       ├── dto/
│       │   ├── request/     # Request DTOs
│       │   └── response/    # Response DTOs
│       ├── entity/          # JPA entities
│       ├── exception/       # Global exception handling
│       ├── repository/      # Spring Data JPA repositories
│       ├── security/        # JWT filter, JwtUtil, UserDetailsService
│       └── service/         # Business logic
│
└── readit-frontend/
    └── frontend/            # React app
        └── src/
            ├── api/         # Axios instance
            ├── context/     # AuthContext (global auth state)
            ├── components/  # Reusable components
            │   ├── Navbar/
            │   ├── PostCard/
            │   ├── NoteCard/
            │   ├── VoteButtons/
            │   └── CommentSection/
            └── pages/       # Page components
                ├── Home/
                ├── Login/
                ├── Register/
                ├── CreatePost/
                ├── PostDetail/
                ├── Notes/
                ├── UploadNote/
                └── NoteDetail/
```

---

## ⚙️ Backend Setup

### Prerequisites
- Java 21
- Maven
- MySQL

### 1. Create the database

```sql
CREATE DATABASE readit_db;
```

### 2. Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/readit_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.jwt.secret=your_super_secret_key_minimum_32_characters
app.jwt.expiration=86400000

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 3. Run the backend

```bash
cd readit-backend
./mvnw spring-boot:run
```

Backend starts at: `http://localhost:8080`

---

## 🖥️ Frontend Setup

### Prerequisites
- Node.js 18+
- npm

### 1. Install dependencies

```bash
cd readit-frontend/frontend
npm install
```

### 2. Start the development server

```bash
npm start
```

Frontend starts at: `http://localhost:3000`

> ⚠️ Make sure the backend is running before starting the frontend.

---

## 🔌 API Endpoints

### Auth — `/api/auth` (public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Posts — `/api/posts` (🔒 JWT required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/posts` | Get paginated feed |
| GET | `/api/posts/{id}` | Get single post |
| GET | `/api/posts/category/{category}` | Filter by category |
| POST | `/api/posts` | Create a post |
| PUT | `/api/posts/{id}` | Update a post (owner only) |
| DELETE | `/api/posts/{id}` | Delete a post (owner only) |
| POST | `/api/posts/{id}/vote` | Upvote or downvote |

### Notes — `/api/notes` (🔒 JWT required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notes` | Get paginated notes list |
| GET | `/api/notes/{id}` | Get single note |
| POST | `/api/notes` | Upload a note (multipart/form-data) |
| DELETE | `/api/notes/{id}` | Delete a note (owner only) |
| POST | `/api/notes/{id}/vote` | Upvote or downvote |

### Comments — (🔒 JWT required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/posts/{id}/comments` | Get comments for a post |
| POST | `/api/posts/{id}/comments` | Add comment to a post |
| GET | `/api/notes/{id}/comments` | Get comments for a note |
| POST | `/api/notes/{id}/comments` | Add comment to a note |
| DELETE | `/api/comments/{id}` | Delete a comment (owner only) |

### Post Categories
```
INTERVIEW_EXPERIENCE
CAREER_ADVICE
GENERAL
```

### Vote Types
```
UPVOTE
DOWNVOTE
```

---

## 🔐 Authentication

All protected endpoints require a Bearer token in the request header:

```
Authorization: Bearer <your_jwt_token>
```

The token is returned on login and register, stored in `localStorage`, and automatically attached to every request via the Axios interceptor.

---

## 📬 Postman Quick Test

**1. Register**
```json
POST http://localhost:8080/api/auth/register
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "123456"
}
```

**2. Login**
```json
POST http://localhost:8080/api/auth/login
{
  "email": "test@example.com",
  "password": "123456"
}
```

**3. Create Post**
```json
POST http://localhost:8080/api/posts
Authorization: Bearer <token>
{
  "title": "My Google Interview Experience",
  "content": "Here is how it went...",
  "category": "INTERVIEW_EXPERIENCE"
}
```

**4. Upload Note** (form-data)
```
POST http://localhost:8080/api/notes
Authorization: Bearer <token>
title      → My DSA Notes
subject    → Data Structures
description→ Complete revision notes
file       → [select file]
```

---

## 🗄️ Database Schema Overview

| Table | Description |
|-------|-------------|
| `users` | Stores user accounts |
| `posts` | Career posts and interview experiences |
| `notes` | Uploaded study notes with file info |
| `comments` | Threaded comments on posts and notes |
| `votes` | Upvotes/downvotes on posts and notes |

---

## 🛣️ Frontend Routes

| Route | Page | Auth Required |
|-------|------|---------------|
| `/login` | Login page | ❌ |
| `/register` | Register page | ❌ |
| `/` | Home feed | ✅ |
| `/posts/create` | Create post | ✅ |
| `/posts/:id` | Post detail + comments | ✅ |
| `/notes` | Notes list | ✅ |
| `/notes/upload` | Upload note | ✅ |
| `/notes/:id` | Note detail + comments | ✅ |

---

## 🔮 Future Plans

- [ ] Excalidraw collaborative whiteboard integration
- [ ] User profile pages
- [ ] Edit posts and notes
- [ ] Bookmark posts and notes
- [ ] Email verification
- [ ] File preview (PDF viewer in browser)
- [ ] Dark mode

---

## 👨‍💻 Author

Built with ❤️ using Spring Boot + React.
