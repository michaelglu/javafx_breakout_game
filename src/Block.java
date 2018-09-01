import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
    private int myLives;
    private int myWidth=45;
    private int myHeight=20;
    private boolean isVisible;
    private Rectangle myRectangle;
    public Block(int xPosition,int yPosition){//location might need tweaking
        myLives=3;
        isVisible=true;
        myRectangle=new Rectangle(xPosition*(50),100+yPosition*25, myWidth, myHeight);
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
        else if (myLives==0)
        {
            isVisible=false;
        }
    }
    public int getMyLives(){
        return myLives;
    }
    public boolean getVisibility(){
        return isVisible;
    }
}
