import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public static final String BOUNCER_IMAGE = "ball.gif";

    public static final Paint GROWER_COLOR = Color.BISQUE;
    public static final double GROWER_RATE = 1.1;
    public static final int GROWER_SIZE = 50;

    // some things we need to remember during our game
    private Scene myScene;
    private Ball myBall;
    private Paddle myPaddle;
    private ImageView myBouncer;//BALL1

    private Rectangle myMover;//TOP BLOCK
    private Block myBlock;
    private Rectangle blockRectangle;


    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        var root = new Group();
        myScene = setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND,root);
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
    private Scene setupGame (int width, int height, Paint background, Group root) {
        // create one top level collection to organize the things in the scene

        // create a place to see the shapes
        var scene = new Scene(root, width, height, background);
        // make some shapes and set their properties
        var image = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        myBall=new Ball(width,height);
        myBouncer = myBall.getIcon();

        myBlock=new Block(1);
        blockRectangle=myBlock.getBlock();



        myPaddle=new Paddle(width,height);
        myMover = myPaddle.getPaddle();

        // order added to the group is the order in which they are drawn
        root.getChildren().add(myBouncer);
        root.getChildren().add(blockRectangle);
        root.getChildren().add(myMover);
       // root.getChildren().add(myGrower);
        // respond to input
        scene.setOnKeyPressed(e -> myPaddle.handleKeyInput(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        return scene;
    }

    // Change properties of shapes to animate them
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start.
    private void step (double elapsedTime, Group rootGroup) {
        // update attributes
        if (myMover.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {

        myBall.paddleCollide(myMover.getX(),myMover.getY()-myMover.getHeight()/2);
        }

        if (blockRectangle.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {

            myBall.blockCollide(blockRectangle.getY()-blockRectangle.getHeight()/2,blockRectangle.getY()+blockRectangle.getHeight()/2,blockRectangle.getX()+blockRectangle.getWidth()/2,blockRectangle.getX()-blockRectangle.getWidth()/2);
            myBlock.hit();
            if(myBlock.getMyLives()==0){
                rootGroup.getChildren().remove(blockRectangle);
            }
        }

        myBall.go(elapsedTime,SCREEN_WIDTH,SCREEN_HEIGHT);








    }



    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
//        if (myGrower.contains(x, y)) {
//            myGrower.setScaleX(myGrower.getScaleX() * GROWER_RATE);
//            myGrower.setScaleY(myGrower.getScaleY() * GROWER_RATE);
//        }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
