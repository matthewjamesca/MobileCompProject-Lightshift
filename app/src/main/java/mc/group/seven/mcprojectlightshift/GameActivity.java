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
    int currentLevelId, currentIndex, currentMoves, exitIndex;
    boolean exitState = false, firstTutorial = false;
    LevelList levelList;

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

        //load levels and determine current level
        levelList = new LevelList();
        currentLevelId = 0;
        gv = (GridView) this.findViewById(R.id.mygrid);

        //initialize both the user's board and the game key
        for (int i = 0; i < 84; i++) {
            gameBoard[i] = "     ";
            gameKey[i] = "     ";
        }
        currentMoves = 0;
        populateLevel(currentLevelId);

        // Grid adapter setup
        CustomGridAdapter gridAdapter = new CustomGridAdapter(GameActivity.this, gameBoard);
        gv.setAdapter(gridAdapter);

        //begin listening
        setupListeners();

        /** TUTORIAL LEVEL **/
        if (currentLevelId == 0) {
            tutorialLevel();
        }
    }

    /**
     * Populates the level depending on user progress
     * @param currentLevelId
     */
    private void populateLevel(int currentLevelId) {
        if (currentLevelId == 0) {
            firstTutorial = true;
        }

        int levelIndex = 0;

        for (int i = 0; i < levelList.getLevels().size(); i++) {
            if (levelList.getLevels().get(i).getId() == currentLevelId) {
                levelIndex = i;
            }
        }

        tv_levelHeader.setText(levelList.getLevels().get(levelIndex).getName());

        String levelKey = levelList.getLevels().get(levelIndex).getKey();

        String[] levelComponents = levelKey.split("\\s+");

        int tileIndex = -1;
        String tileType = "";

        for (int i = 0; i < levelComponents.length; i+=2) {
            if (!levelComponents[i+1].equals("s")) {
                if (levelComponents[i+1].equals("x")) {
                    exitIndex = Integer.parseInt(levelComponents[i]);
                }
                tileIndex = Integer.parseInt(levelComponents[i]);
                tileType = levelComponents[i + 1];

                gameBoard[tileIndex] = "[ " + tileType.toUpperCase() + " ]";
                gameKey[tileIndex] = "[ " + tileType.toUpperCase() + " ]";
            }

            else {
                tileIndex = Integer.parseInt(levelComponents[i]);
                gameBoard[tileIndex] = "[ o ]";
                currentIndex = tileIndex;
            }
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
        populateLevel(currentLevelId);

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
        //button listener
        btn_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetGame();
                Toast.makeText(getApplicationContext(), "Resetting...", Toast.LENGTH_LONG).show();
            }
        });

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
        btn_reset.setOnClickListener(null);
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

        //teleporter logic variables
        boolean teleporterFlag = false;
        int teleporterIndex = -1;

        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex - 7].equals("[ E ]")) {
            finishLevel();
        }
        // teleporter tile logic
        else if (gameKey[currentIndex - 7].equals("[ T ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ T ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex - 7].equals("[ U ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ U ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex - 7].equals("[ V ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ V ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        else {
            gameBoard[currentIndex - 7] = "[ o ]";
        }

        gameBoard[currentIndex] = gameKey[currentIndex];

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

        if (!teleporterFlag) {
            currentIndex = currentIndex - 7;
        }
        else {
            currentIndex = teleporterIndex;
        }

        if (firstTutorial) {
            tv_feedback.setText("Great job! Two final points before you begin..." +
                    "grey tiles do not change when moved on, and you can reset your game at" +
                    " any time by hitting the Reset button! Good luck!");

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

        //teleporter logic variables
        boolean teleporterFlag = false;
        int teleporterIndex = -1;

        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex + 7].equals("[ E ]")) {
            finishLevel();
        }
        // teleporter tile logic
        else if (gameKey[currentIndex + 7].equals("[ T ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ T ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex + 7].equals("[ U ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ U ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex + 7].equals("[ V ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ V ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        else {
            gameBoard[currentIndex + 7] = "[ o ]";
        }

        gameBoard[currentIndex] = gameKey[currentIndex];

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

        if(!teleporterFlag) {
            currentIndex = currentIndex + 7;
        }
        else {
            currentIndex = teleporterIndex;
        }

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
        //teleporter logic variables
        boolean teleporterFlag = false;
        int teleporterIndex = -1;
        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex - 1].equals("[ E ]")) {
            finishLevel();
        }
        // teleporter tile logic
        else if (gameKey[currentIndex - 1].equals("[ T ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ T ]") && i != (currentIndex + 1)) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
            gameBoard[currentIndex] = gameKey[currentIndex];
        }
        // teleporter cont
        else if (gameKey[currentIndex - 1].equals("[ U ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ U ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
            gameBoard[currentIndex] = gameKey[currentIndex];
        }
        // teleporter cont
        else if (gameKey[currentIndex - 1].equals("[ V ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ V ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
            gameBoard[currentIndex] = gameKey[currentIndex];
        }
        else {
            gameBoard[currentIndex - 1] = "[ o ]";
            gameBoard[currentIndex] = gameKey[currentIndex];
        }

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

        if (!teleporterFlag) {
            currentIndex = currentIndex - 1;
        }
        else {
            currentIndex = teleporterIndex;
        }

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
        boolean teleporterFlag = false;
        int teleporterIndex = -1;

        // if its an open exit, declare level won + logic
        if (gameKey[currentIndex + 1].equals("[ E ]")) {
            finishLevel();
        }
        // teleporter tile logic
        else if (gameKey[currentIndex + 1].equals("[ T ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ T ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex + 1].equals("[ U ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ U ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex + 1].equals("[ V ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ V ]") && i != currentIndex + 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        else {
            gameBoard[currentIndex + 1] = "[ o ]";
        }

        gameBoard[currentIndex] = gameKey[currentIndex];

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

        // if not a teleporter, just change the index by 1
        if (!teleporterFlag) {
            currentIndex = currentIndex + 1;
        }
        //if a teleporter was used, use the index of its tile as the new current location
        else {
            currentIndex = teleporterIndex;
        }

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

    public void finishLevel() {
        if (currentLevelId == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
            alertDialog.setTitle("Level Complete!");
            alertDialog.setMessage("Congratulations on finishing the tutorial level." +
                    " Click OK to continue your LightShift journey.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            currentLevelId++;
                            resetGame();
                            populateLevel(currentLevelId);
                            resetGame();
                        }
                    });
            alertDialog.show();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
            alertDialog.setTitle("Level Complete!");
            //TODO
            //IMPLEMENT BEST MOVES
            alertDialog.setMessage("Moves Taken: " + currentMoves + " moves.\n" +
                    "Moves Record: " + currentMoves + " moves.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            currentLevelId++;
                            resetGame();
                            populateLevel(currentLevelId);
                            resetGame();
                        }
                    });
            alertDialog.show();
        }
    }
}
