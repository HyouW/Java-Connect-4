import java.util.Random;

public class MyAgent extends Agent
{
    Random r;
    final int COLUMNS = myGame.getColumnCount();// # of columns in game board
    final int ROWS = myGame.getRowCount();// # of rows in game board

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        if (!isFirstMove())
        {
            if (iCanWin() > -1) { //if i can win, i should
                moveOnColumn(iCanWin());
            }
            else if (theyCanWin() > -1) { //if i can prevent them from winning, i should
                moveOnColumn(theyCanWin());     
            }
            else { //otherwise make a move
                //some decision logic
                moveOnColumn(randomMove()); //random for now
            }
        }
        else moveOnColumn(COLUMNS/2); //the best strategy is to always start by playing in the middle column
                                       //integer division will always put me in the exact middle if COLUMNS is odd, or the first of the 2 middle columns if COLUMNS is even
    }
    
    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }
    
    /**
     * Checks if this is MyAgent's first move of the game
     * 
     * @return true if my agent has played no moves, false otherwise
     */
    public boolean isFirstMove()
    {
        int filledSlots = 0;
        int i = 0;
        while (filledSlots <= 1 && i < COLUMNS)//board is not filled
        {
            if (getLowestEmptyIndex(myGame.getColumn(i)) < ROWS - 1)//bottom slot is filled
            {
                filledSlots++;
            }
            i++;
        }
        if (filledSlots <= 1)//even if the agent goes second
        {
            return true;
        }
        return false;
    }
    
    /**
     * Checks the color of a piece
     * 
     * @param slot the slot to check for a piece and its color
     * @param color the color to check for; true for red, false for yellow
     */
    public boolean colorCheck(Connect4Slot slot, boolean color)
    {
        if (slot.getIsFilled())
        {
            if (slot.getIsRed() == color)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks to see if three pieces are matching vertically
     * 
     * @param slot1 the first slot
     * @param slot2 the second slot
     * @param slot3 the third slot
     * @return true if 
     */
    public boolean match(Connect4Slot slot1, Connect4Slot slot2, Connect4Slot slot3, boolean color)
    {
        boolean slot1ColorCheck = colorCheck(slot1, color);
        boolean slot2ColorCheck = colorCheck(slot2, color);
        boolean slot3ColorCheck = colorCheck(slot3, color);
        return slot1ColorCheck && slot2ColorCheck && slot3ColorCheck;
    }
    
    public int rowCheck(int row)
    {
        if (row > 2){
            return 1;}
        else if (row > 1 && row + 1 < ROWS){
            return 2;}
        else if (row > 0 && row + 2 < ROWS){
            return 3;}
        else if (row + 3 < ROWS){
            return 4;}
        return -1;
    }
    
    
    public int verticalCheck(boolean color)
    {
        for (int j = 0; j < COLUMNS; j++)
        {
            int lowestEmptySlot = getLowestEmptyIndex(myGame.getColumn(j));
                if (lowestEmptySlot > -1)
                {
                    if (lowestEmptySlot < ROWS - 3)
                    {
                        Connect4Column column = myGame.getColumn(j);
                        if (match(
                        column.getSlot(lowestEmptySlot + 1), 
                        column.getSlot(lowestEmptySlot + 2), 
                        column.getSlot(lowestEmptySlot + 3),
                        color
                        ))
                        {
                            return j;
                        }
                    }
                }
        }
        return -1;
    }
    
    public int horizontalCheck(boolean color)
    {
        for (int j = 0; j < COLUMNS; j++)
        {
            int lowestEmptySlot = getLowestEmptyIndex(myGame.getColumn(j));
                if (lowestEmptySlot > -1)
                {
                    if (j + 3 < COLUMNS){//You're in the 1st 4 columns
                        if (match(
                            myGame.getColumn(j+1).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j+2).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j+3).getSlot(lowestEmptySlot),
                            color)){
                            return j;
                        }
                    }
                    if (j > 0 && j + 2 < COLUMNS){//You're at least in the 2nd column and at most in the 3rd last column
                        if (match(
                            myGame.getColumn(j-1).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j+1).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j+2).getSlot(lowestEmptySlot),
                            color)){
                            return j;
                        }
                    }
                    if (j > 1 && j + 1 < COLUMNS){//You're at least in the 3rd column and at most in the 2nd last column
                        if (match(
                            myGame.getColumn(j-2).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j-1).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j+1).getSlot(lowestEmptySlot),
                            color)){
                            return j;
                        }
                    }
                    if (j > 2){//You're at least in the 4th column
                        if (match(
                            myGame.getColumn(j-3).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j-2).getSlot(lowestEmptySlot), 
                            myGame.getColumn(j-1).getSlot(lowestEmptySlot),
                            color)){
                            return j;
                        }
                    }
                }
        }
        return -1;
    }
    
    public int posSlopeCheck(boolean color)
    {
        for (int j = 0; j < COLUMNS; j++)
        {
            int lowestEmptySlot = getLowestEmptyIndex(myGame.getColumn(j));
            {
                if (lowestEmptySlot > -1){
                    int rowPos = rowCheck(lowestEmptySlot);
                    if (rowPos == 1){
                        if (j + 3 < COLUMNS){//You're at least in the 1st 4 columns
                            if (match(
                            myGame.getColumn(j+1).getSlot(lowestEmptySlot-1), 
                            myGame.getColumn(j+2).getSlot(lowestEmptySlot-2), 
                            myGame.getColumn(j+3).getSlot(lowestEmptySlot-3),
                            color)){
                            return j;
                            }
                        }
                    }
                    else if (rowPos == 2){
                        if (j > 0 && j + 2 < COLUMNS){//You're at least in the 2nd column and at most in the 3rd last column
                            if (match(
                                myGame.getColumn(j-1).getSlot(lowestEmptySlot+1), 
                                myGame.getColumn(j+1).getSlot(lowestEmptySlot-1), 
                                myGame.getColumn(j+2).getSlot(lowestEmptySlot-2),
                                color)){
                                return j;
                            }
                        }
                    }
                    else if (rowPos == 3){
                        if (j > 1 && j + 1 < COLUMNS){//You're at least in the 3rd column and at most in the 2nd last column
                            if (match(
                                myGame.getColumn(j-2).getSlot(lowestEmptySlot+2), 
                                myGame.getColumn(j-1).getSlot(lowestEmptySlot+1), 
                                myGame.getColumn(j+1).getSlot(lowestEmptySlot-1),
                                color)){
                                return j;
                            }
                        }
                    }
                    else if (rowPos == 4){
                        if (j > 2){//You're at least in the 4th column
                            if (match(
                                myGame.getColumn(j-3).getSlot(lowestEmptySlot+3), 
                                myGame.getColumn(j-2).getSlot(lowestEmptySlot+2), 
                                myGame.getColumn(j-1).getSlot(lowestEmptySlot+1),
                                color)){
                                return j;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public int negSlopeCheck(boolean color)
    {
        for (int j = 0; j < COLUMNS; j++)
        {
            int lowestEmptySlot = getLowestEmptyIndex(myGame.getColumn(j));
            {
                if (lowestEmptySlot > -1){
                    int rowPos = rowCheck(lowestEmptySlot);
                    if (rowPos == 1){
                        if (j > 2){//You're at least in the 4th column
                            if (match(
                                myGame.getColumn(j-1).getSlot(lowestEmptySlot-1), 
                                myGame.getColumn(j-2).getSlot(lowestEmptySlot-2), 
                                myGame.getColumn(j-3).getSlot(lowestEmptySlot-3),
                                color)){
                                return j;
                            }
                        }
                    }
                    else if (rowPos == 2){
                        if (j > 1 && j + 1 < COLUMNS){//You're at least in the 3rd column and at most in the 2nd last column
                            if (match(
                                myGame.getColumn(j+1).getSlot(lowestEmptySlot+1), 
                                myGame.getColumn(j-1).getSlot(lowestEmptySlot-1), 
                                myGame.getColumn(j-2).getSlot(lowestEmptySlot-2),
                                color)){
                                return j;
                            }
                        }
                    }
                    else if (rowPos == 3){
                        if (j > 0 && j + 2 < COLUMNS){//You're at least in the 2nd column and at most in the 3rd last column
                            if (match(
                                myGame.getColumn(j+2).getSlot(lowestEmptySlot+2), 
                                myGame.getColumn(j+1).getSlot(lowestEmptySlot+1), 
                                myGame.getColumn(j-1).getSlot(lowestEmptySlot-1),
                                color)){
                                return j;
                            }
                        }
                    }
                    else if (rowPos == 4){
                        if (j + 3 < COLUMNS){//You're at least in the 1st 4 columns
                            if (match(
                                myGame.getColumn(j+3).getSlot(lowestEmptySlot+3), 
                                myGame.getColumn(j+2).getSlot(lowestEmptySlot+2), 
                                myGame.getColumn(j+1).getSlot(lowestEmptySlot+1),
                                color)){
                                return j;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    
    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin()
    {
        if (verticalCheck(iAmRed) > -1)
            return verticalCheck(iAmRed);
        else if (horizontalCheck(iAmRed) > -1)
            return horizontalCheck(iAmRed);
        else if (posSlopeCheck(iAmRed) > -1)
            return posSlopeCheck(iAmRed);
        else if (negSlopeCheck(iAmRed) > -1)
            return negSlopeCheck(iAmRed);   
  
        return -1;
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin()
    {
        if (verticalCheck(!iAmRed) > -1)
            return verticalCheck(!iAmRed);
        else if (horizontalCheck(!iAmRed) > -1)
            return horizontalCheck(!iAmRed);
        else if (posSlopeCheck(!iAmRed) > -1)
            return posSlopeCheck(!iAmRed);
        else if (negSlopeCheck(!iAmRed) > -1)
            return negSlopeCheck(!iAmRed);
        return -1;
    }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
}
