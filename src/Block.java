import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Block {
   private int myLives;
   private int myWidth=40;
   private int myHeight=20;
   private long myLasthit;
   private boolean isVisible;
   private Powerup myPowerup;
   private Image[] images;
   private ImageView myRectangle;

    public Block(int xPosition,int yPosition,int lives){
        myLasthit=0;
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
    public boolean checkCollisions(Ball ball){
        if (getBlock().getBoundsInParent().intersects(ball.getIcon().getBoundsInParent())&&getVisibility()) {
            hit();
            ball.blockCollide(getBlock().getY(),getBlock().getY()+getBlock().getFitHeight(),getBlock().getX()+getBlock().getFitWidth(),getBlock().getX(),getBlock().getX()+getBlock().getFitWidth()/2,getBlock().getY()+getBlock().getFitHeight()/2);
            return true;
        }
        return false;
    }
    private void hit(){
        long current=System.nanoTime();//Delay ensures a block does not subtract lives twice per hit
        if(current>=myLasthit+34000000)//1sec/60fps=0.017sec=17,000,000 nanosecs, so wait an extra frame
        {
            myLives-=1;
            myLasthit=System.nanoTime();
        }
        if (myLives==0) {
            if(myPowerup!=null){
                myPowerup.letGo();
            }
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
    public Powerup setMyPowerup(Group group, String type,Ball ball, Paddle paddle){
                myPowerup=new Powerup(group,ball,paddle,type,getBlock().getX()+myWidth/2,getBlock().getY()+myHeight/2);
                return myPowerup;
    }
}
