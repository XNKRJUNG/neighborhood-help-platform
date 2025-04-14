# 🏘️ Neighborhood Help Platform

A community-driven web application where people can post tasks and get help from nearby helpers. Think of it as the neighborhood version of TaskRabbit or Yelp — starting with home repairs, errands, and local assistance.

---

## 🚀 Tech Stack

| Layer     | Tech                          |
|-----------|-------------------------------|
| Backend   | Spring Boot (Java)            |
| Database  | PostgreSQL                    |
| Auth      | JWT (Spring Security)         |
| ORM       | Spring Data JPA (Hibernate)   |
| Testing   | Postman,                      |
| Infra     | REST API, Future AWS support  |

---

## 📦 Features (In Progress)

- 👥 User registration with legal name + phone/email verification
- 🛠️ Post jobs with optional photos and price ranges
- 🎯 Helpers can bid on jobs, select skill sets
- ✅ Verification badges (Mobile, Background, Payment)
- ⭐ Reviews & Ratings after job completion
- 📸 Upload photos for profiles, jobs, and reviews

---

## 📁 Folder Structure (Backend)

```
src/main/java/site/shresthacyrus/neighborhoodhelpplatform/
├── model/        → JPA Entities (User, Job, Bid, Review, etc.)
├── repository/   → Spring Data Repositories
├── controller/   → REST API Endpoints (WIP)
├── service/      → Business logic layer (WIP)
├── common/       → Enums, constants (e.g., RoleEnum, JobStatusEnum)
```

---

## 🛠️ Getting Started

```bash
# 1. Clone the repo
git clone https://github.com/yourusername/neighborhood-help-platform.git
cd neighborhood-help-platform

# 2. Open in IntelliJ or your IDE of choice

# 3. Setup PostgreSQL DB locally
#    (e.g., create db 'neighborhood_help' and set credentials in application.properties)

# 4. Run the app
```

---

## ✅ Current Progress

- [x] User, Job, Bid, Review, Skill, and Photo entities complete
- [x] Relationships + PrePersist hooks wired
- [ ] DTOs + Controllers coming soon
- [ ] Frontend integration coming soon

---

## 🙋‍♂️ Solo Developer

This project is built solo by ["Cyrus" Yogesh Shrestha](https://github.com/XNKRJUNG) as a full-stack engineering exercise with real-world architecture to help the neighborhood.

---

## 🌱 License

MIT License — feel free to fork or build on top!
