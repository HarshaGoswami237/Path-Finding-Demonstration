# Technical Documentation: Pathfinding Lab

## 1. Executive Summary
The **Pathfinding Lab** is a modular simulation engine designed to explore autonomous agent navigation within a grid-based environment. The project emphasizes clean code, separation of concerns, and the implementation of fundamental computer science algorithms.

## 2. Architecture & Design
We utilize a **Decoupled Architecture**:
* **World Engine (`GamePanel.java`):** Manages the render loop and grid states.
* **Logic Engine (`Pathfinder.java`):** Isolates the A* implementation.
* **Input Interface (`ControlPanel.java`):** Bridges user tools to the engine.

## 3. Technology Stack & Rationale
| Tool | Why we used it |
| :--- | :--- |
| **Java Swing** | Native, lightweight, and excellent for mastering component-based GUI design without external dependencies. |
| **Java AWT** | Provides low-level access to pixel-based rendering, perfect for custom grid engines. |
| **IntelliJ IDEA** | Superior debugging and refactoring tools essential for modular code. |

## 4. Comparative Study: What else could we use?
In a real-world graphics environment, we would use different approaches to achieve higher performance:

### Graphics Transformation
* **Current:** We use manual coordinate calculation (`c * TILE_SIZE`).
* **Alternative (`AffineTransform`):** An `AffineTransform` matrix system could be used to perform 2D transformations (Rotation, Scaling, Shearing, Translation). It uses matrix math to calculate new coordinates on the fly.
* **Why we skipped it:** `AffineTransform` adds complexity (requires understanding linear algebra matrices) and can be overkill for a simple grid. We focused on mastering the Pathfinding Logic first.

### Engine Libraries
* **Alternative (LibGDX/JOGL):** These libraries offer GPU acceleration and much faster rendering.
* **Why we skipped it:** As a student project, the goal was to *build* the engine, not *configure* a framework. Using raw Java Swing demonstrates that we understand how the rendering loop works under the hood.

## 5. Algorithmic Design (A*)
We chose the **A* Search Algorithm** because it is efficient for finding the shortest path to a single target.
* **Heuristic:** We used **Manhattan Distance** ($|x_1 - x_2| + |y_1 - y_2|$) because grid-based movement restricts entities to 4 axes (Up, Down, Left, Right).
* **Efficiency:** To optimize performance, we implemented a `closedSet` (HashSet) to track visited nodes, preventing redundant calculations and recursive performance dips.