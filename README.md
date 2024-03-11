### Shared Whiteboard Painter

This repository contains Java code for creating a shared painting/chat surface that works between machines. The implementation is divided into two steps:

### Step 1: Stand-alone GUI Painter
Painter Class
- Painter: Main class containing GUI components for shape and color selection.
- PaintingPrimitive: Abstract class for drawing primitives.
- Line and Circle: Concrete subclasses for lines and circles drawing.
- PaintingPanel: Extends JPanel for painting on canvas.

### Step 2: Networking the Painters
Architecture
- Utilizes a central hub for communication.
- Hub class sets up ServerSocket and handles communication among Painters.
- Painters connect to the Hub and exchange messages through it.
