import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class Main extends Application  {
    public static final String TITLE = "Breakout";
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.WHITE;



    //Objects used in the Game
    private Scene myScene;
    private Block[][]myBlocks;
    private Ball myBall;
    private Paddle myPaddle;
    private ImageView splashScreen;
    private ArrayList<ImageView> lives;
    private ArrayList<Powerup> powerups;
    private Text scoreBoard;
    private Text levelBoard;
    //Images
    private Image lifeImage;
    private Image splashImage,gameOverImage;
    //Variables
    private int blockCount;//Number of blocks visible on the screen
    private int score;//player score
    private int currentLevel;//current level
    private boolean endlessMode;//whether or not endless mode is enabled


    @Override
    public void start (Stage stage) {
       //Load Variables & initialize Lists
        scoreBoard=new Text("");
        levelBoard=new Text("");
        endlessMode=false;
        currentLevel=1;
        lives=new ArrayList();
        powerups=new ArrayList<>();
        //Load Images
        gameOverImage = new Image(this.getClass().getClassLoader().getResourceAsStream("gameover.GIF"));
        splashImage=new Image(this.getClass().getClassLoader().getResourceAsStream("SplashScreen.GIF"));
        lifeImage=new Image(this.getClass().getClassLoader().getResourceAsStream("life.GIF"));

        // create one top level collection to organize the things in the scene
        var root = new Group();
        // attach scene to the stage and display it
        myScene = setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND,currentLevel,root);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        // attach "game loop" to timeline to play it
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY, root));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int sceneWidth, int sceneHeight, Paint background, int level, Group root) {
        // create a place to see the shapes
        var scene = new Scene(root, sceneWidth, sceneHeight, background);
        //Load Nodes

        //splash screen
        splashScreen=new ImageView(splashImage);
        splashScreen.setFitHeight(SCREEN_HEIGHT);
        splashScreen.setFitWidth(SCREEN_WIDTH);
        //Paddle
        myPaddle=new Paddle(sceneWidth,sceneHeight);
        //Ball
        myBall=new Ball(sceneWidth,sceneHeight,myPaddle);

        setupLevel(root,currentLevel);
        //Splash screen gets drawn above everything else
        root.getChildren().add(splashScreen);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(),root,SCREEN_WIDTH));// respond to keyboard input
        return scene;
    }

    private void step (double elapsedTime, Group rootGroup) {
       if (myBall.getMyLives()==0){// Check if there are lives left
           splashScreen.setImage(gameOverImage);
           rootGroup.getChildren().clear();
           setupLevel(rootGroup,1);
           rootGroup.getChildren().add(splashScreen);
       }
       //check if life was added:
        if(myBall.getMyLives()>lives.size()){
            lives.add(new ImageView(lifeImage));
            lives.get(lives.size()-1).setFitWidth(20);
            lives.get(lives.size()-1).setFitHeight(20);
            lives.get(lives.size()-1).setY(0);
            lives.get(lives.size()-1).setX(25*(myBall.getMyLives()-1));
            rootGroup.getChildren().add(lives.get(lives.size()-1));
        }
        //Check if a life was lost
       if(myBall.getMyLives()>0&&myBall.getMyLives()<lives.size()){
           rootGroup.getChildren().remove(lives.get(lives.size()-1));
            lives.remove(lives.size()-1);
        }
        if(!endlessMode){//Check if the level is complete; endless mode is never complete=> don't check during endless mode
            if(blockCount==0){
                currentLevel+=1;
                setupLevel(rootGroup,currentLevel);
            }
        }
        else if(blockCount<5){//Fires during endless mode
            setBlocks(rootGroup,3);
        }
        //Activate all powerups; all of powerups logic is contained inside their go method
        for(Powerup p:powerups)
        {
            p.go(elapsedTime);
        }
        //Loop through blocks[][] to check for collisions & subtract lives upon collision
       for(int i=0;i<5;i++)//rows
       {
           for(int j=0;j<8;j++)//columns
           {
               if(myBlocks[i][j]!=null){//some are empty, see setBlocks()
                   if (myBlocks[i][j].checkCollisions(myBall)) {
                       score+=10;
                       scoreBoard.setText("Score: "+score);
                       if(myBlocks[i][j].getMyLives()==0){
                       rootGroup.getChildren().remove(myBlocks[i][j].getBlock());//Makes the block invisible
                       blockCount-=1;
                   }
               }}
           }
       }
       //The ball's logic for checking for wall collisions is inside the go() method
        myBall.go(elapsedTime);
    }

    public void handleKeyInput (KeyCode code, Group myGroup, int screenWidth) {
        if (code == KeyCode.SPACE &&myBall.getMyLives()>0) {//For dismissing splashscreen
            startGame(myGroup);
        }
        if(code==KeyCode.I)//Especially useful for debugging, once on, can't be turned off
        {
            myBall.setIndestructible();
        }
        if(code==KeyCode.DIGIT2)//Jump to level 2/reset level 2
        {
            endlessMode=false;
            currentLevel=2;
            setupLevel(myGroup,currentLevel);
        }
        if(code==KeyCode.DIGIT3)//Jump to level 3/reset level 3
        {
            endlessMode=false;
            currentLevel=3;
            setupLevel(myGroup,currentLevel);
        }
        if (code==KeyCode.E)//Jump to endless mode/reset endless mode
        {
            currentLevel=4;
            endlessMode=true;
            setupLevel(myGroup,3);
        }

        myPaddle.handleKeyInput(code,screenWidth);//for paddle

    }

    public void setupLevel(Group root, int level){
        if(level>3){
            endlessMode=true;
            level=3;
        }
        root.getChildren().clear();
        myBall.setLives(3);
        blockCount=0;
        root.getChildren().add(myBall.getIcon());//myBall.getIcon() returns the Imageview of the Ball
        root.getChildren().add(scoreBoard);
        root.getChildren().add(myPaddle.getPaddle());//myPaddle.getPaddle() returns the Imageview of the Paddle
        root.getChildren().add(levelBoard);
        setUpScoreBoard();
        setUpLevelBoard(level);
        setUpLives(root);
        //GENERATE BLOCKS on the screen:
        myBlocks=new Block[5][8];
        setBlocks(root,level);
        myPaddle.reset(SCREEN_WIDTH,SCREEN_HEIGHT);
        myBall.resetBall(SCREEN_WIDTH,SCREEN_HEIGHT);
        if(currentLevel>1){myBall.activateSmartPaddle();}//SMART Paddle feature activates after level 2

    }
    public void setBlocks(Group root, int level){
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<8;j++){//Want to generate blocks on average 80% of the time
                if(myBlocks[i][j]==null||!myBlocks[i][j].getVisibility()){
                    if(i%2!=0&&j%2==0)//Ensures at least 50% is populated
                    {
                        myBlocks[i][j]=new Block(j,i,level);
                        root.getChildren().add(myBlocks[i][j].getBlock());
                        blockCount+=1;
                    }
                    //randomize the rest of the blocks
                    else{

                        int benchmark=(int)(Math.random()*10);
                        if(benchmark>4)
                        {
                            myBlocks[i][j]=new Block(j,i,level);
                            root.getChildren().add(myBlocks[i][j].getBlock());
                            blockCount+=1;
                            if(benchmark==5)
                            {
                                powerups.add(myBlocks[i][j].setMyPowerup(root,"lives",myBall,myPaddle));
                            }
                            else if(benchmark==6){
                                powerups.add(myBlocks[i][j].setMyPowerup(root,"speedy",myBall,myPaddle));
                            }
                            else if(benchmark==7){
                                powerups.add(myBlocks[i][j].setMyPowerup(root,"size",myBall,myPaddle));
                            }
                        }
                    }
                }
            }
        }

    }
    public void setUpScoreBoard(){
        //Scoreboard setup
        scoreBoard.setY(25);
        scoreBoard.setX(SCREEN_WIDTH-150);
        scoreBoard.setFont(Font.font("Verdana", 16));
        scoreBoard.setText("Score: ");
        //score reset:
        score =0;
    }
    public void setUpLevelBoard(int level){
        //Scoreboard setup
        levelBoard.setY(25);
        levelBoard.setX(SCREEN_WIDTH-220);
        levelBoard.setFont(Font.font("Verdana", 14));
        if(!endlessMode){levelBoard.setText(""+level);}
        else {levelBoard.setText("ENDLESS");}

    }
    public void setUpLives(Group root){
        for(int i=0;i<myBall.getMyLives();i++)
        {
            lives.add(new ImageView(lifeImage));
            lives.get(i).setFitWidth(20);
            lives.get(i).setFitHeight(20);
            lives.get(i).setY(0);
            lives.get(i).setX(25*i);
            root.getChildren().add(lives.get(i));
        }

    }
    public void startGame(Group myGroup){
        myGroup.getChildren().remove(splashScreen);
        myBall.setSpeed(myBall.DEFAULT_BALL_SPEED);
    }

    public static void main (String[] args) {
        launch(args);
    }
}
