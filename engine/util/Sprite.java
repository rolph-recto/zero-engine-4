// Sprite.java

package engine.util;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

enum AnimationType {
    ONE_SHOT, //cycles through frames only once; ex. 1 2 3 4 5
    NORMAL //goes back to beginning when finished; ex. 1 2 3 4 5 1 2 3 4 5
}

//class that describes one animation of a sprite
class Animation {
    public int startFrame;
    public int endFrame;
    public AnimationType type;

    public Animation() {
        this.startFrame=0;
        this.endFrame=0;
        this.type=AnimationType.NORMAL;
    }
}

//class that includes the sprite image and the set of the sprite's animations
final class AnimationData {
    public String name;
    public int frameWidth; //how wide each frame is
    public int frameHeight; //how tall each frame is
    public int maxFrames; //number of frames an animation can have (imageWidth/offX)
    public int maxAnimations; //number of possible animations (imageHeight/offY)
    public BufferedImage image;
    public ArrayList<Animation> animationSet;

    public AnimationData() {
        this.name="";
        this.frameWidth=0;
        this.frameHeight=0;
        this.image=null;
        this.animationSet=new ArrayList();
    }
}

/* A class that encapsulates an image
 * and includes drawing properties (position, alpha transparency, etc).
 * The image is divided into a grid of frames, and then a specific frame
 * of animation is drawn for each update.
 */
public class Sprite {
    protected int posX, posY; //position
    protected int frameNum; //current animation frame; horizontal offset
    protected int animationNum; //current animation; vertical offset
    protected AnimationData animation;
    protected boolean paused; //is animation paused?
    //number of updates before moving to the next frame; sets speed of animation
    protected int animationFactor;
    protected int drawNum; //number of updates since last frame update
    protected double alpha; //transparency; 1.0 = opaque, 0.0 = transparency

    //constructor
    public Sprite() {
        this.posX=0;
        this.posY=0;
        this.frameNum=0;
        this.animationNum=0;
        this.animation=null;
        this.paused=false;
        this.animationFactor=1;
        this.drawNum=1;
        this.alpha=1.0;
    }

    //constructor with sprite parameter to clone
    public Sprite(Sprite s) {
        this();
        this.clone(s);
    }

    public Sprite(String name, BufferedImage img, int frameWidth, int frameHeight) {
        this();
        this.create(name,img,frameWidth,frameHeight);
    }

    public Sprite(String name, InputStream input, int frameWidth, int frameHeight)
    throws IOException {
        this();
        this.create(name,input,frameWidth,frameHeight);
    }

    public Sprite(String name, URL input, int frameWidth, int frameHeight)
    throws IOException {
        this();
        this.create(name,input,frameWidth,frameHeight);
    }

    //clones a sprite's animation data
    //thus two sprites with different properties (position, transparency)
    //can share the same animation data
    public void clone(Sprite s) {
        this.animation=s.animation;
        //reset frame and animation to prevent overflow
        //(ex. currentAnim is 5 when theres only 3 animations)
        this.frameNum=0;
        this.animationNum=0;
    }
    
    //clones THIS sprite;
    //this is the reverse operation of clone(spr)
    public Sprite clone() {
    	Sprite spr = new Sprite(this);
    	return spr;
    }

    //creates new animation data
    public void create(String name, BufferedImage img, int frameWidth, int frameHeight) {
        if ((img.getWidth(null) % frameWidth != 0) || (img.getHeight(null) % frameHeight != 0)) {
        	throw new IllegalArgumentException("Sprite: Frame dimension incompatible with image");
        }
        
        Animation a;
        this.animation = new AnimationData();
        this.animation.image = img;
        this.animation.frameWidth = frameWidth;
        this.animation.frameHeight = frameHeight;
        this.animation.maxFrames = img.getWidth(null)/frameWidth;
        this.animation.maxAnimations = img.getHeight(null)/frameHeight;
        for (int i=1; i<=this.animation.maxAnimations; i++) {
            a = new Animation();
            a.startFrame = 0;
            a.endFrame = this.animation.maxFrames-1;
            a.type = AnimationType.NORMAL;
            this.animation.animationSet.add(a);
        }
    }

    public void create(String name, InputStream input, int frameWidth, int frameHeight)
    throws IOException {
        //convert input stream to image
        BufferedImage img=ImageIO.read(input);
        this.create(name,img,frameWidth,frameHeight);
    }

    public void create(String name, URL input, int frameWidth, int frameHeight)
    throws IOException {
        //convert input stream to image
        BufferedImage img=ImageIO.read(input);
        this.create(name,img,frameWidth,frameHeight);
    }

