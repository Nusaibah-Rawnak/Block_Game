# Block_Game

## Introduction

This project was developed as part of COMP 250 Winter 2024. It involves implementing a block game using a recursive quad-tree structure. Players can interact with the game by performing various actions such as rotating, reflecting, and smashing blocks to achieve specific goals.

The primary objective of the project is to strengthen understanding of recursion, data structures, and Java programming concepts.

## Package Structure

The project contains the following source files, all organized under the `Block_Game` package:

1. **Block.java**
2. **PerimeterGoal.java**
3. **BlobGoal.java**

## Class Descriptions

### 1. Block.java
This class represents a recursive block structure. Each block can either be a solid color or subdivided into four smaller blocks (children).

#### Key Methods
- **`Block(int level, int maxDepth)`**: Constructs a block with random colors and subdivisions up to the specified maximum depth.
- **`updateSizeAndPosition(int size, int xCoord, int yCoord)`**: Updates the size and coordinates of the block and its children.
- **`getSelectedBlock(int x, int y, int level)`**: Returns the block at the specified level containing the given coordinates.
- **`reflect(int direction)`**: Reflects the block horizontally or vertically.
- **`rotate(int direction)`**: Rotates the block clockwise or counterclockwise.
- **`smash()`**: Subdivides the block into four new randomly colored sub-blocks.
- **`flatten()`**: Returns a 2D array representation of the block structure.

### 2. PerimeterGoal.java
This class extends the abstract `Goal` class and implements scoring based on the number of blocks of a target color along the perimeter of the board.

#### Key Methods
- **`score(Block board)`**: Computes the score by counting blocks of the target color along the perimeter. Corner blocks count twice towards the final score.
- **`description()`**: Returns a description of the goal.

### 3. BlobGoal.java
This class extends the abstract `Goal` class and implements scoring based on the largest connected group (blob) of blocks of a target color.

#### Key Methods
- **`score(Block board)`**: Computes the score by finding the largest connected blob of the target color. Only orthogonally connected blocks are counted.
- **`description()`**: Returns a description of the goal.

## How to Run

1. Place the source files (`Block.java`, `PerimeterGoal.java`, `BlobGoal.java`) in a package named `Block_Game`.
2. Compile the Java files using:
   ```bash
   javac Block_Game/*.java
   ```
3. Run the main program.

## Success Criteria

- The `Block` class correctly represents a recursive quad-tree structure.
- The `PerimeterGoal` and `BlobGoal` classes compute scores accurately based on their respective criteria.
- All methods adhere to the provided specifications and constraints.

## Future Enhancements

- Add more goal types for varied gameplay.
- Implement additional visualizations for the block structure.

## Contact

For any inquiries or feedback, please contact Nusaibah Binte Rawnak at nusaibah.rawnak@mail.mcgill.ca.

## License

This project is licensed under the MIT License.
