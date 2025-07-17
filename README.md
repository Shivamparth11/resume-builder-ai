# Resume Builder with AI Integration 🧠📄

A full-stack Java + Spring Boot based Resume Builder backend enhanced with AI-powered feedback using Python (Flask + NLP). The AI analyzes your resume summary and provides suggestions for improvement.

---

## 💡 Features

- CRUD operations for resume (Create, Read, Update, Delete)
- AI integration for summary analysis using Flask + NLTK
- Detailed feedback based on summary content
- Validations and Global Exception Handling
- RESTful API design

---

## 🧠 AI Feedback Engine

- Python Flask microservice using:
  - NLTK (stopwords, tokens)
  - spaCy (NER)
  - OpenAI API or custom logic
- Runs on `localhost:5000`

---

## 🧪 Tech Stack

| Layer        | Tech                             |
|--------------|----------------------------------|
| Backend      | Java, Spring Boot, Spring Web    |
| Database     | H2 (in-memory)                   |
| AI Service   | Python, Flask, NLTK, spaCy       |
| API Comm     | RestTemplate                     |
| Tools        | Maven, Postman, Git, VS Code     |

---

## 🛠️ Running the Project

### ⚙️ 1. Start Spring Boot Backend

```bash
mvn spring-boot:run

```
### ⚙️ 2. start ai in vs code

```bash
python resume_analyzer.py 
