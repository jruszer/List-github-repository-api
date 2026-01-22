# GitlistApplication

## 🇺🇸 English

### Description
REST API designed to list all public GitHub repositories for a given user without forks. The response includes:
- Repository Name
- Owner Login
- List of branches
- SHA of the last commit for each branch

### Tech Stack
- **Language:** Java 25
- **Framework:** Spring Boot 4.0.1
- **Build Tool:** Gradle

### Error Handling
404 Not Found: Use when the GitHub user does not exist.

### API Endpoints
| Method | URL | Description |
| :--- | :--- | :--- |
| `GET` | `/api/github/repos/{username}` | Lists repositories for the specified user |

### How to Run

1. Clone the repository.
2. Navigate to the project directory.
3. Run using Gradle wrapper:

```bash
./gradlew bootRun ```

The application will start at http://localhost:8080