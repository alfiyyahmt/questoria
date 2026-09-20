# QUESTORIA Help Guide

This document provides instructions for using QUESTORIA and troubleshooting common problems.

## 1. Getting Started

QUESTORIA is a Steam game management application.

The main features are:

- Steam login
- Game library
- Backlog management
- Game progress tracking
- Quests
- Achievements
- Friends
- Reviews
- Profile

## 2. Logging in with Steam

1. Open the QUESTORIA application.
2. Select **Login with Steam**.
3. You will be redirected to Steam for authentication.
4. Log in to your Steam account.
5. Authorize the authentication request.
6. After successful authentication, you will be redirected back to QUESTORIA.

Your Steam account is then connected to your QUESTORIA session.

## 3. Viewing Your Library

Open **Library** from the sidebar.

The Library displays games associated with your Steam account.

Each game may display:

- Game artwork
- Game title
- Developer
- Status
- Personal review information

Steam game ownership information is retrieved through the Steam API.

## 4. Searching for a Game

Game search is available from the Home page.

1. Open **Home**.
2. Enter a game name in the search field.
3. Select the desired game from the search results.
4. Open the game detail page to view additional information.

## 5. Viewing Game Details

The Game Detail page displays information about a selected game.

Information may include:

- Game artwork
- Game title
- Developer
- Genre
- Description
- Steam App ID

The page also provides actions such as adding the game to the backlog.

## 6. Adding a Game to the Backlog

1. Open a game's detail page.
2. Select **+ Add to Backlog**.
3. The game will be added to your personal backlog.
4. Open **Backlog** to manage the game.

## 7. Managing the Backlog

Open **Backlog** from the sidebar.

A backlog item can contain:

- Game
- Status
- Progress
- Notes
- Completion state

Use the available controls to update your game progress and status.

## 8. Tracking Game Progress

Progress can be updated from the Backlog.

Update the progress according to your current game completion.

The progress indicator represents the current completion level.

When the game reaches the required completion state, it can be marked as completed.

## 9. Adding Notes

Backlog items can contain personal notes.

Notes can be used to record:

- Personal thoughts
- Things to remember
- Goals
- Game progress information
- Future plans

Save the changes after editing the note.

## 10. Completing a Game

When you finish a game:

1. Open the game in your Backlog.
2. Update the progress.
3. Set the appropriate completion status.
4. Save the changes.

Completed games can also contribute to relevant quest progress.

## 11. Joining a Quest

Open **Quests** from the sidebar.

To participate in a quest:

1. Open a quest.
2. Review the quest description.
3. Check the target.
4. Select the join option.

After joining, the quest will track your individual progress.

## 12. Quest Progress

Quest progress represents your personal progress toward the quest target.

For example:

**Quest Target:** Complete 5 games

**Current Progress:** 3 / 5

**Remaining:** 2 games

Once the required target is reached, the quest can be completed.

## 13. Quest Achievements

Completed quests may provide an achievement.

An achievement can contain:

- Achievement name
- Achievement description
- Completion status

Achievements are associated with quests created by the administrator or web owner.

## 14. Viewing Steam Friends

Open **Friends** from the sidebar.

The Friends page displays available Steam friends.

Each friend may display:

- Avatar
- Steam username
- Steam profile link

Selecting the Steam profile link opens the corresponding Steam profile.

## 15. Writing a Review

Reviews can be created for games.

A review contains:

- Rating
- Review text
- Game
- Reviewer
- Creation date

To write a review:

1. Open the relevant game.
2. Select the review option.
3. Enter a rating.
4. Write your review.
5. Submit the review.

## 16. Viewing Reviews

Open **Reviews** from the sidebar.

Depending on the available filter, users can view:

- All reviews
- Their own reviews
- Friends' reviews

## 17. Viewing Your Profile

Open **Profile** from the sidebar.

The Profile page contains information related to the connected Steam account.

The profile may display:

- Steam avatar
- Steam username
- Steam information
- Steam profile link

## 18. Logging Out

