import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {
    private int myLives;
    private int myWidth=45;
    private int myHeight=20;
    private boolean isVisible;
   // private Rectangle myRectangle;
   private Image[] images;
    private ImageView myRectangle;
    private Image image;
    public Block(int xPosition,int yPosition,int lives){
        images=new Image[3];
        images[0]=new Image(this.getClass().getClassLoader().getResourceAsStream("brick1.gif"));
        images[1]=new Image(this.getClass().getClassLoader().getResourceAsStream("brick2.gif"));
        images[2]=new Image(this.getClass().getClassLoader().getResourceAsStream("brick3.gif"));
        myLives=lives;
        isVisible=true;
        myRectangle=new ImageView(images[lives-1]);
        myRectangle.setFitHeight(myHeight);
        myRectangle.setFitWidth(myWidth);
        myRectangle.setX(xPosition*50);
        myRectangle.setY(100+yPosition*25);

    }
    public ImageView getBlock(){return myRectangle;}
    public void hit(){
        myLives-=1;
        if (myLives==0)
        {
            isVisible=false;
        }
        else{
        myRectangle.setImage(images[myLives-1]);
        }

    }
    public int getMyLives(){
        return myLives;
    }
    public boolean getVisibility(){
        return isVisible;
    }
}
