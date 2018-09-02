import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Ball {
    private int xDirection;
    private int yDirection;
    private int mySpeed;
    private ImageView icon;
    private Image image;
    private boolean hitsEnabled;

    public Ball(int width,int height){
        hitsEnabled=true;
        xDirection=1;
        yDirection=1;
        mySpeed=0;
        image =new Image(this.getClass().getClassLoader().getResourceAsStream("ball.gif"));
        icon = new ImageView(image);
        icon.setX(width / 2 - icon.getBoundsInLocal().getWidth() / 2);
        icon.setY(height / 2 - icon.getBoundsInLocal().getHeight() / 2);
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
        if(icon.getY()>screenHeight-1||icon.getY()<1){

            yDirection=-1*yDirection;
        }

    }

    public void blockCollide (double blockTop,double blockBottom, double blockRight, double blockLeft)
    {
       if(icon.getX()==blockLeft||icon.getX()==blockRight)
       {
           xDirection=-1*xDirection;
       }
       yDirection=-1*yDirection;
       long current=System.nanoTime();

    }
    public void paddleCollide(double paddleCenter,double paddleY){
        if(icon.getX()<paddleCenter)
        {
            xDirection=-1*xDirection;
        }
        if(icon.getY()>paddleY){
            yDirection=-1*yDirection;
        }



    }
    public boolean getHitsEnabled(){
        return hitsEnabled;
    }
    public void setHitsEnabled(boolean bool){
        hitsEnabled=bool;
    }

}
