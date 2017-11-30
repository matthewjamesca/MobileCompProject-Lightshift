package mc.group.seven.mcprojectlightshift;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    /** GAME OBJECTS **/
    public GridView gv;
    public View view;
    public String[] gameBoard = new String[63];
    public String[] gameKey = new String[63];
    int currentLevelId, currentIndex, currentMoves, exitIndex, remainingMoves;
    boolean exitState = false, firstTutorial = false, otherTutorial = false;
    LevelList levelList;
    public CustomImageAdapter imageAdapter;

    /** SAVED PROGRESS OBJECTS **/
    public boolean isCampaign;

    /** ACTIVITY VIEW OBJECTS **/
    TextView tv_moves, tv_levelHeader, tv_feedback;
    Button btn_reset, btn_fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        isCampaign = true;

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
        for (int i = 0; i < gameKey.length; i++) {
            gameBoard[i] = "     ";
            gameKey[i] = "     ";
        }
        currentMoves = 0;
        populateLevel(currentLevelId);

        // Grid adapter setup
        imageAdapter = new CustomImageAdapter(this);
        gv.setAdapter(imageAdapter);

        gv.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gv.setVerticalScrollBarEnabled(false);

        updateView();
    }

    /**
     * Populates the level depending on user progress
     * @param currentLevelId
     */
    private void populateLevel(int currentLevelId) {
        /** TUTORIAL LEVEL **/
        if (currentLevelId == 0) {
            firstTutorial = true;
            tutorialLevel();
        }
        else if (currentLevelId == 5) {
            teleportationTutorial();
            otherTutorial = true;
        }
        else if (currentLevelId == 7) {
            maxMovesTutorial();
            otherTutorial = true;
        }
        else {
            btn_fb.setVisibility(View.INVISIBLE);
        }

        int levelIndex = 0;

        for (int i = 0; i < levelList.getLevels().size(); i++) {
            if (levelList.getLevels().get(i).getId() == currentLevelId) {
                levelIndex = i;
            }
        }

        tv_levelHeader.setText(levelList.getLevels().get(levelIndex).getName());

        remainingMoves = levelList.getLevels().get(levelIndex).getMovesLeft();

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

        if (!firstTutorial && !otherTutorial) {
            int yellowCount = 0;

            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (yellowCount != 0) {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                } else {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                }
            } else {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                } else {
                    tv_feedback.setText("Exit open!");
                }
            }
        }
    }

    /**
     * Resets the state of the game
     */
    private void resetGame() {
        //initialize both the user's board and the game key
        for (int i = 0; i < gameKey.length; i++) {
            gameBoard[i] = "     ";
            gameKey[i] = "     ";
        }
        currentMoves = 0;
        populateLevel(currentLevelId);

        if (currentLevelId == 0) {
            firstTutorial = true;
        }

        //initialize other activity components
        tv_moves.setText("Moves: " + currentMoves);

        int yellowCount = 0;

        for (int i = 0; i < gameKey.length; i++) {
            if (gameKey[i].equals("[ Y ]")) {
                yellowCount++;
            }
        }

        if (!firstTutorial && !otherTutorial) {
            if (yellowCount != 0) {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                } else {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                }
            } else {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                } else {
                    tv_feedback.setText("Exit open!");
                }
            }
        }

        updateView();
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
                if (!gameKey[currentIndex + 1].equals("     ") && !gameKey[currentIndex + 1].equals("[ X ]")) {
                    moveRight();
                }
            }
            @Override
            public void onSwipeUp() {
                if (!gameKey[currentIndex - 7].equals("     ") && !gameKey[currentIndex - 7].equals("[ X ]")) {
                    moveUp();
                }
            }
            @Override
            public void onSwipeDown() {
                if (!gameKey[currentIndex + 7].equals("     ") && !gameKey[currentIndex + 7].equals("[ X ]")) {
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
                if (!gameKey[currentIndex + 1].equals("     ") && !gameKey[currentIndex + 1].equals("[ X ]")) {
                    moveRight();
                }
            }
            @Override
            public void onSwipeUp() {
                if (!gameKey[currentIndex - 7].equals("     ") && !gameKey[currentIndex - 7].equals("[ X ]")) {
                    moveUp();
                }
            }
            @Override
            public void onSwipeDown() {
                if (!gameKey[currentIndex + 7].equals("     ") && !gameKey[currentIndex + 7].equals("[ X ]")) {
                    moveDown();
                }
            }
        });
    }

    /**
     * Updates game board
     */
    public void updateView() {
        for (int i = 0; i < gameKey.length; i++) {
            imageAdapter.mThumbIds[i] = null;
        }

        Integer[] newView = new Integer[gameKey.length];

        for (int i = 0; i < gameKey.length; i++) {
            if (gameBoard[i].equals("[ Y ]") && gameKey[i].equals("[ Y ]")) {
                newView[i] = R.drawable.yellowemptytile;
            }
            else if (gameBoard[i].equals("[ B ]") && gameKey[i].equals("[ B ]")) {
                newView[i] = R.drawable.blueemptytile;
            }
            else if (gameBoard[i].equals("[ G ]") && gameKey[i].equals("[ G ]")) {
                newView[i] = R.drawable.greyemptytile;
            }
            else if (gameBoard[i].equals("[ o ]") && gameKey[i].equals("[ Y ]")) {
                newView[i] = R.drawable.yellowoccupiedtile;
            }
            else if (gameBoard[i].equals("[ o ]") && gameKey[i].equals("[ B ]")) {
                newView[i] = R.drawable.blueoccupiedtile;
            }
            else if (gameBoard[i].equals("[ o ]") && gameKey[i].equals("[ T ]")) {
                newView[i] = R.drawable.teleportertoccupied;
            }
            else if (gameBoard[i].equals("[ T ]") && gameKey[i].equals("[ T ]")) {
                newView[i] = R.drawable.teleportertempty;
            }
            else if (gameBoard[i].equals("[ o ]") && gameKey[i].equals("[ U ]")) {
                newView[i] = R.drawable.teleporteruoccupied;
            }
            else if (gameBoard[i].equals("[ U ]") && gameKey[i].equals("[ U ]")) {
                newView[i] = R.drawable.teleporteruempty;
            }
            else if (gameBoard[i].equals("[ o ]") && gameKey[i].equals("[ V ]")) {
                newView[i] = R.drawable.teleportervoccupied;
            }
            else if (gameBoard[i].equals("[ V ]") && gameKey[i].equals("[ V ]")) {
                newView[i] = R.drawable.teleportervempty;
            }
            else if (gameBoard[i].equals("[ o ]") && gameKey[i].equals("[ G ]")) {
                newView[i] = R.drawable.greyoccupiedtile;
            }
            else if (gameBoard[i].equals("[ X ]")) {
                newView[i] = R.drawable.closedexittile;
            }
            else if (gameBoard[i].equals("[ E ]")) {
                newView[i] = R.drawable.openexittile;
            }
            else {
                newView[i] = R.drawable.emptytile;
            }
        }

        imageAdapter.mThumbIds = newView;

        imageAdapter.notifyDataSetChanged();

        gv.setAdapter(imageAdapter);

        gv.invalidateViews();

        if (firstTutorial || otherTutorial) {
            removeListeners();
        }

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

        btn_fb.setVisibility(View.VISIBLE);

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

    public void teleportationTutorial() {

        removeListeners();

        btn_fb.setVisibility(View.VISIBLE);

        tv_feedback.setText("Whoa! A teleporter! Each teleporter has two tiles of the same color. " +
                "If you step on one, you will teleport to the other end! Give it a try! (Press OK to" +
                " continue)");

        btn_fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setupListeners();
                btn_fb.setVisibility(View.INVISIBLE);
                btn_fb.setOnClickListener(null);

                otherTutorial = false;

                int yellowCount = 0;

                for (int i = 0; i < gameKey.length; i++) {
                    if (gameKey[i].equals("[ Y ]")) {
                        yellowCount++;
                    }
                }

                if (!firstTutorial && !otherTutorial) {
                    if (yellowCount != 0) {
                        if (remainingMoves != -1) {
                            tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                        } else {
                            tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                        }
                    } else {
                        if (remainingMoves != -1) {
                            tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                        } else {
                            tv_feedback.setText("Exit open!");
                        }
                    }
                }
            }
        });
    }

    public void maxMovesTutorial() {
        removeListeners();

        btn_fb.setVisibility(View.VISIBLE);

        tv_feedback.setText("Uh oh...You now only have a max amount of " +
                "moves that you can make to reach the exit! If you don't make it in time, you will fail." +
                " Good luck! (Press OK to Continue)");

        btn_fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setupListeners();
                btn_fb.setVisibility(View.INVISIBLE);
                btn_fb.setOnClickListener(null);

                otherTutorial = false;

                int yellowCount = 0;

                for (int i = 0; i < gameKey.length; i++) {
                    if (gameKey[i].equals("[ Y ]")) {
                        yellowCount++;
                    }
                }

                if (!firstTutorial && !otherTutorial) {
                    if (yellowCount != 0) {
                        if (remainingMoves != -1) {
                            tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                        } else {
                            tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                        }
                    } else {
                        if (remainingMoves != -1) {
                            tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                        } else {
                            tv_feedback.setText("Exit open!");
                        }
                    }
                }
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
            return;
        }
        // teleporter tile logic
        else if (gameKey[currentIndex - 7].equals("[ T ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ T ]") && i != currentIndex - 7) {
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
                if (gameKey[i].equals("[ U ]") && i != currentIndex - 7) {
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
                if (gameKey[i].equals("[ V ]") && i != currentIndex - 7) {
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
        for (int i = 0; i < gameKey.length; i++) {
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

            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (remainingMoves != -1 && remainingMoves - currentMoves == 0) {
                levelFailed();
                return;
            }

            if (yellowCount!=0) {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                }
            }
            else {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Exit open!");
                }
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
            return;
        }
        // teleporter tile logic
        else if (gameKey[currentIndex + 7].equals("[ T ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ T ]") && i != currentIndex + 7) {
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
                if (gameKey[i].equals("[ U ]") && i != currentIndex + 7) {
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
                if (gameKey[i].equals("[ V ]") && i != currentIndex + 7) {
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
        for (int i = 0; i < gameKey.length; i++) {
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

            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (remainingMoves != -1 && remainingMoves - currentMoves == 0) {
                levelFailed();
                return;
            }

            if (yellowCount!=0) {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                }
            }
            else {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Exit open!");
                }
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
            return;
        }
        // teleporter tile logic
        else if (gameKey[currentIndex - 1].equals("[ T ]")) {
            // replace current index
            gameBoard[currentIndex] = gameKey[currentIndex];
            // search for second teleportation tile
            for (int i = 0; i < gameKey.length; i++) {
                // if it equals the teleporter tile and isnt the current index
                if (gameKey[i].equals("[ T ]") && i != (currentIndex - 1)) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex - 1].equals("[ U ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ U ]") && i != currentIndex - 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
        }
        // teleporter cont
        else if (gameKey[currentIndex - 1].equals("[ V ]")) {
            gameBoard[currentIndex] = gameKey[currentIndex];
            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ V ]") && i != currentIndex - 1) {
                    gameBoard[i] = "[ o ]";
                    teleporterFlag = true;
                    teleporterIndex = i;
                }
            }
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
        for (int i = 0; i < gameKey.length; i++) {
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
            Log.d("Index", "" + teleporterIndex);
            currentIndex = teleporterIndex;
        }

        if (!firstTutorial) {
            int yellowCount = 0;

            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (remainingMoves != -1 && remainingMoves - currentMoves == 0) {
                levelFailed();
                return;
            }

            if (yellowCount!=0) {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                }
            }
            else {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Exit open!");
                }
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
            return;
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
        for (int i = 0; i < gameKey.length; i++) {
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

            for (int i = 0; i < gameKey.length; i++) {
                if (gameKey[i].equals("[ Y ]")) {
                    yellowCount++;
                }
            }

            if (remainingMoves != -1 && remainingMoves - currentMoves == 0) {
                levelFailed();
                return;
            }

            if (yellowCount!=0) {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Yellow Tiles Remaining: " + yellowCount);
                }
            }
            else {
                if (remainingMoves != -1) {
                    tv_feedback.setText("Exit open!" + "\nMoves Left: " + (remainingMoves - currentMoves));
                }
                else {
                    tv_feedback.setText("Exit open!");
                }
            }
        }

        updateView();
    }

    public void finishLevel() {
        if (isCampaign) {
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
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            } else if (currentLevelId == 9 && isCampaign) {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
                alertDialog.setMessage("Congratulations on completing the campaign! Make sure" +
                        " to check the Downloadable Levels if you would like to continue your adventure!\n\n"
                        + "Moves Taken: " + currentMoves + " moves.\n" + "Moves Record: " + currentMoves + " " +
                        "moves.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                resetGame();
                                finish();
                            }
                        });
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Level Complete!");
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
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }

    public void levelFailed() {
        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
        alertDialog.setTitle("Level Faied!");
        alertDialog.setMessage("You ran out of moves. Click OK to try again!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        resetGame();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
