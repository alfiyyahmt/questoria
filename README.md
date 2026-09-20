# QUESTORIA

## Personal Steam Game Backlog & Quest Management System

QUESTORIA is a web-based game management system designed to help Steam users organize, track, and manage their personal game collections.

The application integrates with Steam to retrieve the user's Steam account and game information. Users can manage their game backlog, track game progress, participate in quests, write reviews, view Steam friends, and manage their profile.

QUESTORIA is developed as an Object-Oriented Programming (OOP) project using Java and Spring Boot.

## Features

### Steam Authentication

Users can sign in to QUESTORIA using their Steam account through Steam OpenID.

### Home

The Home page provides an overview of QUESTORIA and allows users to search for Steam games.

### Library

The Library displays games owned by the user's Steam account.

Users can view:
- Game artwork
- Game title
- Developer
- Game status
- Personal review information

### Backlog

The Backlog allows users to organize games they want to play.

Users can:
- Add games to their backlog
- Change game status
- Track game progress
- Add personal notes
- Mark games as completed
- Write reviews

### Quests

Quests are created by the administrator or web owner.

Users can:
- Browse available quests
- Join quests
- View quest targets
- Track quest progress
- Complete quests
- Obtain achievements

### Friends

The Friends page displays the user's Steam friends.

Users can view:
- Steam avatar
- Steam username
- Steam profile

### Reviews

Users can create reviews for games.

Each review contains:
- Game
- Rating
- Review content
- Reviewer
- Creation date

### Profile

The Profile page displays the user's Steam account information and QUESTORIA profile information.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Spring Boot | Backend framework |
| Spring MVC | Application architecture |
| Spring Data JPA | Database access |
| Hibernate | Object-relational mapping |
| Thymeleaf | Server-side HTML rendering |
| MySQL | Database |
| Steam Web API | Steam account and user data |
| Steam Store API | Game information |
| Steam OpenID | Steam authentication |
| HTML | Web page structure |
| CSS | User interface |
| Maven | Dependency and project management |
| IntelliJ IDEA | Development environment |

## Architecture

QUESTORIA follows the Model-View-Controller (MVC) architecture.

User → View (Thymeleaf) → Controller → Service → Repository → MySQL Database

The Service layer also communicates with the Steam API when external Steam data is required.

### Model

The Model represents the application's data and domain objects.

Main domain objects include:
- Player
- Game
- BacklogItem
- Quest
- QuestPlayer
- Review

### View

The View is implemented using Thymeleaf templates.

Main pages include:
- Home
- Library
- Backlog
- Quests
- Friends
- Reviews
- Profile
- Game Detail

### Controller

Controllers handle HTTP requests and connect the View with the application's business logic.

Examples include:
- HomeController
- LibraryController
- BacklogController
- QuestController
- FriendsController
- ReviewsController
- ProfileController

### Service

Service classes contain the main application logic and coordinate between controllers, repositories, and external APIs.

### Repository

Repository classes handle communication with the MySQL database using Spring Data JPA.

## Steam API Integration

QUESTORIA uses several Steam services.

### Steam OpenID

Steam OpenID is used for user authentication.

### Steam Web API

The Steam Web API is used to retrieve Steam account-related information such as:
- Steam profile
- Owned games
- Steam friends

### Steam Store API

The Steam Store API is used to retrieve game information such as:
- Game name
- Developer
- Genre
- Description
- Header image
- Steam App ID

## Database

QUESTORIA uses MySQL to store application-specific data.

The database stores information related to:
- Players
- Games
- Backlog items
- Quests
- Quest participation
- Quest progress
- Reviews

Steam remains the source of Steam account and game ownership information, while QUESTORIA stores data required for its own application features.

## Project Structure

The main project structure is organized into:

- `controller/` — Handles HTTP requests.
- `service/` — Contains business logic.
- `repository/` — Handles database operations.
- `model/` — Contains application entities and domain objects.
- `config/` — Contains application configuration.
- `templates/` — Contains Thymeleaf HTML pages.
- `static/css/` — Contains the application's stylesheet.

