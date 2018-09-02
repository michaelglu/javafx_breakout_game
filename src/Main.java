import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application  {
    public static final String TITLE = "Breakout";
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 600;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;



    //Objects used in the Game
    private Scene myScene;
    private Block[][]myBlocks;
    private Ball myBall;
    private Paddle myPaddle;
    private Rectangle myMover;//TOP BLOCK


    @Override
    public void start (Stage stage) {

        // attach scene to the stage and display it
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



        myPaddle=new Paddle(width,height);


        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBall.getIcon());
        root.getChildren().add(myPaddle.getPaddle());
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode(),root,SCREEN_WIDTH));
//        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime, Group rootGroup) {
        // update attributes
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
                       if(myBall.getHitsEnabled()){
                           myBlocks[i][j].hit();
                       }
                       myBall.blockCollide(myBlocks[i][j].getBlock().getY()-myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getY()+myBlocks[i][j].getBlock().getFitHeight()/2,myBlocks[i][j].getBlock().getX()+myBlocks[i][j].getBlock().getFitWidth()/2,myBlocks[i][j].getBlock().getX()-myBlocks[i][j].getBlock().getFitWidth()/2);


                   if(myBlocks[i][j].getMyLives()==0){
                       rootGroup.getChildren().remove(myBlocks[i][j].getBlock());
                   }

               }}
           }
       }



        myBall.go(elapsedTime,SCREEN_WIDTH,SCREEN_HEIGHT);
    }
    public void handleKeyInput (KeyCode code, Group myGroup, int screenWidth) {
        if (code == KeyCode.SPACE ) {
            startGame(myGroup);
        }
        myPaddle.handleKeyInput(code,screenWidth);

    }
    public void startGame(Group myGroup){
        myBall.setSpeed(120);
    }

    public static void main (String[] args) {
        launch(args);
    }
}
