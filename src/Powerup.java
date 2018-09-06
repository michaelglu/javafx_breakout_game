import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Powerup {
    private Image myImage;
    private ImageView myIcon;
    private String myType;
    private Paddle myPaddle;
    private Ball myBall;
    private int mySpeed;
    private Group myGroup;
    private boolean isVisible;
    //1-increase paddle size; 2-paddle increases ball speed; 3-extra life
    public Powerup(Group group,Ball ball, Paddle paddle, String type, double x, double y)
    {
        isVisible=true;
        mySpeed=0;
        myBall=ball;
        myType=type;
        myPaddle=paddle;
        myGroup=group;
        if (myType=="lives")
        {
            myImage=new Image(this.getClass().getClassLoader().getResourceAsStream("life-power.gif"));
        }
        else if(myType=="size")
        {
            myImage=new Image(this.getClass().getClassLoader().getResourceAsStream("paddle_size_power.gif"));
        }
        else if(myType=="speedy")
        {
            myImage=new Image(this.getClass().getClassLoader().getResourceAsStream("speed-paddle-power.gif"));
        }
        myIcon=new ImageView(myImage);
        myIcon.setFitWidth(10);
        myIcon.setFitHeight(10);
        myIcon.setX(x-5);
        myIcon.setY(y-5);
        myGroup.getChildren().add(myIcon);

    }
    public void letGo(){
        mySpeed=60;
    }
    public void go(double elapsedTime){
        myIcon.setY(myIcon.getY()+mySpeed*elapsedTime);
        if (myIcon.getBoundsInParent().intersects(myPaddle.getPaddle().getBoundsInParent())&&isVisible){
            paddleCollide();
        }

    }

    public void paddleCollide(){
     if(myType=="size")
     {
         myPaddle.grow();
     }
     else if(myType=="speedy")
     {
         myBall.setSpeedy();
     }
     else if(myType=="lives")
     {
         myBall.addLives(1);
     }
     isVisible=false;
     myGroup.getChildren().remove(myIcon);
    }

}
