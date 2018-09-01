import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
    private int myLives;
    private int myWidth=50;
    private int myHeight=30;
    private Rectangle myRectangle;
    public Block(int position){//location might need tweaking
        myLives=3;
        myRectangle=new Rectangle(50+position*(55),30, myWidth, myHeight);
        myRectangle.setFill(Color.RED);
    }
    public Rectangle getBlock(){return myRectangle;}
    public void hit(){
        myLives--;
        if (myLives==2)
        {
            myRectangle.setFill(Color.YELLOW);
        }
        else  if (myLives==1)
        {
            myRectangle.setFill(Color.ORANGE);
        }
    }
    public int getMyLives(){
        return myLives;
    }
}
