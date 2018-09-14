import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/*
Ball.java is a class containing the game logic for the Ball object;
I beleive that it is well designed, because it's DRY, meaning that there is no repeated code; all methods are <20 lines
in length due to an effective use of helper methods; The code is also SHY, as it does comeplete tasks when called by
other classes: the Ball's go() method invoked in a Main class only takes 1 parameter: elapsed time and from then handles
all the collision logic by itself; The BlockCollide method, called by the Block class also handles all the logic of the
collision by itself. The Ball.java follows the principle of Tell the Other Guy because other classes can call a single
method of the Ball class, and a Ball object takes care of the rest.
The class also follows consistent naming shceme, as all variables and methods are in a camelCase format, which is a
standard of the industry
*/
public class Ball {
    public final int DEFAULT_BALL_SPEED=120;
    private final int  MAX_BALL_SPEED=240;
    private int xDirection;
    private int yDirection;
    private double mySpeed;
    private int myLives;
    private ImageView icon;
    private Image image;
    private boolean isIndestructible;
    private Paddle myPaddle;
    private boolean isSpeedy;
    private boolean smartPaddle;

    public Ball(int width, int height, Paddle paddle){
        isSpeedy=false;
        smartPaddle=false;
        myPaddle=paddle;
        isIndestructible =false;
        myLives =3;
        xDirection=1;
        yDirection=1;
        mySpeed=0;
        image =new Image(this.getClass().getClassLoader().getResourceAsStream("ball.gif"));
        icon = new ImageView(image);
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        resetBall(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);

    }
    public void resetBall(int width,int height){
        isSpeedy=false;
        smartPaddle=false;
        mySpeed=0;
        icon.setX(width / 2 - icon.getBoundsInLocal().getWidth() / 2);
        icon.setY(height / 2 - icon.getBoundsInLocal().getHeight() / 2);
        xDirection=1;
        yDirection=1;
    }



    public void go(double elapsedTime){//Called in step() method of Main.java, handle's most of the Ball's logic
        checkPaddleCollisions();
        checkWallCollision(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
        icon.setX(icon.getX() + mySpeed * elapsedTime*xDirection);
        icon.setY(icon.getY() + mySpeed * elapsedTime*yDirection);
    }


    public void checkPaddleCollisions(){
        if (myPaddle.getPaddle().getBoundsInParent().intersects(icon.getBoundsInParent())) {
            paddleCollide(myPaddle.getPaddle().getX()+myPaddle.getPaddle().getFitWidth()/2,myPaddle.getPaddle().getY()-myPaddle.getPaddle().getFitHeight()/2);
        }
    }
    private void checkWallCollision(int screenWidth, int screenHeight){
        if(icon.getX()>screenWidth-1||icon.getX()<1){
            xDirection=-1*xDirection;
        }
        if(icon.getY()<75){

            yDirection=-1*yDirection;
        }
        if(icon.getY()>=screenHeight) {
            if (!isIndestructible) {
                myLives -= 1;
                resetBall(screenWidth, screenHeight);
            }
            else{
                yDirection=-1*yDirection;
            }
        }

    }
    //blockCollide handles block collision logic, called by the Block's checkCollisions() method
    public void blockCollide (double blockTop,double blockBottom, double blockRight, double blockLeft, double blockCenterX,double blockCenterY)
    {
        //Hitting on the left side, xdiredtion>0 switch to <0
         if((icon.getX()+icon.getFitWidth())<=blockCenterX&&(icon.getX()+icon.getFitWidth()) >= blockLeft&&((icon.getY()+icon.getFitHeight()/2)>blockTop&&(icon.getY()+icon.getFitHeight()/2)<blockBottom)){
            xDirection=-1*Math.abs(xDirection);
         }
        //Hitting on the right side, xdiredtion<0 switch to >0
        else if(icon.getX()>=blockCenterX&&icon.getX() <= blockRight&&((icon.getY()+icon.getFitHeight()/2)>blockTop&&(icon.getY()+icon.getFitHeight()/2)<blockBottom)){
            xDirection=Math.abs(xDirection);
        }
        //Hitting top: go up
        if(icon.getY()+icon.getFitHeight()<=blockCenterY&&icon.getY()+icon.getFitHeight()>=blockTop&&yDirection>0)
        {
            yDirection = -1 * Math.abs(yDirection);
        }
        //Hitting bottom go down
        else if(icon.getY()>blockCenterY&&icon.getY()<=blockBottom&&yDirection<0) {
            yDirection = Math.abs(yDirection);
        }
    }
    private void paddleCollide(double paddleCenter,double paddleY){

            if(icon.getY()>paddleY){
                yDirection=-1*yDirection;
            }

            if(smartPaddle){
                if(icon.getX()>paddleCenter){
                    xDirection=Math.abs(xDirection);
                }
                else if(icon.getX()<paddleCenter){
                    xDirection=-1*Math.abs(xDirection);
                }
            }
            if(isSpeedy && mySpeed<MAX_BALL_SPEED){
                mySpeed*=1.5;
                isSpeedy=false;
            }
    }
    public void activateSmartPaddle(){smartPaddle=true;}
    public void setSpeed(int speed){
        mySpeed=speed;
    }
    public ImageView getIcon(){return icon;}
    public void addLives(int lives){
        myLives+=lives;
    }
    public int getMyLives(){
        return myLives;
    }
    public void setSpeedy(){//called ny Powerup.java
        isSpeedy=true;
    }
    public void setLives(int lives){myLives=lives;}
    public void setIndestructible(){isIndestructible =true;}

}
