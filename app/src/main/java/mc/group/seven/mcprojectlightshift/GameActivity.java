package mc.group.seven.mcprojectlightshift;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    public GridView gv;
    public View view;
    public String[] gameBoard = new String[84];
    public String[] gameKey = new String[84];
    int currentLevel, currentIndex, currentMoves, exitIndex;
    boolean exitState = false, firstTutorial = false;

    // Activity view objects
    TextView tv_moves, tv_levelHeader, tv_feedback;
    Button btn_reset, btn_fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // get window view
        view = this.getWindow().getDecorView().findViewById(android.R.id.content);

        //initialize other activity components
        tv_moves = (TextView) findViewById(R.id.tv_moves);
        tv_levelHeader = (TextView) findViewById(R.id.tv_levelheader);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_fb = (Button) findViewById(R.id.btn_fb);

        // Set current level and populate game
        currentLevel = 0;
        gv = (GridView) this.findViewById(R.id.mygrid);

        //initialize both the user's board and the game key
        for (int i = 0; i < 84; i++) {
            gameBoard[i] = "     ";
            gameKey[i] = "     ";
        }
        currentMoves = 0;
        populateLevel(currentLevel);

        // Grid adapter setup
        CustomGridAdapter gridAdapter = new CustomGridAdapter(GameActivity.this, gameBoard);
        gv.setAdapter(gridAdapter);

        //begin listening
        setupListeners();

        /** TUTORIAL LEVEL **/
        if (currentLevel == 0) {
            tutorialLevel();
        }

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
            case 0:
                firstTutorial = true;
                tv_levelHeader.setText("Level 0 - Learning to Shift");

                gameBoard[23] = "[ X ]";
                gameBoard[30] = "[ B ]";
                gameBoard[31] = "[ Y ]";
                gameBoard[32] = "[ Y ]";
                gameBoard[37] = "[ G ]";
                gameBoard[39] = "[ Y ]";
                gameBoard[44] = "[ Y ]";
                gameBoard[45] = "[ Y ]";
                gameBoard[46] = "[ Y ]";
                gameBoard[51] = "[ o ]";

                gameKey[23] = "[ X ]";
                gameKey[30] = "[ B ]";
                gameKey[31] = "[ Y ]";
                gameKey[32] = "[ Y ]";
                gameKey[37] = "[ G ]";
                gameKey[39] = "[ Y ]";
                gameKey[44] = "[ Y ]";
                gameKey[45] = "[ Y ]";
                gameKey[46] = "[ Y ]";
                gameKey[51] = "[ B ]";

                currentIndex = 51;
                exitIndex = 23;
                break;

            case 1:
                tv_levelHeader.setText("Level 1 - Getting Started");

                gameBoard[24] = "[ Y ]";
                gameBoard[31] = "[ Y ]";
                gameBoard[38] = "[ B ]";
                gameBoard[39] = "[ Y ]";
                gameBoard[45] = "[ o ]";
                gameBoard[52] = "[ X ]";

                gameKey[24] = "[ Y ]";
                gameKey[31] = "[ Y ]";
                gameKey[38] = "[ B ]";
                gameKey[39] = "[ Y ]";
                gameKey[45] = "[ Y ]";
                gameKey[52] = "[ X ]";

                currentIndex = 45;
                exitIndex = 52;
                break;
        }
    }

    /**
     * Resets the state of the game
     */
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

        int yellowCount = 0;

        for (int i = 0; i < 84; i++) {
            if (gameKey[i].equals("[ Y ]")) {
                yellowCount++;
            }
        }

        if (yellowCount!=0) {
            tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
        }
        else {
            tv_feedback.setText("Exit open!");
        }

        gv.invalidateViews();
    }

    /**
     * Sets up the listeners for player movement.
     */
    private void setupListeners() {
        // Swipe listeners and movement game logic
        gv.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                // if its a valid tile to move to...
                if (!gameKey[currentIndex - 1].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveLeft();
                }
            }
            @Override
            public void onSwipeRight() {
                if (!gameKey[currentIndex + 1].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveRight();
                }
            }
            @Override
            public void onSwipeUp() {
                if (!gameKey[currentIndex - 7].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveUp();
                }
            }
            @Override
            public void onSwipeDown() {
                if (!gameKey[currentIndex + 7].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveDown();
                }
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                // if its a valid tile to move to...
                if (!gameKey[currentIndex - 1].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveLeft();
                }
            }
            @Override
            public void onSwipeRight() {
                if (!gameKey[currentIndex + 1].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveRight();
                }
            }
            @Override
            public void onSwipeUp() {
                if (!gameKey[currentIndex - 7].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveUp();
                }
            }
            @Override
            public void onSwipeDown() {
                if (!gameKey[currentIndex + 7].equals("     ") && !gameKey[currentIndex - 1].equals("[ X ]")) {
                    moveDown();
                }
            }
        });
    }

    /**
     * Updates game board
     */
    public void updateView() {
        gv.invalidateViews();
    }

    /**
     * Removes the listeners to lock game state
     */
    public void removeListeners() {
        view.setOnTouchListener(null);
        gv.setOnTouchListener(null);
    }

    /**
     * Code for the first level - special due to tutorial
     */
    public void tutorialLevel() {

        removeListeners();

        tv_feedback.setText("Welcome to LightShift! Please hit the 'OK' button to the " +
                "right to continue...");

        btn_fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv_feedback.setText("The goal in LightShift is to turn all tiles " +
                        "blue and reach the exit... (Hit OK to continue)");

                btn_fb.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        tv_feedback.setText("You can shift tiles between blue and yellow " +
                                "by moving onto them, and you can move by swiping in the desired " +
                                "direction. Give it a try now.");

                        setupListeners();
                        btn_fb.setVisibility(View.INVISIBLE);
                        btn_fb.setOnClickListener(null);
                    }
                });
            }
        });
    }

    /**
     * moves the player up a tile
     */
    public void moveUp() {
        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex - 7].equals("[ E ]")) {
            if (currentLevel == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("Congratulations on finishing the tutorial level." +
                        " Click OK to continue your LightShift journey.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("STATS HERE");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
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

        if (firstTutorial) {
            tv_feedback.setText("Great job! One last thing...if you ever make a mistake" +
                    " and want to start over, hit the reset button in the top right. Good" +
                    " luck and have fun! (Hit OK to continue)");

            removeListeners();
            btn_fb.setVisibility(View.VISIBLE);

            btn_fb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tv_feedback.setText("");

                    setupListeners();
                    btn_fb.setVisibility(View.INVISIBLE);
                    btn_fb.setOnClickListener(null);
                    firstTutorial = false;
                }
            });
        }

        else {
            int yellowCount = 0;

            for (int i = 0; i < 84; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (yellowCount!=0) {
                tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
            }
            else {
                tv_feedback.setText("Exit open!");
            }
        }

        updateView();
    }

    /**
     * moves the player down a tile
     */
    public void moveDown() {
        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex + 7].equals("[ E ]")) {
            if (currentLevel == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("Congratulations on finishing the tutorial level." +
                        " Click OK to continue your LightShift journey.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("STATS HERE");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
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

        if (!firstTutorial) {
            int yellowCount = 0;

            for (int i = 0; i < 84; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (yellowCount!=0) {
                tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
            }
            else {
                tv_feedback.setText("Exit open!");
            }
        }

        updateView();
    }

    /**
     * moves the player left a tile
     */
    public void moveLeft() {
        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex - 1].equals("[ E ]")) {
            if (currentLevel == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("Congratulations on finishing the tutorial level." +
                        " Click OK to continue your LightShift journey.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("STATS HERE");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
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

        if (!firstTutorial) {
            int yellowCount = 0;

            for (int i = 0; i < 84; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (yellowCount != 0) {
                tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
            } else {
                tv_feedback.setText("Exit open!");
            }
        }

        updateView();
    }

    /**
     * moves a player right a tile
     */
    public void moveRight() {
        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex + 1].equals("[ E ]")) {
            if (currentLevel == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("Congratulations on finishing the tutorial level." +
                        " Click OK to continue your LightShift journey.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("STATS HERE");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                currentLevel++;
                                resetGame();
                                populateLevel(currentLevel);
                                resetGame();
                            }
                        });
                alertDialog.show();
            }
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

        if (!firstTutorial) {
            int yellowCount = 0;

            for (int i = 0; i < 84; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (yellowCount != 0) {
                tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
            } else {
                tv_feedback.setText("Exit open!");
            }
        }

        updateView();
    }
}
