import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Ball {
    private int xDirection;
    private int yDirection;
    private int mySpeed;
    private int myLives;
    private ImageView icon;
    private Image image;
    private long timeOfLastHit;
    private boolean indestructable;


    public Ball(int width,int height){
        indestructable=false;
        timeOfLastHit=System.nanoTime();
        myLives =3;
        xDirection=1;
        yDirection=1;
        mySpeed=0;
        image =new Image(this.getClass().getClassLoader().getResourceAsStream("ball.gif"));
        icon = new ImageView(image);
        resetBall(width,height);
    }
    public void resetBall(int width,int height){
        mySpeed=0;
        icon.setX(width / 2 - icon.getBoundsInLocal().getWidth() / 2);
        icon.setY(height / 2 - icon.getBoundsInLocal().getHeight() / 2);
        xDirection=1;
        yDirection=1;
    }


    public ImageView getIcon(){return icon;}
    public void go(double elapsedTime, int screenWidth, int screenHeight){
        checkWallCollision(screenWidth,screenHeight);
        icon.setX(icon.getX() + mySpeed * elapsedTime*xDirection);
        icon.setY(icon.getY() + mySpeed * elapsedTime*yDirection);
    }
    public void setSpeed(int speed){
        mySpeed=speed;
    }
    public void checkWallCollision(int screenWidth, int screenHeight){
        if(icon.getX()>screenWidth-1||icon.getX()<1){
            xDirection=-1*xDirection;
        }
        if(icon.getY()<75){

            yDirection=-1*yDirection;
        }
        if(icon.getY()>=screenHeight) {
            if (!indestructable) {
                myLives -= 1;
                resetBall(screenWidth, screenHeight);
            }
            else{
                yDirection=-1*yDirection;
            }
        }

    }

    public void blockCollide (double blockTop,double blockBottom, double blockRight, double blockLeft, double blockCenter)
    {

        if(icon.getX() < blockCenter&&(icon.getY()>blockTop&&icon.getY()<blockBottom)){
            xDirection=-1*Math.abs(xDirection);

        }

        else if(icon.getX() > blockCenter&&(icon.getY()>=blockTop&&icon.getY()<=blockBottom))
        {
            xDirection=Math.abs(xDirection);
        }

            yDirection = -1 * yDirection;
            timeOfLastHit = System.nanoTime();


    }
    public void paddleCollide(double paddleCenter,double paddleY){

            if(icon.getY()>paddleY){
                yDirection=-1*yDirection;
            }


    }
    public int getMyLives(){
        return myLives;
    }
    public void setLives(int lives){myLives=lives;}
    public long getTimeOfLastHit(){return timeOfLastHit;}
    public void setIndestructable(){indestructable=true;}

}
