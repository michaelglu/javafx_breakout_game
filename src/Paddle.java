import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Paddle {
    private double myHeight,myWidth,mySpeed;
    private ImageView myRectangle;
    private Image image;

    public Paddle(int sceneWidth, int sceneHeight)
    {
        myHeight=10;
        myWidth=75;
        mySpeed=20;
        image =new Image(this.getClass().getClassLoader().getResourceAsStream("paddle.gif"));
        myRectangle= new ImageView(image);
        myRectangle.setFitWidth(myWidth);
        myRectangle.setFitHeight(myHeight);
        myRectangle.setX(sceneWidth / 2 - myWidth/2);
        myRectangle.setY(sceneHeight-50);
    }
    public ImageView getPaddle(){return myRectangle;}
    // What to do each time a key is pressed
    public void handleKeyInput (KeyCode code, int sceneWidth) {
        if (code == KeyCode.RIGHT && (myRectangle.getX()+myRectangle.getFitWidth()/2)<sceneWidth-myWidth/2) {
            myRectangle.setX(myRectangle.getX() + mySpeed);
        }
        else if (code == KeyCode.LEFT&&(myRectangle.getX())>0) {
            myRectangle.setX(myRectangle.getX() - mySpeed);
        }

    }
    public void reset(int sceneWidth,int sceneHeight){
        myRectangle.setFitWidth(myWidth);
        myRectangle.setFitHeight(myHeight);
        myRectangle.setX(sceneWidth / 2 - myWidth/2);
        myRectangle.setY(sceneHeight-50);
    }

    //POWERUPS:
    public void grow(){
        myRectangle.setFitWidth(myRectangle.getFitWidth()*1.5);
    }
}