The main application class is `QuestoriaApplication.java`.

## Main Domain Objects

### Player

Represents a QUESTORIA user connected to a Steam account.

### Game

Represents a Steam game used by QUESTORIA.

Game information can include:
- Steam App ID
- Name
- Header image
- Developer
- Genre
- Description

### BacklogItem

Represents a user's personal backlog entry for a game.

It stores information such as:
- Player
- Game
- Status
- Progress
- Notes
- Completion state

### Quest

Represents a challenge created by an administrator or web owner.

A quest can contain:
- Quest name
- Description
- Target
- Achievement name
- Achievement description

### QuestPlayer

Connects a player with a quest and stores the player's quest progress and completion status.

### Review

Represents a user's review of a game.

It contains:
- Player
- Game
- Rating
- Review content
- Creation time

## Application Flow

Open QUESTORIA → Login with Steam → Steam Authentication → Load Steam Account → Load Owned Games → Library

From the Library, users can access:
- Backlog
- Quests
- Friends
- Reviews
- Profile

## Backlog Flow

Search Game → Game Detail → Add to Backlog → Set Status → Update Progress → Complete Game → Write Review

## Quest Flow

Administrator Creates Quest → Quest Appears in QUESTORIA → User Joins Quest → User Progresses Through Quest → Quest Progress Updated → Target Reached → Quest Completed → Achievement Obtained

## Configuration

Sensitive configuration values are provided through environment variables.

Required environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `STEAM_API_KEY`

API keys, database passwords, and other sensitive credentials must not be committed to the repository.

## Requirements

Before running QUESTORIA, make sure the following are installed:

- Java
- Maven
- MySQL
- IntelliJ IDEA or another Java IDE

A Steam Web API key is also required for Steam-related functionality.

## Running the Application

### 1. Clone the Repository

Clone the QUESTORIA repository from GitHub.

### 2. Open the Project

Open the project using IntelliJ IDEA.

### 3. Configure the Database

Create a MySQL database and configure the database connection using the required environment variables.

### 4. Configure Steam API

Set the `STEAM_API_KEY` environment variable with a valid Steam Web API key.

### 5. Run the Application

Run `QuestoriaApplication.java`.

### 6. Open the Application

Open the application URL provided by the Spring Boot server in a web browser.

## UI Design

QUESTORIA uses a clean, minimal, light-themed interface.

The main visual characteristics are:
- White surfaces
- Light gray background
- Muted green accents
- Dark gray typography
- Thin borders
- Small rounded corners
- Minimal shadows
- Wide Steam-style game artwork

Main navigation:
- Home
- Library
- Backlog
- Quests
- Friends
- Reviews
- Profile
- Logout

Detailed UI specifications are documented in `DESIGN.md`.

## Development Guidelines

When developing QUESTORIA:

- Follow the existing MVC architecture.
- Keep business logic inside service classes.
- Keep database operations inside repository classes.
- Keep HTTP request handling inside controllers.
- Use Thymeleaf for server-rendered pages.
- Reuse existing CSS variables and components.
- Follow the visual rules defined in `DESIGN.md`.
- Keep sensitive credentials outside the source code.
- Avoid changing unrelated pages when implementing a feature.
- Preserve the existing sidebar and footer unless a change specifically requires it.

## Documentation

| File | Description |
|---|---|
| `README.md` | Project overview, features, architecture, setup, and development information |
| `DESIGN.md` | UI/UX design system and visual guidelines |
| `HELP.md` | User guide and troubleshooting information |

## Project Status

QUESTORIA is an ongoing academic software engineering project.

Current development areas include:
- Steam authentication
- Steam API integration
- Game library
- Backlog management
- Game progress tracking
- Quest management
- Quest achievements
- Friends
- Reviews
- User profile
- Database integration
- UI consistency
- Deployment preparation

## License

This project is developed for educational purposes as part of an Object-Oriented Programming project.

Steam and related Steam trademarks belong to Valve Corporation.

QUESTORIA is an independent educational project and is not affiliated with or endorsed by Valve Corporation.
