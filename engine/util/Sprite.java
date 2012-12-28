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
        startFrame=0;
        endFrame=0;
        type=AnimationType.NORMAL;
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
    public ArrayList animationSet;

    public AnimationData() {
        name="";
        frameWidth=0; frameHeight=0;
        image=null;
        animationSet=new ArrayList();
    }
}

//Exception subclass to handle Sprite exceptions
final class SpriteException extends RuntimeException {
    private Sprite sprite;

    public SpriteException(Sprite s, String msg) {
        super(msg);
        setSprite(s);
    }

    //sprite modifier method
    public void setSprite(Sprite s) {
        sprite=s;
    }

    //sprite accessor method
    public Sprite getSprite() {
        return sprite;
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
        posX=0; posY=0;
        frameNum=0; animationNum=0;
        animation=null;
        paused=false;
        animationFactor=1;
        drawNum=1;
        alpha=1.0;
    }

    //constructor with sprite parameter to clone
    public Sprite(Sprite s) {
        this();
        clone(s);
    }

    public Sprite(String name, BufferedImage img, int frameWidth, int frameHeight)
    throws SpriteException {
        this();
        create(name,img,frameWidth,frameHeight);
    }

    public Sprite(String name, InputStream input, int frameWidth, int frameHeight)
    throws SpriteException, IOException {
        this();
        create(name,input,frameWidth,frameHeight);
    }

    public Sprite(String name, URL input, int frameWidth, int frameHeight)
    throws SpriteException, IOException {
        this();
        create(name,input,frameWidth,frameHeight);
    }

    //clones a sprite's animation data
    //thus two sprites with different properties (position, transparency)
    //can share the same animation data
    public void clone(Sprite s) {
        animation=s.animation;
        //reset frame and animation to prevent overflow
        //(ex. currentAnim is 5 when theres only 3 animations)
        frameNum=0; animationNum=0;
    }

    //creates new animation data
    public void create(String name, BufferedImage img, int frameWidth, int frameHeight)
    throws SpriteException {
        if (img.getWidth(null) % frameWidth == 0 &&
        img.getHeight(null) % frameHeight == 0) {
            Animation a;
            animation=new AnimationData();
            animation.image=img;
            animation.frameWidth=frameWidth;
            animation.frameHeight=frameHeight;
            animation.maxFrames=img.getWidth(null)/frameWidth;
            animation.maxAnimations=img.getHeight(null)/frameHeight;
            for (int i=1; i<=animation.maxAnimations; i++) {
                a=new Animation();
                a.startFrame=0;
                a.endFrame=animation.maxFrames-1;
                a.type=AnimationType.NORMAL;
                animation.animationSet.add(a);
            }
        }
        else
            throw new SpriteException(this, "Frame dimension incompatible with image");
    }

    public void create(String name, InputStream input, int frameWidth, int frameHeight)
    throws SpriteException, IOException {
        //convert input stream to image
        BufferedImage img=ImageIO.read(input);
        create(name,img,frameWidth,frameHeight);
    }

    public void create(String name, URL input, int frameWidth, int frameHeight)
    throws SpriteException, IOException {
        //convert input stream to image
        BufferedImage img=ImageIO.read(input);
        create(name,img,frameWidth,frameHeight);
    }

