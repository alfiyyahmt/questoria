# QUESTORIA Design System

## 1. Overview

QUESTORIA is a personal Steam game backlog and quest management system.

The interface uses a clean, simple, light-themed visual style with soft green accents. The design focuses on readability, consistency, and a calm game-tracking experience rather than a highly decorative or gaming-heavy appearance.

The visual identity is based on:
- White surfaces
- Soft light-gray backgrounds
- Muted green accents
- Dark gray text
- Thin borders
- Small rounded corners
- Simple spacing
- Minimal shadows
- Steam-style wide game artwork

## 2. Design Principles

### Clean
The interface should remain simple and uncluttered.

### Consistent
Spacing, typography, buttons, cards, borders, and colors should remain visually consistent across pages.

### Calm
Use muted green and neutral colors instead of bright or aggressive gaming colors.

### Readable
Text hierarchy should be clear and easy to scan.

### Functional
Visual elements should support the user's task and should not be added only for decoration.

## 3. Color System

### Background
`#f7f8f7`

Main page background.

### Surface
`#ffffff`

Used for cards, panels, forms, sidebar, and other main surfaces.

### Soft Surface
`#f3f6f3`

Used for subtle highlighted areas such as achievement sections and supporting content.

### Primary Text
`#20242a`

Primary text color.

### Secondary Text
`#687078`

Used for supporting information and secondary content.

### Muted Text
`#8a9198`

Used for descriptions, metadata, labels, and less important information.

### Border
`#e3e7e4`

Main border color.

### Light Border
`#edf0ee`

Used for subtle separators.

### Primary Green
`#718f79`

Main accent color used for primary buttons, progress bars, links, section kickers, ratings, and selected states.

### Dark Green
`#587160`

Used for emphasized green text and elements.

### Light Green
`#edf3ee`

Used for active navigation, status badges, quest tags, achievement areas, selected filters, and soft highlights.

### Green Hover
`#647f6c`

Used for hover states of primary green elements.

## 4. Typography

The main font is:

`"Segoe UI", Arial, sans-serif`

### Page Title
- Size: 28px
- Weight: 700

### Section Title
- Size: 19px
- Weight: 700

### Card Title
- Size: 18px
- Weight: 700

### Body
- Size: 14px
- Weight: 400

### Secondary
- Size: 13px

### Small
- Size: 11px

## 5. Layout

The application uses a fixed left sidebar with a width of 235px.

The main content occupies the remaining page width.

Main content generally uses a maximum width of 1180px and is centered on the page.

Profile uses a narrower maximum width of 1000px.

Game Detail uses a maximum width of 1120px.

## 6. Sidebar

The sidebar:
- Uses a white background
- Has a thin right border
- Contains the QUESTORIA brand
- Contains the main navigation
- Places Logout at the bottom
- Uses soft green for the active navigation item

Main navigation:

1. Home
2. Library
3. Backlog
4. Quests
5. Friends
6. Reviews
7. Profile
8. Logout

Normal users should not see an Admin menu item in the sidebar.

The active navigation item uses a soft green background and dark green text.

## 7. Search

Game search is primarily available on the Dashboard/Home page.

The search input uses:
- White background
- Thin gray border
- Rounded corners
- Soft green focus state

Search suggestions use wide Steam-style game thumbnails.

## 8. Cards

Cards use:
- White background
- Thin gray border
- Small rounded corners
- Minimal shadow

Hover effects are subtle and should not significantly change the layout.

## 9. Game Artwork

Game artwork uses a wide Steam-style format.

Preferred aspect ratio:

`460 / 215`

Images should:
- Be wide rather than vertical
- Fill the available image area
- Use `object-fit: cover`
- Have slightly rounded corners where appropriate

This style is used consistently across Dashboard, Library, Backlog, Reviews, Game Detail, and Search.

## 10. Dashboard

The Dashboard provides an overview of the user's game activity.

Main elements:
- Welcome section
- Statistics
- Featured games
- Game search
- Other relevant sections

Statistics use three columns on desktop.

Stat cards use:
- White background
- Thin border
- Rounded corners
- Large numeric values
- Small muted descriptions

## 11. Library

The Library displays games owned by the user.

Desktop layout uses three columns.

Each library card contains:
- Wide game artwork
- Game title
- Developer
- Status
- User review rating when available

Library cards should remain compact and easy to scan.

## 12. Backlog

The Backlog uses horizontal game cards on desktop.

Each card contains:
- Wide Steam artwork
- Game title
- Developer
- Status
- Progress
- Progress controls
- Notes
- Review action when applicable

Desktop backlog cards use approximately 460px for the artwork section.

On smaller screens, backlog cards become vertical.

## 13. Progress

Progress indicators use the primary green color.

The progress track uses a light gray background.

The progress fill uses `#718f79`.

Progress bars use rounded ends and remain visually subtle.

## 14. Quests

Quests use a two-column grid on desktop.

