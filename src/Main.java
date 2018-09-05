import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;

import javafx.scene.image.ImageView;
import java.awt.*;
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
    private Image lifeImage;
    private Image splashImage,gameOverImage;
    private ArrayList<ImageView> lives;
    private Text scoreBoard;
    private int blockCount;//Number of blocks visible on the screen
    private int score;//player score
    private int currentLevel;//current level
    private boolean endlessMode;//whether or not endless mode is enabled
    private Text levelBoard;


    @Override
    public void start (Stage stage) {
       //Load Variables
        scoreBoard=new Text("Score: ");
        levelBoard=new Text("");
        endlessMode=false;
        currentLevel=1;
        lives=new ArrayList();
        //Load Images
        gameOverImage = new Image(this.getClass().getClassLoader().getResourceAsStream("gameover.gif"));
        splashImage=new Image(this.getClass().getClassLoader().getResourceAsStream("splashScreen.gif"));
        lifeImage=new Image(this.getClass().getClassLoader().getResourceAsStream("life.gif"));
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
        //Ball
        myBall=new Ball(sceneWidth,sceneHeight);
        //Paddle
        myPaddle=new Paddle(sceneWidth,sceneHeight);
        //The game has 3 normal levels and an endless mode, for each normal level
//        if(currentLevel>=4){//endless level is set up with hardest blocks only
//            setupLevel(root,3);
//            endlessMode=true;
//        }
//        else{
//            setupLevel(root,currentLevel);
//        }
        setupLevel(root,currentLevel);


        root.getChildren().add(splashScreen);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(),root,SCREEN_WIDTH));// respond to keyboard input
        return scene;
    }

    private void step (double elapsedTime, Group rootGroup) {
//        System.out.println("BLOCKS LEFT: "+blockCount);

       if (myBall.getMyLives()==0){// Check if there are lives left
           splashScreen.setImage(gameOverImage);
           rootGroup.getChildren().clear();
           setupLevel(rootGroup,1);
           rootGroup.getChildren().add(splashScreen);
       }
       if(myBall.getMyLives()>0&&myBall.getMyLives()<lives.size()){//Check if a life was lost during the last step
           rootGroup.getChildren().remove(lives.get(lives.size()-1));
            lives.remove(lives.size()-1);
        }
        if(!endlessMode){//Check if the level is complete; endless mode is never complete=> don't check during endless mode
            if(blockCount==0){
                currentLevel+=1;
//                System.out.println("Level: "+currentLevel);
                setupLevel(rootGroup,currentLevel);
            }
        }
        else if(blockCount<5){//Fires during endless mode
            setBlocks(rootGroup,3);
        }
        //Paddle collision
        if (myPaddle.getPaddle().getBoundsInParent().intersects(myBall.getIcon().getBoundsInParent())) {
            myBall.paddleCollide(myPaddle.getPaddle().getX(),myPaddle.getPaddle().getY()-myPaddle.getPaddle().getFitHeight()/2);
        }
        //Loop through blocks[][] to check for collisions & subtract lives upon collision
       for(int i=0;i<5;i++)//rows
       {
           for(int j=0;j<8;j++)//columns
           {
               if(myBlocks[i][j]!=null){//some are empty, see setBlocks()
                   //myBlock.check for collisions, myBall.check for collisions()
                   if (myBlocks[i][j].getBlock().getBoundsInParent().intersects(myBall.getIcon().getBoundsInParent())&&myBlocks[i][j].getVisibility()) {
                       myBlocks[i][j].hit();
                       score+=10;
                       scoreBoard.setText("Score: "+score);
                       myBall.blockCollide(myBlocks[i][j].getBlock().getY()-myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getY()+myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getX()+myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX()-myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX());
                       if(myBlocks[i][j].getMyLives()==0){
                       rootGroup.getChildren().remove(myBlocks[i][j].getBlock());//Makes the block invisible
                       blockCount-=1;
                   }
               }}
           }
       }
       //The ball's logic for checking for wall collisions is inside the go() method
        myBall.go(elapsedTime,SCREEN_WIDTH,SCREEN_HEIGHT);
    }

    public void handleKeyInput (KeyCode code, Group myGroup, int screenWidth) {
        if (code == KeyCode.SPACE &&myBall.getMyLives()>0) {//For dismissing splashscreen
//            setupLevel(myGroup,currentLevel);
            startGame(myGroup);
        }
        if(code==KeyCode.I)
        {
            myBall.setIndestructable();
        }
        if(code==KeyCode.DIGIT2)
        {
            endlessMode=false;
            currentLevel=2;
            setupLevel(myGroup,currentLevel);
        }
        if(code==KeyCode.DIGIT3)
        {
            endlessMode=false;
            currentLevel=3;
            setupLevel(myGroup,currentLevel);
        }
        if (code==KeyCode.E)
        {
            currentLevel=4;
            endlessMode=true;
            setupLevel(myGroup,3);
        }

        myPaddle.handleKeyInput(code,screenWidth);//for paddle

    }

    public void setupLevel(Group root, int level){
    //    System.out.println(level);
        if(level>3)
        {
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

        myBall.resetBall(SCREEN_WIDTH,SCREEN_HEIGHT);

    }
    public void setBlocks(Group root, int level){
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<8;j++){//Want to generate blocks 80% of the time
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
            lives.get(i).setFitWidth(50);
            lives.get(i).setFitHeight(50);
            lives.get(i).setY(0);
            lives.get(i).setX(55*i);
            root.getChildren().add(lives.get(i));
        }

    }
    public void startGame(Group myGroup){
        myGroup.getChildren().remove(splashScreen);
        myBall.setSpeed(180);
    }

    public static void main (String[] args) {
        launch(args);
    }
}