    //draws the sprite frame;
    //Note: tx, ty, tw, th is the clipping rectangle for the frame
    public void draw(Graphics2D g, boolean autoUpdate, int tx, int ty, int tw, int th)
    throws SpriteException {
        if (animation != null) {
            int dx1, dy1, dx2, dy2; //rectangle that marks where to draw on the screen
            int sx1, sy1, sx2, sy2; //rectangle that marks what part of the image to draw
            //screen coordinates = sprite's position + clipping offset
            dx1=posX+tx; dy1=posY+ty;
            dx2=dx1+tw; dy2=dy1+th;
            //source image coordinates are determined
            //by the animation and frame number and clipping offset
            sx1=(frameNum*animation.frameWidth)+tx;
            sy1=(animationNum*animation.frameHeight)+ty;
            sx2=sx1+tw; sy2=sy1+th;
            //save the original composition, add alpha transparency, draw,
            //then return to the original composition
            Composite originalComp=g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha));
            g.drawImage(animation.image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
            g.setComposite(originalComp);

            if (autoUpdate) update();
        }
        else
            throw new SpriteException(this, "Sprite does not have animation data");

    }
    
    //draw sprite without a clipping rectangle
    public void draw(Graphics2D g, boolean autoUpdate) throws SpriteException {
        if (animation != null)
            draw(g, autoUpdate, 0, 0, animation.frameWidth, animation.frameHeight);
        else
            throw new SpriteException(this, "Sprite does not have animation data");
    }

    //draw sprite without a clipping rectangle and update it
    public void draw(Graphics2D g) throws SpriteException {
        if (animation != null)
            draw(g, true, 0, 0, animation.frameWidth, animation.frameHeight);
        else
            throw new SpriteException(this, "Sprite does not have animation data");
    }

    //alpha accessor
    public double getAlpha() {
        return alpha;
    }

    //position accessors
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getWidth() {
        return animation.frameWidth;
    }

    public int getHeight() {
        return animation.frameHeight;
    }

    public boolean isPaused() {
        return paused;
    }

    //pauses the animation
    public void pause() {
        paused=true;
    }

    //...guess what this one does?
    public void resume() {
        paused=false;
    }

    //alpha modifier
    public void setAlpha(double a) throws SpriteException {
        if (a >= 0.0 && a <= 1.0) {
            alpha=a;
        }
        else
            throw new SpriteException(this, "Alpha value must be between 0.0 and 1.0");
    }

    //animation modifier
    public void setAnimation(int a) throws SpriteException {
        if (a >=0 && a < animation.animationSet.size()) {
            animationNum=a;
        }
        else
            throw new SpriteException(this, "Animation number out of index bounds");
    }

    //sets animation type, startFrame, and endFrame
    public void setAnimationProperties(int a, AnimationType type,
    int frameStart, int frameEnd) throws SpriteException {
        Animation anim=getAnimationByIndex(a);
        anim.type=type;
        if (frameStart < frameEnd) {
            if (frameStart >= 0 && frameEnd < animation.maxFrames) {
                anim.startFrame=frameStart;
                anim.endFrame=frameEnd;
            }
            else
                throw new SpriteException(this, "frameStart and/or frameEnd out of index bounds");
        }
        else
            throw new SpriteException(this, "frameStart must be before frameEnd");
    }

    //frameNum modifier
    public void setFrame(int f) throws SpriteException {
        Animation anim=getAnimationByIndex(animationNum);
        if (f >= anim.startFrame && f <= anim.endFrame)
            frameNum=f;
        else
            throw new SpriteException(this, "Frame number out of index bounds");
    }

    //animationFactor modifier; argument is number of updates between each frame
    public void setAnimationFactor(int factor) throws SpriteException {
        if (factor > 0)
            animationFactor=factor;
        else
            throw new SpriteException(this, "Animation factor must be greater than 0");
    }
    
    //animationFactor modifier; argument is the percentage the animation slows down
    public void setAnimationFactor(double factor) throws SpriteException {
        if (factor > 0.0) {
            animationFactor=(int)(1.0/factor);
        }
        else
            throw new SpriteException(this, "Animation factor must be greater than 0");
    }

    //position modifier
    public void setPosition(int x, int y) {
        posX=x;
        posY=y;
    }

    public void setPosX(int x) {
        posX=x;
    }

    public void setPosY(int y) throws SpriteException {
        posY=y;
    }

    //returns animation object using array index
    private Animation getAnimationByIndex(int a) throws SpriteException {
        if (a >=0 && a < animation.animationSet.size()) {
            return (Animation)animation.animationSet.get(a);
        }
        else
            throw new SpriteException(this, "Animation number out of index bounds");
    }

    //called every update cycle
    private void update() throws SpriteException {
        //time to update the frame
        if (drawNum >= animationFactor) {
            Animation anim=getAnimationByIndex(animationNum);
            if (anim.type == AnimationType.NORMAL) {
                //update ONLY when the sprite is not paused
                if (paused==false) {
                    frameNum++;
                    if (frameNum >= anim.endFrame)
                        frameNum=anim.startFrame;
                }
            }
            //animation type is ONE_SHOT
            else {
                //update ONLY when the sprite is not paused
                if (paused==false) {
                    frameNum++;
                    //once one animation cycle is done, pause the sprite
                    if (frameNum >= anim.endFrame)
                        paused=true;
                }
            }

            drawNum=1;
        }
        //else, increment the update counter
        else
            drawNum++;
    }
}