Quest cards contain:
- Quest title
- Description
- Quest metadata
- Target
- Progress
- Achievement information
- Join/progress controls
- Completion status

Quest tags and statuses use soft green backgrounds.

Quest progress uses the same progress style as Backlog.

## 15. Quest Achievement

Achievement information is displayed inside a soft highlighted section.

It uses:
- Soft green-gray background
- Light border
- Rounded corners
- Green achievement icon

The achievement section should remain integrated with the quest card.

## 16. Profile

The Profile page uses a narrower layout.

The profile card contains:
- Avatar
- Username
- Steam information
- Profile details
- Steam profile link

The avatar is circular and approximately 94px × 94px.

The Profile page should remain simple and should not use unnecessary decorative elements.

## 17. Reviews

Reviews use horizontal cards on desktop.

Each review contains:
- Game artwork
- Game title
- Reviewer
- Rating
- Review text
- Date

Desktop review cards use approximately 260px for the artwork section.

On smaller screens, review cards become vertical.

Ratings use the primary green color.

## 18. Review Form

The review form uses a white card with a thin border.

Form controls use:
- White background
- Light gray borders
- Rounded corners
- Soft green focus state

The review textarea should have enough height for comfortable writing.

## 19. Game Detail

Game Detail uses a two-column layout on desktop.

The page contains:
- Wide game artwork
- Game title
- Developer
- Description
- Metadata
- Action buttons
- Game information

Game artwork uses the same wide Steam-style ratio as other game components.

On smaller screens, the layout becomes one column.

## 20. Buttons

### Primary Button

Primary actions use `#718f79` with white text.

Used for important actions such as:
- Add to Backlog
- Join Quest
- Submit
- Save

### Secondary Button

Secondary actions use:
- White background
- Light gray border
- Dark gray text

### Danger Button

Danger actions use a muted red instead of a bright red.

## 21. Status Badges

Status badges use:
- Soft green background
- Dark green text
- Pill-shaped border radius

They should remain compact and should not dominate the interface.

## 22. Empty States

Empty states use:
- White background
- Thin border
- Rounded corners
- Centered content
- Soft green icon
- Muted supporting text

The empty state should clearly explain why there is no content.

## 23. Admin

Admin pages use the same visual system as the rest of QUESTORIA.

Admin functionality may exist in the backend and through dedicated admin pages, but Admin should not appear as a normal item in the main user sidebar.

Admin cards and forms use:
- White surfaces
- Thin borders
- Muted green accents
- Simple rounded corners
- Minimal shadows

## 24. Footer

The footer uses:
- White background
- Thin top border
- Simple typography
- Minimal spacing

The footer contains the QUESTORIA name and application description.

The footer should remain visually minimal.

## 25. Responsive Design

### 1200px
Large horizontal components reduce their artwork width.

### 1100px
Game grids reduce from four columns to three.

Library grids reduce from three columns to two.

### 900px
Two-column quest layouts become one column.

Game Detail becomes one column.

### 850px
Backlog artwork becomes narrower.

Review cards become more compact.

### 720px
The sidebar becomes part of the normal page flow.

Main content uses the full width.

Backlog and Review cards become vertical.

### 480px
Game and Library grids become one column.

Statistics become one column.

Sidebar navigation becomes a three-column grid.

Forms and controls become vertically stacked where necessary.

## 26. Interaction

Interactions should remain subtle.

Allowed:
- Small hover movement
- Soft shadows
- Background changes
- Border color changes
- Soft focus states

Avoid:
- Large animations
- Glowing effects
- Neon colors
- Heavy shadows
- Excessive gradients
- Excessive transitions

## 27. Accessibility

The interface should maintain:
- Readable text
- Sufficient contrast
- Visible focus states
- Clear buttons and links
- Responsive layouts
- Usable controls on smaller screens
- Meaningful alternative text for game artwork and avatars

## 28. Visual Identity

QUESTORIA should feel:

- Clean
- Simple
- Calm
- Modern
- Light
- Organized
- Steam-oriented
- Green-accented

The interface should not look:

- Neon
- Cyberpunk
- Dark gaming UI
- Overly colorful
- Highly decorative
- Heavy or crowded

The main visual combination is:

White surfaces + soft gray background + muted green accents + dark gray typography + thin borders + wide Steam-style game artwork.

## 29. Implementation Rule

`style.css` is the source of truth for the actual visual implementation.

`DESIGN.md` describes the intended design system and should remain consistent with the existing CSS.

Future UI changes should:
1. Preserve the existing visual style.
2. Avoid redesigning unrelated pages.
3. Preserve the existing sidebar unless specifically requested.
4. Preserve the existing footer unless specifically requested.
5. Reuse existing colors.
6. Reuse existing typography.
7. Keep game artwork wide and Steam-style.
8. Keep borders subtle.
9. Keep green accents muted.
10. Make only the necessary changes for each UI request.