    //draws the sprite frame;
    //Note: tx, ty, tw, th is the clipping rectangle for the frame
    public void draw(Graphics2D g, boolean autoUpdate, int tx, int ty, int tw, int th) {
        if (this.animation == null) {
        	throw new RuntimeException("Sprite: Sprite has not been created nor cloned");
        }
        
        int dx1, dy1, dx2, dy2; //rectangle that marks where to draw on the screen
        int sx1, sy1, sx2, sy2; //rectangle that marks what part of the image to draw
        //screen coordinates = sprite's position + clipping offset
        dx1=this.posX+tx; dy1=this.posY+ty;
        dx2=dx1+tw; dy2=dy1+th;
        //source image coordinates are determined
        //by the animation and frame number and clipping offset
        sx1=(this.frameNum*this.animation.frameWidth)+tx;
        sy1=(this.animationNum*this.animation.frameHeight)+ty;
        sx2=sx1+tw; sy2=sy1+th;
        //save the original composition, add alpha transparency, draw,
        //then return to the original composition
        Composite originalComp=g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)this.alpha));
        g.drawImage(this.animation.image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        g.setComposite(originalComp);

        if (autoUpdate) update();

    }
    
    //draw sprite without a clipping rectangle
    public void draw(Graphics2D g, boolean autoUpdate) {
        if (this.animation == null) {
        	throw new RuntimeException("Sprite: Sprite has not been created nor cloned");
        }
        
        this.draw(g, autoUpdate, 0, 0, animation.frameWidth, animation.frameHeight);
    }

    //draw sprite without a clipping rectangle and update it
    public void draw(Graphics2D g) {
        if (this.animation == null) {
        	throw new RuntimeException("Sprite: Sprite has not been created nor cloned");
        }
        
        this.draw(g, true, 0, 0, animation.frameWidth, animation.frameHeight);
    }

    //alpha accessor
    public double getAlpha() {
        return this.alpha;
    }

    //position accessors
    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getWidth() {
        return this.animation.frameWidth;
    }

    public int getHeight() {
        return this.animation.frameHeight;
    }

    public boolean isPaused() {
        return this.paused;
    }

    //pauses the animation
    public void pause() {
        this.paused=true;
    }

    //...guess what this one does?
    public void resume() {
        this.paused=false;
    }

    //alpha modifier
    public void setAlpha(double a) {
        if (a < 0.0 || a > 1.0) {
        	throw new IllegalArgumentException("Sprite: Alpha value must be between 0.0 and 1.0");
        }
        
        this.alpha=a;
    }

    //animation modifier
    public void setAnimation(int a) {
        if (a < 0 || a >= this.animation.animationSet.size()) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: Animation number out of index bounds");
        }
        
        this.animationNum=a;
    }

    //sets animation type, startFrame, and endFrame
    public void setAnimationProperties(int a, AnimationType type, int frameStart, int frameEnd) {
        if (frameStart >= frameEnd) {
        	throw new IllegalArgumentException("Sprite: frameStart must be before frameEnd");
        }
        if (frameStart < 0 || frameEnd >= this.animation.maxFrames) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: frameStart and/or frameEnd out of index bounds");
        }

        Animation anim=this.getAnimationByIndex(a);
        anim.type=type;
        anim.startFrame=frameStart;
        anim.endFrame=frameEnd;
    }

    //frameNum modifier
    public void setFrame(int f) {
        Animation anim=getAnimationByIndex(this.animationNum);
        
        if (f < anim.startFrame || f > anim.endFrame) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: Frame number out of index bounds");
        }
        
        this.frameNum=f;
    }

    //animationFactor modifier; argument is number of updates between each frame
    public void setAnimationFactor(int factor) {
        if (factor <= 0) {
        	throw new IllegalArgumentException("Sprite: Animation factor must be greater than 0");
        }
        
        this.animationFactor=factor;
    }
    
    //animationFactor modifier; argument is the percentage the animation slows down
    public void setAnimationFactor(double factor) {
        if (factor <= 0.0) {
        	throw new IllegalArgumentException("Sprite: Animation factor must be greater than 0");
        }
        
        this.animationFactor=(int)(1.0/factor);
    }

    //position modifier
    public void setPosition(int x, int y) {
        this.posX=x;
        this.posY=y;
    }

    public void setPosX(int x) {
        this.posX=x;
    }

    public void setPosY(int y) {
        this.posY=y;
    }

    //returns animation object using array index
    private Animation getAnimationByIndex(int a) {
        if (a < 0 || a >= this.animation.animationSet.size()) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: Animation number out of index bounds");
        }
        
        return (Animation)this.animation.animationSet.get(a);
    }

    //called every update cycle
    private void update() {
        //time to update the frame
        if (this.drawNum >= this.animationFactor) {
            Animation anim=getAnimationByIndex(this.animationNum);
            //animation type is NORMAL
            if (anim.type == AnimationType.NORMAL) {
                //update ONLY when the sprite is not paused
                if (this.paused==false) {
                    this.frameNum++;
                    if (this.frameNum >= anim.endFrame)
                        this.frameNum = anim.startFrame;
                }
            }
            //animation type is ONE_SHOT
            else {
                //update ONLY when the sprite is not paused
                if (this.paused==false) {
                    this.frameNum++;
                    //once one animation cycle is done, pause the sprite
                    if (this.frameNum >= anim.endFrame)
                        this.paused=true;
                }
            }

            this.drawNum=1;
        }
        //else, increment the update counter
        else
            this.drawNum++;
    }
}
