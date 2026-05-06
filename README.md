# 🧭 Pathfinding Lab: Modular NPC Navigation

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Swing-blue.svg)]()
[![Status](https://img.shields.io/badge/Phase-2-success.svg)]()

A modular, interactive 2D simulation engine built from scratch in Java Swing. This laboratory allows users to design 2D environments and observe autonomous agents navigate complex terrain using the **A* (A-Star) Pathfinding Algorithm**.

## 🚀 Key Features
* **Real-time Map Editor:** Paint walls, define player targets, and spawn NPCs on the fly.
* **Autonomous AI:** NPC agents use a heuristic-based A* engine to calculate the shortest path dynamically.
* **Viewport System:** Interactive "Camera" viewport that centers on the player during navigation.
* **Decoupled Architecture:** Clean separation between rendering, logic, and user input.
* **Safety First:** Multi-check safety breaks to prevent UI thread locking during complex calculations.

## 🎨 Visual Legend
| Element | Visual | Role |
| :--- | :--- | :--- |
| **Grass** | Sea Green | Walkable terrain |
| **Wall** | Dark Grey | Impassable (Weight: $\infty$) |
| **Player** | White | Controllable Target |
| **NPC** | Cyan | Autonomous Follower |
| **Path** | Translucent Yellow | Visualizes the "Brain" |

## ⚙️ Technical Specs
* **Grid Size:** 25x25 (Dynamic)
* **Rendering:** Java 2D Graphics Engine
* **AI Heuristic:** Manhattan Distance
* **Input Control:** KeyBindings (W/A/S/D) & Mouse-based Editor

## 🛤 Roadmap
- [x] A* Pathfinding implementation
- [x] Viewport & Camera follow
- [ ] **Sprite Layer:** Asset-based textures instead of blocks.
- [ ] **Weighted Terrain:** Mud/Road cost-based navigation.
- [ ] **AffineTransform:** Implement matrix-based zooming/rotation.