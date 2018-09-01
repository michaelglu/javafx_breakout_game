import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.awt.*;

public class Paddle {
    private int myHeight,myWidth,mySpeed;
    private Rectangle myRectangle;

    public Paddle(int sceneWidth, int sceneHeight)
    {
        myHeight=10;
        myWidth=50;
        mySpeed=10;
        myRectangle=new Rectangle(sceneWidth / 2 - 25, sceneHeight-50, myWidth, myHeight);
        myRectangle.setFill(Color.PLUM);

    }
    public Rectangle getPaddle(){return myRectangle;}
    // What to do each time a key is pressed
    public void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            myRectangle.setX(myRectangle.getX() + mySpeed);
        }
        else if (code == KeyCode.LEFT) {
            myRectangle.setX(myRectangle.getX() - mySpeed);
        }

    }
}
