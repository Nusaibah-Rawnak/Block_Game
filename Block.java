import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random();

 public static void main(String args []){
  // lower right level 1 block, (level 2)
  Block[] childrenLevel2 = {
          new Block(12, 8, 4, 2, 2, GameColors.BLUE, new Block[0]),
          new Block(8, 8, 4, 2, 2, GameColors.RED, new Block[0]),
          new Block(8, 12, 4, 2, 2, GameColors.YELLOW, new Block[0]),
          new Block(12, 12, 4, 2, 2, GameColors.BLUE, new Block[0])
  };
  // level 1 block
  Block[] childrenLevel1 = {
          new Block(8, 0, 8, 1, 2, GameColors.GREEN, new Block[0]),
          new Block(0, 0, 8, 1, 2, GameColors.RED, new Block[0]),
          new Block(0, 8, 8, 1, 2, GameColors.YELLOW, new Block[0]),
          new Block(8, 8, 8, 1, 2, null, childrenLevel2)
  };
  // top-level block
  Block topLevelBlock = new Block(0, 0, 16, 0, 2, null, childrenLevel1);

  // recursively setting correct positions and sizes
  topLevelBlock.updateSizeAndPosition(16, 0, 0);

  // printing block and its children
  topLevelBlock.printBlock();
 }
 /*
  * These two constructors are here for testing purposes.
  */
 public Block() {}

 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }


 /*
  * Creates a random block given its level and a max depth.
  *
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
  if (lvl < 0 || lvl > maxDepth) {
   throw new IllegalArgumentException("Level must be between 0 and maxDepth (inclusive)!");
  }

  // we initialize the level and maxDepth
  this.level = lvl;
  this.maxDepth = maxDepth;

  // we do not subdivide the block if it is already in the max depth
  if (this.level ==  this.maxDepth) {
   // we assign a color to the leaf
   this.color = GameColors.BLOCK_COLORS[gen.nextInt(GameColors.BLOCK_COLORS.length)];
   // the block has no children, hence, size of array is 0
   this.children = new Block[0];
  }

  // if level is less than maxDepth, we need to check whether we should subdivide it
  else {
   // we check if random number is less than Math.exp(-0.25 * level), and subdivide it
   if (gen.nextDouble() < Math.exp(-0.25 * level)) {
    this.children = new Block[4];
    for (int i = 0; i < this.children.length; i++) {
     this.children[i] = new Block(this.level + 1, maxDepth);
    }
    // no color is assigned to subdivided block, hence null
    this.color = null;
   }
   // we check if random number is more than Math.exp(-0.25 * level), and do not subdivide it
   else {
    // since we do not subdivide it, the block is a colored leaf block
    this.color = GameColors.BLOCK_COLORS[gen.nextInt(GameColors.BLOCK_COLORS.length)];
    // the block does not have more children
    this.children = new Block[0];
   }
  }
 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the
  * blocks.
  *
  *  The size is the height and width of the block. (xCoord, yCoord) are the
  *  coordinates of the top left corner of the block.
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  /*
   * ADD YOUR CODE HERE
   */
  // we use the private helper method to validate the updated size to make sure it can be subdivided
  validateSize(size, this.level);
  // we initialize new size and position for the block
  this.xCoord = xCoord;
  this.yCoord = yCoord;
  this.size = size;

  if (this.children != null && this.children.length == 4) {
   int updatedSize = size / 2;
   int updatedXCoord = xCoord + updatedSize;
   int updatedYCoord = yCoord + updatedSize;

   this.children[0].updateSizeAndPosition(updatedSize, updatedXCoord, yCoord);
   this.children[1].updateSizeAndPosition(updatedSize, xCoord, yCoord);
   this.children[2].updateSizeAndPosition(updatedSize, xCoord, updatedYCoord);
   this.children[3].updateSizeAndPosition(updatedSize, updatedXCoord, updatedYCoord);
  }
 }

 // private helper method to validate size
 private void validateSize(int size, int depthLevel) {
  // we initialize the minimum size of the block and set the size to 1
  int minBlockSize = 1;
  // we then multiply the minBlockSize by 2 at each level in order to find out the min size for the depthLevel
  for (int i = 0; i < this.maxDepth - depthLevel; i++) {
   minBlockSize *= 2;
  }

  // we then check if the size is greater than minimum size and if it can be divided down to the unit size
  if ( size <= 0 || size % minBlockSize != 0) {
   throw new IllegalArgumentException("Invalid size! Size must be positive and divisible by " + minBlockSize);
  }
 }

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  /*
   * ADD YOUR CODE HERE
   */
  // we create a new arraylist to store BlockToDraw objects
  ArrayList<BlockToDraw> blocksToDraw = new ArrayList<>();
  // we call a private recursive method to add blocks to the arraylist
  getBlocksToDrawRecursive(this, blocksToDraw);

  // we return the arraylist og BlocksToDraws
  return blocksToDraw;
 }

 // private helper method to add blocks to the arraylist
 private void getBlocksToDrawRecursive(Block block, ArrayList<BlockToDraw> blocksToDraw) {
  // we check if the block has children, to see if it is undivided
  if (block.children == null || block.children.length == 0) {
   // if the block is not subdivided, it is a leaf. In this case, we add the block itself to the list
   // block filled with its color and has no stroke
   blocksToDraw.add(new BlockToDraw(block.color, block.xCoord, block.yCoord, block.size, 0));
   // block with frame color and a stroke thickness of 3
   blocksToDraw.add(new BlockToDraw(GameColors.FRAME_COLOR, block.xCoord, block.yCoord, block.size, 3));
  }
  // if the block is subdivided and has children, we enter the else block
  else {
   // we recursively add the block's children to the list
   for (Block child : block.children) {
    getBlocksToDrawRecursive(child, blocksToDraw);
   }
  }
 }

 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  /*
   * ADD YOUR CODE HERE
   */
  // check for input validation for the level
  if (lvl < this.level || lvl > this.maxDepth) {
   throw new IllegalArgumentException("Invalid level! Level is not within the range.");
  }
  // check for input validation for the coordinates
  if (x < this.xCoord || x >= this.xCoord + this.size || y < this.yCoord || y >= this.yCoord + this.size) {
   return null;
  }
  // we return the block if it is at the correct level or undivided
  if (this.level == lvl || this.children == null || this.children.length == 0) {
   return this;
  }
   // if block is not at the correct position or not undivided, then we move to the else block
  else {
   Block closestBlock = null;
   // we use recursion to look for the corresponding child block
   for (Block childBlock : this.children) {
    Block correspondingBlock = childBlock.getSelectedBlock(x, y, lvl);
    // we look for the closestBlock and return it
    if (correspondingBlock != null && (closestBlock == null|| correspondingBlock.level > closestBlock.level)) {
     closestBlock = correspondingBlock;
    }
   }
   // we return either the closestBlock or this block
   if (closestBlock != null) {
    return closestBlock;
   }
   else {
    return this;
   }
  }
 }

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */

 // we add a new private helper method called moveUp
 private void moveUp() {
  // we subtract the block's size from its y-coordinate to move the block up
  this.yCoord -= this.size;
  // we adjust the position by updating its size and coordinates
  adjustPosition(this.size, this.xCoord, this.yCoord);
 }

 // we add a new private helper method called moveDown
 private void moveDown() {
  // we add the block's size to its y-coordinate to move the block down
  this.yCoord += this.size;
  // we adjust the position by updating its size and coordinates
  adjustPosition(this.size, this.xCoord, this.yCoord);
 }


 // we add a new private helper method called moveLeft
 private void moveLeft() {
  // we subtract the block's size from its x-coordinate to move the block left
  this.xCoord -= this.size;
  // we adjust the position by updating its size and coordinates
  adjustPosition(this.size, this.xCoord, this.yCoord);
 }

 // we add a new private helper method called moveRight
 private void moveRight() {
  // we add the block's size to its x-coordinate to move the block right
  this.xCoord += this.size;
  // we adjust the position by updating its size and coordinates
  adjustPosition(this.size, this.xCoord, this.yCoord);
 }

 // we add a new private helper method called adjustPosition
 private void adjustPosition(int blockSize, int xPosition, int yPosition) {
  // we call the method updateSizeAndPosition to adjust the size and coordinates
  this.updateSizeAndPosition(blockSize, xPosition, yPosition);
 }

 public void reflect(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  // we throw an exception if direction is neither 0 nor 1
  if (direction != 0 && direction !=1) {
   throw new IllegalArgumentException("Invalid direction! Must be 0 or 1");
  }
  // we use recursion for the reflection process
  applyReflection(this, direction);
 }

 // we add a new private helper method called applyReflection
 private void applyReflection(Block currentBlock, int direction) {
  // we check if there are children in the block
  if (currentBlock.children.length == 0) {
   // if there are no children, then there will be nothing to reflect, hence, it will return nothing
   return;
  }

  // if direction is 0, we reflect over x-axis
  if (direction == 0) {
   // we move children to the opposite side of the axis vertically
   currentBlock.children[0].moveDown();
   currentBlock.children[1].moveDown();
   currentBlock.children[2].moveUp();
   currentBlock.children[3].moveUp();

   // we swap the top and bottom children
   swapBlocks(currentBlock, 0, 3);
   swapBlocks(currentBlock, 1, 2);
  }

  // if direction is 1, we reflect over y-axis
  else {
   // we move children to the opposite side of the axis horizontally
   currentBlock.children[0].moveLeft();
   currentBlock.children[1].moveRight();
   currentBlock.children[2].moveRight();
   currentBlock.children[3].moveLeft();

   // we swap the left and right children
   swapBlocks(currentBlock, 0, 1);
   swapBlocks(currentBlock, 2, 3);
  }

   // we use recursion to reflect all the children blocks
  for (Block childBlock : currentBlock.children) {
   applyReflection(childBlock, direction);
  }
 }

 // we add a new private helper method called swapBlocks
 private void swapBlocks(Block parentBlock, int indexA, int indexB) {
  // we swap two children blocks of a parentBlock using the input indices
  Block temp = parentBlock.children[indexA];
  parentBlock.children[indexA] = parentBlock.children[indexB];
  parentBlock.children[indexB] = temp;
 }
 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  /*
   * ADD YOUR CODE HERE
   */
  // we check for the input validation for the direction
  if (direction != 0 && direction != 1) {
   throw new IllegalArgumentException("Invalid Direction! Input must be a 0 or a 1");
  }

  // we check if this block has children
  if (this.children == null || this.children.length == 0) {
   // if the block has no children, then we do nothing
   return;
  }

  // we check what the rotation direction is
  // if direction is 1, we rotate in the clockwise direction
  if (direction == 1) {
   rotateClockwise();
  }
  // if direction is 0, we rotate in the counter-clockwise direction
  else {
   rotateCounterClockwise();
  }

  // we use recursion to rotate the direction of all children
  for (Block childBlock : this.children) {
   childBlock.rotate(direction);
  }
 }

 // we add a new private helper method called rotateClockwise
 private void rotateClockwise() {
  // we adjust the orientation of each child block in a clockwise rotation
  this.children[0].moveDown();
  this.children[1].moveRight();
  this.children[2].moveUp();
  this.children[3].moveLeft();

  // we create a temporary block and swap the children block to their new positions after rotation
  Block tmp = this.children[0];
  this.children[0] = this.children[1];
  this.children[1] = this.children[2];
  this.children[2] = this.children[3];
  this.children[3] = tmp;
 }

 // we add a new private helper method called rotateCounterClockwise
 private void rotateCounterClockwise() {
  // we adjust the orientation of each child block in a counter-clockwise rotation
  this.children[0].moveLeft();
  this.children[1].moveDown();
  this.children[2].moveRight();
  this.children[3].moveUp();

  // we create a temporary block and swap the children block to their new positions after rotation
  Block tmp = this.children[3];
  this.children[3] = this.children[2];
  this.children[2] = this.children[1];
  this.children[1] = this.children[0];
  this.children[0] = tmp;
 }

 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  /*
   * ADD YOUR CODE HERE
   */
  // we check if the block is a top-level block or if it is at a level greater than or equal to the maximum depth
  if (this.level == 0 || this.level >= this.maxDepth) {
   // we return false if it satisfies the above conditions
   return false;
  }

  // we initialize a new array that stores the four new sub-blocks we generate after smashing a block
  this.children = new Block[4];

  // we generate the sub-blocks and assign the new children blocks to the array
  for (int i = 0; i < this.children.length; i++) {
   this.children[i] = createNewChild(i);
  }

  // after smashing the blocks, we return true
  return true;
 }

 // we add a new private helper method called createNewChild
 private Block createNewChild(int position) {
  // the newSize of the child block will be half the size of the initial block
  int newSize = this.size / 2;

  // we adjust the newXCoord for the child block for UR and LR
  int newXCoord = this.xCoord + (position % 2) * newSize;
  // we adjust the newYCoord for the child block for LL and LR
  int newYCoord = this.yCoord + (position / 2) * newSize;
  // we randomly assign colors to each of the new children blocks
  Color newColor = GameColors.BLOCK_COLORS[gen.nextInt(GameColors.BLOCK_COLORS.length)];

  // we return a new block with all the new properties
  return new Block(newXCoord, newYCoord, newSize, this.level + 1, this.maxDepth, newColor, new Block[0]);
 }





 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  * 
  * Return and array arr where, arr[i] represents the unit cells in row i, 
  * arr[i][j] is the color of unit cell in row i and column j.
  * 
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  /*
   * ADD YOUR CODE HERE
   */
  // we calculate the array size based on the depth of the node in the tree
  int arraySize = (int) Math.pow(2, this.maxDepth - this.level);
  // we initialize a new 2D color array which holds the flattened colors
  Color[][] colors = new Color[arraySize][arraySize];

  // we check if the node has children
  if (this.children.length != 0) {
   // if the node had children, we use recursion to flatten each child
   Color[][] upperLeft = this.children[0].flatten();
   Color[][] upperRight = this.children[1].flatten();
   Color[][] lowerLeft = this.children[2].flatten();
   Color[][] lowerRight = this.children[3].flatten();

   // we calculate the size of a side of the quadrant
   int halfSize = upperRight.length;

   // we copy the colors of the upperRight and upperLeft quadrants to the top right and left of the colors array respectively
   for (int i = 0; i < halfSize; i++) {
    System.arraycopy(upperRight[i], 0, colors[i], 0, halfSize);
    System.arraycopy(upperLeft[i], 0, colors[i], halfSize, halfSize);
   }

   // we copy the colors of the lowerLeft and lowerRight quadrants to the bottom left and right of the colors array respectively
   for (int i = 0; i < halfSize; i++) {
    System.arraycopy(lowerLeft[i], 0, colors[halfSize + i], 0, halfSize);
    System.arraycopy(lowerRight[i], 0, colors[halfSize + i], halfSize, halfSize);
   }
  }

  // if the node does not have children, we enter the else block
  else {
   // we fill the colors array with the node's color
   // we iterate over each cell in the array and set it to the node's color
   for (int i = 0; i < arraySize; i++) {
    for (int j = 0; j < arraySize; j++) {
     colors[i][j] = this.color;
    }
   }
  }

  // we return the colors array
  return colors;
 }


 
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
