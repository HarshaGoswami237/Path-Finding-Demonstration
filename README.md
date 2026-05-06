# 🧭 Pathfinding Lab: Modular NPC Navigation

A clean, modular 2D grid-based engine built in Java Swing. This project focuses on decoupled architecture, allowing for real-time map editing and algorithmic pathfinding.

## 🚀 Current State: Phase 2 (Autonomous AI & Lifecycle)
We have successfully implemented the A* (A-Star) Search Algorithm* and a simulation state machine. The NPC now possesses "intelligence" to navigate mazes and a defined lifecycle from spawn to objective.

### 🏗 Architecture Overview
- **`Main.java`**: The Orchestrator. Uses BorderLayout to separate the workspace into a Center (World) and East (Tools).
- **`GamePanel.java`**: The World Engine. Coordinates between the Map data and the Pathfinder. Manages the 60FPS render loop.
- **`ControlPanel.java`**: The Input Interface. Bridges user intent (buttons) to the Game state, including the new Simulation Toggle.
- **`Pathfinder.java`**: The Logic Engine. An isolated A* implementation that calculates shortest paths using the Manhattan distance heuristic.
- **`Node.java`**: The Data Model. Represents individual grid coordinates with cost values ($f, g, h$).
- **`Node.java`**: Add that it implements Comparable to allow the PriorityQueue in the Pathfinder to automatically sort by the lowest $f$ cost.
### 🧠 Logic & Reasoning (The "Why")
1. **Simulation Toggle (Start/Stop)**  
   **Reasoning:** In an editor-first environment, the user needs a "Drafting Phase."  
   **The Logic:** A global `isRunning` boolean acts as a gatekeeper. This allows the user to paint complex obstacle courses without the NPC moving prematurely.

2. **Entity Lifecycle (Collision & Destruction)**  
   **Reasoning:** To provide clear feedback for "Mission Success" without intrusive pop-up windows.  
   **The Logic:** When `NPC_Position == Player_Position`, the `npcActive` flag is set to false. This removes the NPC from the rendering loop. Re-placing the NPC via the sidebar tool "respawns" the entity.

3. **Safety-First Pathfinding (Anti-Freeze)**  
   **Reasoning:** Java Swing's UI thread (EDT) will freeze if a loop runs too long.  
   **The Logic:** We implemented a `closedSet` (HashSet) to prevent re-checking nodes and a `maxChecks` limit. If a path is blocked, the NPC stops gracefully instead of crashing the application.

### 🎨 Visual Legend
| Element | Color | Tile Logic |
| :--- | :--- | :--- |
| **Grass** | Sea Green | Walkable |
| **Wall** | Dark Grey | Impassable (Weight: $\infty$) |
| **Player** | White | The dynamic target (Goal Node) |
| **NPC** | Cyan | The autonomous agent (Start Node) |
| **Path** | Translucent Yellow | Visualizes the NPC's "Plan" |

### ⚙️ Technical Specs
- **Grid Size:** 10 x 10
- **Tile Scaling:** 64px (Optimized for 640px viewport)
- **AI Heuristic:** Manhattan Distance ($|x_1 - x_2| + |y_1 - y_2|$)
- **Tick Rate:** 300ms per NPC step (Adjustable in GamePanel)
- **Input Handling:** Non-blocking key bindings using ActionMap for smooth performance.

### 🛠 Features
- [x] **Real-time Map Editor:** Toggle walls and relocate entities on the fly.
- [x] **A* Pathfinding:** Intelligent obstacle avoidance.
- [x] **Simulation Control:** Manual Start/Stop toggle for navigation.
- [x] **Auto-Destruction:** NPC is removed upon successful capture of the target.
- [x] **Safe Execution:** Multi-check safety breaks to prevent UI thread locking.
- [x] **Map Reset:** One-click clear function to wipe all obstacles and reset entity positions.
- [x] **Keyboard Control:** Integrated InputMap for real-time W/A/S/D player movement with boundary and wall collision detection.
### 🛤 Roadmap
- **Sprite Layer:** Replace color blocks with PNG textures (Grass, Stone, Character sprites).
- **Keyboard Control:** Add W/A/S/D support to move the Player in real-time.
- **Weighted Terrain:** Add "Mud" or "Puddle" tiles that slow down the NPC.
- **Movement Smoothing:** Transition from grid-snapping to pixel-perfect sliding.