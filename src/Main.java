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
import javafx.stage.Stage;
import javafx.util.Duration;

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


    @Override
    public void start (Stage stage) {

        // attach scene to the stage and display it
        gameOverImage = new Image(this.getClass().getClassLoader().getResourceAsStream("gameover.gif"));
        splashImage=new Image(this.getClass().getClassLoader().getResourceAsStream("splashScreen.gif"));
        lifeImage=new Image(this.getClass().getClassLoader().getResourceAsStream("life.gif"));
        splashScreen=new ImageView(splashImage);
        splashScreen.setFitHeight(SCREEN_HEIGHT);
        splashScreen.setFitWidth(SCREEN_WIDTH);
        var root = new Group();
        myScene = setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND,3,root);
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
    private Scene setupGame (int width, int height, Paint background, int level, Group root) {
        // create one top level collection to organize the things in the scene
        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        myBall=new Ball(width,height);

        //GENERATE BLOCKS on the screen:
        myBlocks=new Block[5][8];
        setupLevel(root,3);



        myPaddle=new Paddle(width,height);


        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall.getIcon());
        root.getChildren().add(myPaddle.getPaddle());
        root.getChildren().add(splashScreen);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(),root,SCREEN_WIDTH));
//        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime, Group rootGroup) {
        // update attributes
       if (myBall.getMyLives()==0)
       {
           splashScreen.setImage(gameOverImage);
           rootGroup.getChildren().clear();
           rootGroup.getChildren().add(splashScreen);
       }
       if(myBall.getMyLives()>0&&myBall.getMyLives()<lives.size()){
           rootGroup.getChildren().remove(lives.get(lives.size()-1));
            lives.remove(lives.size()-1);
        }
        if (myPaddle.getPaddle().getBoundsInParent().intersects(myBall.getIcon().getBoundsInParent())) {

        myBall.paddleCollide(myPaddle.getPaddle().getX(),myPaddle.getPaddle().getY()-myPaddle.getPaddle().getFitHeight()/2-1);
        }
       for(int i=0;i<5;i++)
       {
           for(int j=0;j<8;j++)
           {
               if(myBlocks[i][j]!=null)
               {

                   if (myBlocks[i][j].getBlock().getBoundsInParent().intersects(myBall.getIcon().getBoundsInParent())&&myBlocks[i][j].getVisibility()) {



                              myBlocks[i][j].hit();


//
                       myBall.blockCollide(myBlocks[i][j].getBlock().getY()-myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getY()+myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getX()+myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX()-myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX());





                       if(myBlocks[i][j].getMyLives()==0){
                       rootGroup.getChildren().remove(myBlocks[i][j].getBlock());
                   }

               }}
           }
       }



        myBall.go(elapsedTime,SCREEN_WIDTH,SCREEN_HEIGHT);
    }
    public void handleKeyInput (KeyCode code, Group myGroup, int screenWidth) {
        if (code == KeyCode.SPACE &&myBall.getMyLives()>0) {
            startGame(myGroup);
        }
        myPaddle.handleKeyInput(code,screenWidth);

    }
    public void setupLevel(Group root, int level){
        lives=new ArrayList();
        for(int i=0;i<myBall.getMyLives();i++)
        {
            System.out.println("added life");
           lives.add(new ImageView(lifeImage));
           lives.get(i).setFitWidth(50);
            lives.get(i).setFitHeight(50);
            lives.get(i).setY(0);
            lives.get(i).setX(55*i);
            root.getChildren().add(lives.get(i));
        }
        //BLOCK SETUP
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<8;j++){//Want to generate blocks 80% of the time
                if(i%2!=0&&j%2==0)//Ensures at least 50% is populated
                {
                    myBlocks[i][j]=new Block(j,i,level);
                    root.getChildren().add(myBlocks[i][j].getBlock());
                }
                //randomize the rest of the blocks
                else{

                    int benchmark=(int)(Math.random()*10);
                    if(benchmark>4)
                    {
                        myBlocks[i][j]=new Block(j,i,level);
                        root.getChildren().add(myBlocks[i][j].getBlock());
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