To end the current QUESTORIA session:

1. Open the sidebar.
2. Select **Logout**.

The current session will be terminated.

# Troubleshooting

## 19. Steam Login Does Not Work

If Steam authentication fails, check:

- Steam API configuration
- Steam OpenID configuration
- Application URL
- Whether the application is running
- Whether the Steam account can be accessed normally

Check the application console for additional error information.

## 20. Games Are Not Appearing

If games do not appear in the Library:

1. Make sure you are logged in with Steam.
2. Check that the Steam account owns games.
3. Check whether the Steam game library is visible.
4. Check the Steam API key.
5. Check the application console for API errors.

Some Steam information may not be available when account or game privacy settings restrict access.

## 21. Steam Friends Are Not Appearing

If no friends are displayed:

- Check whether your Steam friends list is available.
- Check your Steam privacy settings.
- Make sure the Steam Web API is configured correctly.
- Check the application console for API errors.

A private Steam profile or restricted friends list may prevent friend information from being retrieved.

## 22. Game Search Returns No Results

If a game cannot be found:

- Check the spelling of the game name.
- Try a shorter search term.
- Check your internet connection.
- Check the Steam Store API configuration.
- Check the application console for API errors.

## 23. Database Connection Error

If QUESTORIA cannot connect to MySQL, check:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Also make sure:

- MySQL is running.
- The database exists.
- The database credentials are correct.
- The configured database URL is valid.

## 24. Environment Variables Are Not Detected

If Spring Boot reports that a variable is missing, verify that the required environment variables have been configured.

Required variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `STEAM_API_KEY`

After changing environment variables, restart the application or IntelliJ IDEA if necessary.

## 25. Application Does Not Start

Check the following:

1. Java is installed.
2. Maven dependencies are available.
3. MySQL is running.
4. Environment variables are configured.
5. `application.properties` is correctly configured.
6. There are no compilation errors.

Run the application again after resolving the reported error.

## 26. Database Tables Are Missing

If a required table does not exist, check the JPA configuration:

`spring.jpa.hibernate.ddl-auto=update`

Then restart the application.

If the problem continues, check:

- Database connection
- Entity classes
- JPA configuration
- Application logs

## 27. Steam API Key Problems

If Steam API requests fail, verify that:

`STEAM_API_KEY`

contains a valid Steam Web API key.

Do not place the API key directly inside source code or commit it to GitHub.

## 28. CSS or UI Changes Are Not Visible

If changes to the interface are not appearing:

1. Save the CSS file.
2. Restart the application if necessary.
3. Refresh the browser.
4. Perform a hard refresh.
5. Check that the correct stylesheet is loaded.

The main stylesheet is:

`src/main/resources/static/css/style.css`

The visual rules are documented in:

`DESIGN.md`

## 29. Thymeleaf Page Does Not Render

If a page does not render correctly, check:

- HTML file location
- Thymeleaf syntax
- Controller mapping
- Model attributes
- Template variable names
- Application logs

Templates are located in:

`src/main/resources/templates/`

## 30. API Data Is Missing

If a page loads but some Steam information is missing, the problem may be caused by:

- Steam API restrictions
- Private Steam profile settings
- Missing API response data
- Invalid Steam App ID
- API request failure
- Network problems

The application should handle unavailable information without preventing the rest of the page from loading.

## 31. Development Notes

When making changes to QUESTORIA:

- Follow the existing MVC architecture.
- Avoid placing business logic directly inside templates.
- Keep database operations inside repositories.
- Keep business logic inside services.
- Keep request handling inside controllers.
- Reuse existing CSS styles.
- Follow `DESIGN.md`.
- Avoid unnecessary redesigns.
- Do not expose API keys.
- Do not commit passwords or database credentials.

## 32. Documentation Files

QUESTORIA contains three main documentation files:

- `README.md` — Project overview, features, architecture, setup, and development information.
- `DESIGN.md` — UI/UX design system and visual guidelines.
- `HELP.md` — User guide and troubleshooting information.