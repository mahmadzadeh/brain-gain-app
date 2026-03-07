# Spatial Planning Game – Implementation Plan

## Goal
Add the Spatial Planning puzzle (tree-shaped frame with numbered balls) to the Brain Gain app, keeping UX/architecture consistent with existing games while allowing future variants of the board layout.

## Feature Skeleton
- **Packages**: create `com.spatialplanning` with `game` (logic/state), `ui.mainscreen` (activity, fragments, custom views), and `util` (timers/config). Mirror structure/patterns from existing games such as `com.monkeyladder`.
- **Entry point**: new tile and icon in `activity_game_selection.xml` plus `R.drawable/spatial_planning_icon`. `GameSelectionActivity` launches `com.spatialplanning.ui.mainscreen.MainActivity` via `CountdownActivityIntent`.
- **Resources**: instructions copy, strings (title, buttons, stats labels), colors if needed, and animations/sounds reusing the shared `SoundPlayer`.

## Data Model & Logic
- **Graph representation**: the board has 12 fixed slots (6 on the left branches, 6 on the right). Junctions are structural only; each slot knows which positions lie farther out on the same branch so we can enforce blocking rules. When checking `canMove(from)` or placing onto `to`, we ensure those outer nodes are empty.
- **Reference layout** (slot ids used in code):

```
              TOP_LEFT                    TOP_RIGHT
                    \                    /
                     \                  /
                 MID_LEFT_INNER    MID_RIGHT_INNER
                /          \      /            \
    MID_LEFT_OUTER   BOTTOM_LEFT_INNER   BOTTOM_RIGHT_INNER   MID_RIGHT_OUTER
                             |                          |
                 BOTTOM_LEFT_MIDDLE         BOTTOM_RIGHT_MIDDLE
                             |                          |
                 BOTTOM_LEFT_OUTER          BOTTOM_RIGHT_OUTER
```

- Moves follow the branches: outermost slots are always movable; inner slots become movable only after every outward slot on that branch is empty. The same blocker list is used when placing a ball into a slot so we never insert a ball “behind” another one.
- **Core classes**:
  - `SpatialTree` — holds nodes, validates moves (`canMove`, `move`), detects solved state, tracks move count.
  - `SpatialTreeGenerator` — produces solvable layouts per difficulty (varying branch depth, number of balls, time limits). Use shuffle + solver/backtracking to avoid impossible states.
  - `SpatialPlanningScore` — encapsulates moves, elapsed time, accuracy, bonus logic.
- **Persistence hooks**: follow the precedent from other games (`ProgressCounter`, shared prefs) to store high scores, last level, streaks.

## UI Flow
- `SpatialPlanningActivity` hosts the gameplay scene.
  1. Receives difficulty/level from intent.
  2. Shows instruction dialog.
  3. Starts countdown then initiates timer/tracking.
- **Custom view** (`SpatialTreeView`):
  - Draws the tree frame and numbered discs.
  - Highlights movable slots, handles tap-to-select or drag-and-drop, enforces single active move.
  - Animates moves (position interpolation, sound cues).
- **HUD/Overlays**:
  - Move counter, timer, pause/restart buttons.
  - Completion dialog summarizing score, best, next steps (Retry, Next Level, Exit).

## Integration
- Wire sounds/timers via utilities in `com.util` (e.g., `SoundPlayer`, `TimerUtil`).
- Hook analytics or progress tracking if other games emit events (review existing implementations for consistency).
- Update navigation/back-stack handling so the activity returns to the game selection screen gracefully.

## Testing & QA
- **Unit tests** (`app/src/test`):
  - `SpatialTreeTest` for move legality, blocker logic, solver verification, score calculations.
  - `SpatialTreeGeneratorTest` to confirm difficulty outputs expected node counts and solvability.
- **Instrumented tests** (`app/src/androidTest`):
  - Basic UI smoke test: launch activity, perform a legal move, rotate screen, ensure state persists.
  - Accessibility checks for TalkBack labels if instrumentation allows.
- **Manual QA**:
  - Verify drag/tap interactions, countdown integration, pause/resume, sound toggle, color contrast (light/dark), tablet/phone layouts, and behavior when abandoning/restarting levels.

## Next Steps
1. Create package skeleton, resources, and selection tile.
2. Implement core logic classes with unit tests.
3. Build the custom view + activity and hook up UI flow.
4. Integrate persistence/analytics, run tests, and perform manual QA.
