package mc.group.seven.mcprojectlightshift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    //test commit and push

    public GridView gv;
    public String[] gameBoard = new String[84];
    public String[] gameKey = new String[84];
    int currentLevel, currentIndex, currentMoves, exitIndex;
    boolean exitState = false;

    // Activity view objects
    TextView tv_moves;
    Button btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // get window view
        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);

        // Set current level and populate game
        currentLevel = 1;
        gv = (GridView) this.findViewById(R.id.mygrid);

        //initialize both the user's board and the game key
        for (int i = 0; i < 84; i++) {
            gameBoard[i] = "     ";
            gameKey[i] = "     ";
        }
        currentMoves = 0;
        populateLevel(currentLevel);

        //initialize other activity components
        tv_moves = (TextView) findViewById(R.id.tv_moves);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        // Grid adapter shenanigans
        CustomGridAdapter gridAdapter = new CustomGridAdapter(GameActivity.this, gameBoard);
        gv.setAdapter(gridAdapter);

        // Swipe listeners and movement game logic
        gv.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                // if its a valid tile to move to...
                if (!gameKey[currentIndex - 1].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex - 1].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }

                    // if its not, move the player piece
                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex - 1] = "[ o ]";

                    // swap tile state if its yellow/blue
                    if (gameKey[currentIndex - 1].equals("[ Y ]")) {
                        gameKey[currentIndex - 1] = "[ B ]";
                    }
                    else if (gameKey[currentIndex - 1].equals("[ B ]")) {
                        gameKey[currentIndex - 1] = "[ Y ]";
                    }

                    // check if exit tile should be opened
                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    // if it is, open it
                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    // update moves and track the current player piece index on the grid
                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex - 1;
                }

                gv.invalidateViews();
            }
            @Override
            public void onSwipeRight() {
                if (!gameKey[currentIndex + 1].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex + 1].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }
                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex + 1] = "[ o ]";

                    if (gameKey[currentIndex + 1].equals("[ Y ]")) {
                        gameKey[currentIndex + 1] = "[ B ]";
                    }
                    else if (gameKey[currentIndex + 1].equals("[ B ]")) {
                        gameKey[currentIndex + 1] = "[ Y ]";
                    }

                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex + 1;
                }

                gv.invalidateViews();
            }
            @Override
            public void onSwipeUp() {
                if (!gameKey[currentIndex - 7].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex - 7].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }

                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex - 7] = "[ o ]";

                    if (gameKey[currentIndex - 7].equals("[ Y ]")) {
                        gameKey[currentIndex - 7] = "[ B ]";
                    }
                    else if (gameKey[currentIndex - 7].equals("[ B ]")) {
                        gameKey[currentIndex - 7] = "[ Y ]";
                    }

                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex - 7;
                }

                gv.invalidateViews();
            }
            @Override
            public void onSwipeDown() {
                if (!gameKey[currentIndex + 7].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex + 7].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }

                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex + 7] = "[ o ]";

                    if (gameKey[currentIndex + 7].equals("[ Y ]")) {
                        gameKey[currentIndex + 7] = "[ B ]";
                    }
                    else if (gameKey[currentIndex + 7].equals("[ B ]")) {
                        gameKey[currentIndex + 7] = "[ Y ]";
                    }

                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex + 7;
                }

                gv.invalidateViews();
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                // if its a valid tile to move to...
                if (!gameKey[currentIndex - 1].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex - 1].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }

                    // if its not, move the player piece
                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex - 1] = "[ o ]";

                    // swap tile state if its yellow/blue
                    if (gameKey[currentIndex - 1].equals("[ Y ]")) {
                        gameKey[currentIndex - 1] = "[ B ]";
                    }
                    else if (gameKey[currentIndex - 1].equals("[ B ]")) {
                        gameKey[currentIndex - 1] = "[ Y ]";
                    }

                    // check if exit tile should be opened
                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    // if it is, open it
                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    // update moves and track the current player piece index on the grid
                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex - 1;
                }

                gv.invalidateViews();
            }
            @Override
            public void onSwipeRight() {
                if (!gameKey[currentIndex + 1].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex + 1].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }
                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex + 1] = "[ o ]";

                    if (gameKey[currentIndex + 1].equals("[ Y ]")) {
                        gameKey[currentIndex + 1] = "[ B ]";
                    }
                    else if (gameKey[currentIndex + 1].equals("[ B ]")) {
                        gameKey[currentIndex + 1] = "[ Y ]";
                    }

                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex + 1;
                }

                gv.invalidateViews();
            }
            @Override
            public void onSwipeUp() {
                if (!gameKey[currentIndex - 7].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex - 7].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }

                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex - 7] = "[ o ]";

                    if (gameKey[currentIndex - 7].equals("[ Y ]")) {
                        gameKey[currentIndex - 7] = "[ B ]";
                    }
                    else if (gameKey[currentIndex - 7].equals("[ B ]")) {
                        gameKey[currentIndex - 7] = "[ Y ]";
                    }

                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex - 7;
                }

                gv.invalidateViews();
            }
            @Override
            public void onSwipeDown() {
                if (!gameKey[currentIndex + 7].equals("     ")) {
                    // if its an open exit, declare level won + logic
                    if (gameKey[currentIndex + 7].equals("[ E ]")) {
                        Toast.makeText(getApplicationContext(), "You won! Resetting.", Toast.LENGTH_LONG).show();
                        resetGame();
                        return;
                    }

                    gameBoard[currentIndex] = gameKey[currentIndex];
                    gameBoard[currentIndex + 7] = "[ o ]";

                    if (gameKey[currentIndex + 7].equals("[ Y ]")) {
                        gameKey[currentIndex + 7] = "[ B ]";
                    }
                    else if (gameKey[currentIndex + 7].equals("[ B ]")) {
                        gameKey[currentIndex + 7] = "[ Y ]";
                    }

                    exitState = true;
                    for (int i = 0; i < 84; i++) {
                        if (gameKey[i].equals("[ Y ]")) {
                            exitState = false;
                        }
                    }

                    if (exitState) {
                        gameBoard[exitIndex] = "[ E ]";
                        gameKey[exitIndex] = "[ E ]";
                    }
                    else {
                        gameBoard[exitIndex] = "[ X ]";
                        gameKey[exitIndex] = "[ X ]";
                    }

                    currentMoves++;
                    tv_moves.setText("Moves: " + currentMoves);
                    currentIndex = currentIndex + 7;
                }

                gv.invalidateViews();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetGame();
                Toast.makeText(getApplicationContext(), "Resetting...", Toast.LENGTH_LONG).show();
            }
        });
    }



    /**
     * Populates the level depending on user progress
     * @param currentLevel
     */
    private void populateLevel(int currentLevel) {
        switch (currentLevel) {
            case 1:
                gameBoard[23] = "[ X ]";
                gameBoard[30] = "[ B ]";
                gameBoard[31] = "[ Y ]";
                gameBoard[32] = "[ Y ]";
                gameBoard[37] = "[ G ]";
                gameBoard[39] = "[ Y ]";
                gameBoard[44] = "[ Y ]";
                gameBoard[45] = "[ Y ]";
                gameBoard[46] = "[ Y ]";
                gameBoard[51] = "[ Y ]";
                gameBoard[58] = "[ o ]";

                gameKey[23] = "[ X ]";
                gameKey[30] = "[ B ]";
                gameKey[31] = "[ Y ]";
                gameKey[32] = "[ Y ]";
                gameKey[37] = "[ G ]";
                gameKey[39] = "[ Y ]";
                gameKey[44] = "[ Y ]";
                gameKey[45] = "[ Y ]";
                gameKey[46] = "[ Y ]";
                gameKey[51] = "[ Y ]";
                gameKey[58] = "[ S ]";

                currentIndex = 58;
                exitIndex = 23;
        }
    }

    private void resetGame() {
        //initialize both the user's board and the game key
        for (int i = 0; i < 84; i++) {
            gameBoard[i] = "     ";
            gameKey[i] = "     ";
        }
        currentMoves = 0;
        populateLevel(currentLevel);

        //initialize other activity components
        tv_moves.setText("Moves: " + currentMoves);

        gv.invalidateViews();
    }
}
