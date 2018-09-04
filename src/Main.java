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
    private Rectangle myMover;//TOP BLOCK
    private ImageView splashScreen;
    private ImageView gameOver;
    private Image lifeImage;
    private Image splashImage;
    private Image gameOverImage;
    private ArrayList<ImageView> lives;
    private Text scoreBoard;
    private int blockCount;
    private int score;
    private int currentLevel;
    private boolean endlessMode;


    @Override
    public void start (Stage stage) {

       //Load Variables Images + SplashScreen
        scoreBoard=new Text("Score: ");
        endlessMode=false;

        currentLevel=4;
        gameOverImage = new Image(this.getClass().getClassLoader().getResourceAsStream("gameover.gif"));
        splashImage=new Image(this.getClass().getClassLoader().getResourceAsStream("splashScreen.gif"));
        lifeImage=new Image(this.getClass().getClassLoader().getResourceAsStream("life.gif"));
        splashScreen=new ImageView(splashImage);
        splashScreen.setFitHeight(SCREEN_HEIGHT);
        splashScreen.setFitWidth(SCREEN_WIDTH);
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
        //Load Game Elements
        myBall=new Ball(sceneWidth,sceneHeight);
        myPaddle=new Paddle(sceneWidth,sceneHeight);
        if(currentLevel<4){
            setupLevel(root,currentLevel);
        }
        else{
            setupLevel(root,3);
            endlessMode=true;
        }

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall.getIcon());
        root.getChildren().add(scoreBoard);
        root.getChildren().add(myPaddle.getPaddle());
        root.getChildren().add(splashScreen);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(),root,SCREEN_WIDTH));
//        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }

    private void step (double elapsedTime, Group rootGroup) {
        // Check if there are lives left
       if (myBall.getMyLives()==0)
       {
           splashScreen.setImage(gameOverImage);
           rootGroup.getChildren().clear();
           rootGroup.getChildren().add(splashScreen);
       }
       //Check if a life was lost during the last step
       if(myBall.getMyLives()>0&&myBall.getMyLives()<lives.size()){
           rootGroup.getChildren().remove(lives.get(lives.size()-1));
            lives.remove(lives.size()-1);
        }
        //Check if the level is complete:
        if(!endlessMode){
            if(blockCount==0){
                currentLevel+=1;
                // System.out.println("Loading level: "+currentLevel);
                setupLevel(rootGroup,currentLevel);
            }
        }
        else if(blockCount<5){
            setBlocks(rootGroup,3);
        }

        //Paddle collision
        if (myPaddle.getPaddle().getBoundsInParent().intersects(myBall.getIcon().getBoundsInParent())) {
            myBall.paddleCollide(myPaddle.getPaddle().getX(),myPaddle.getPaddle().getY()-myPaddle.getPaddle().getFitHeight()/2);
        }
        //Loop through blocks[][] to check for collisions & subtract lives upon collision
       for(int i=0;i<5;i++)//rows
       {
           for(int j=0;j<8;j++)//cols
           {
               if(myBlocks[i][j]!=null){//some are empty
                   if (myBlocks[i][j].getBlock().getBoundsInParent().intersects(myBall.getIcon().getBoundsInParent())&&myBlocks[i][j].getVisibility()) {
                       myBlocks[i][j].hit();
                       score+=10;
                       scoreBoard.setText("Score: "+score);
                       myBall.blockCollide(myBlocks[i][j].getBlock().getY()-myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getY()+myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getX()+myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX()-myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX());
                       if(myBlocks[i][j].getMyLives()==0){
                       rootGroup.getChildren().remove(myBlocks[i][j].getBlock());
                       blockCount-=1;
                   }
               }}
           }
       }
       //The ball's logic for checking for wall collisions is inside the go() method
        myBall.go(elapsedTime,SCREEN_WIDTH,SCREEN_HEIGHT);
    }
    public void handleKeyInput (KeyCode code, Group myGroup, int screenWidth) {
        if (code == KeyCode.SPACE &&myBall.getMyLives()>0) {//For splashscreen
            startGame(myGroup);
        }
        myPaddle.handleKeyInput(code,screenWidth);//for paddle

    }
    public void setupLevel(Group root, int level){
        //Scoreboard setup
        scoreBoard.setY(25);
        scoreBoard.setX(SCREEN_WIDTH-150);
        scoreBoard.setFont(Font.font("Verdana", 20));
        scoreBoard.setText("Score: ");

        //Lives set up
        lives=new ArrayList();
        for(int i=0;i<myBall.getMyLives();i++)
        {
           lives.add(new ImageView(lifeImage));
           lives.get(i).setFitWidth(50);
           lives.get(i).setFitHeight(50);
           lives.get(i).setY(0);
           lives.get(i).setX(55*i);
           root.getChildren().add(lives.get(i));
        }
        //score reset:
        score =0;
        //GENERATE BLOCKS on the screen:
        myBlocks=new Block[5][8];
        setBlocks(root,level);
        myBall.resetBall(SCREEN_WIDTH,SCREEN_HEIGHT);
        if(currentLevel>1){
            myBall.setSpeed(180);
        }
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
    public void startGame(Group myGroup){
        myGroup.getChildren().remove(splashScreen);
        myBall.setSpeed(180);
    }

    public static void main (String[] args) {
        launch(args);
    }
}
