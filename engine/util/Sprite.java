// Sprite.java

package engine.util;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

//class that describes one animation of a sprite
final class Animation {
    public int start_frame;
    public int end_frame;

    public Animation() {
        this.start_frame=0;
        this.end_frame=0;
    }
    
    public Animation(int start, int end) {
        this.start_frame=start;
        this.end_frame=end;
    }
}

//class that includes the sprite image and the set of the sprite's animations
final class AnimationData {
    public int frame_width; //how wide each frame is
    public int frame_height; //how tall each frame is
    public int max_frames; //number of frames an animation can have (imageWidth/offX)
    public int max_animations; //number of possible animations (imageHeight/offY)
    public BufferedImage image;
    public ArrayList<Animation> animation_list;

    public AnimationData() {
        this.frame_width=0;
        this.frame_height=0;
        this.image=null;
        this.animation_list=new ArrayList();
    }
    
    //save animation data
    public void save(OutputStream out) throws IOException {
    	out.write(this.frame_width);
    	out.write(this.frame_height);
    	out.write(this.max_frames);
    	out.write(this.max_animations);
    	out.write(this.animation_list.size());
    	for (Animation anim : this.animation_list) {
    		out.write(anim.start_frame);
    		out.write(anim.end_frame);
    	}
    	
    	ImageIO.write(this.image, "png", out);
    }
    
    //load animation data
    public void load(InputStream in) throws IOException {
    	this.frame_width = in.read();
    	this.frame_height = in.read();
    	this.max_frames = in.read();
    	this.max_animations = in.read();
    	
    	int anim_size = in.read();
    	this.animation_list.clear();
    	for (int i=0; i<anim_size; i++) {
    		Animation a = new Animation();
    		a.start_frame = in.read();
    		a.end_frame = in.read();
    		this.animation_list.add(a);
    	}
    	
    	this.image = ImageIO.read(in);
    	
    }
}

/* A class that encapsulates an image
 * and includes drawing properties (position, alpha transparency, etc).
 * The image is divided into a grid of frames, and then a specific frame
 * of animation is drawn for each update.
 */
public class Sprite implements Serializable {
    protected int pos_x, pos_y; //position
    protected int frame_num; //current animation frame; horizontal offset
    protected int animation_num; //current animation; vertical offset
    protected AnimationData animation;
    protected boolean paused; //is animation paused?
    //number of updates before moving to the next frame; sets speed of animation
    protected int animation_factor;
    protected int draw_num; //number of updates since last frame update
    protected double alpha; //transparency; 1.0 = opaque, 0.0 = transparency

    //constructor
    protected Sprite() {
        this.pos_x=0;
        this.pos_y=0;
        this.frame_num=0;
        this.animation_num=0;
        this.animation=null;
        this.paused=false;
        this.animation_factor=1;
        this.draw_num=1;
        this.alpha=1.0;
    }

    //constructor with sprite parameter to clone
    public Sprite(Sprite s) {
        this();
        this.clone(s);
    }

    public Sprite(BufferedImage img, int frameWidth, int frameHeight) {
        this();
        this.create(img,frameWidth,frameHeight);
    }

    public Sprite(InputStream input, int frameWidth, int frameHeight)
    throws IOException {
        this();
        this.create(input, frameWidth, frameHeight);
    }
    
    public Sprite(InputStream input) throws ClassNotFoundException, IOException {
    	this();
    	this.animation = new AnimationData();
    	this.load(input);
    }
    
    public Sprite(String file) throws ClassNotFoundException, IOException {
    	this();
    	this.animation = new AnimationData();
    	this.load(file);
    }

    //clones a sprite's animation data
    //thus two sprites with different properties (position, transparency)
    //can share the same animation data
    public void clone(Sprite s) {
        this.animation=s.animation;
        //reset frame and animation to prevent overflow
        //(ex. currentAnim is 5 when theres only 3 animations)
        this.frame_num=0;
        this.animation_num=0;
    }
    
    //clones THIS sprite;
    //this is the reverse operation of clone(spr)
    public Sprite clone() {
    	Sprite spr = new Sprite(this);
    	return spr;
    }

    //creates new animation data
    public void create(BufferedImage img, int frameWidth, int frameHeight) {
        if ((img.getWidth(null) % frameWidth != 0) || (img.getHeight(null) % frameHeight != 0)) {
        	throw new IllegalArgumentException("Sprite: Frame dimension incompatible with image");
        }
        
        Animation a;
        this.animation = new AnimationData();
        this.animation.image = img;
        this.animation.frame_width = frameWidth;
        this.animation.frame_height = frameHeight;
        this.animation.max_frames = img.getWidth(null)/frameWidth;
        this.animation.max_animations = img.getHeight(null)/frameHeight;
        for (int i=1; i<=this.animation.max_animations; i++) {
            a = new Animation(0, this.animation.max_frames-1);
            this.animation.animation_list.add(a);
        }
    }

    public void create(InputStream input, int frameWidth, int frameHeight)
    throws IOException {
        //convert input stream to image
        BufferedImage img=ImageIO.read(input);
        this.create(img,frameWidth,frameHeight);
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
        dx1=this.pos_x+tx; dy1=this.pos_y+ty;
        dx2=dx1+tw; dy2=dy1+th;
        //source image coordinates are determined
        //by the animation and frame number and clipping offset
        sx1=(this.frame_num*this.animation.frame_width)+tx;
        sy1=(this.animation_num*this.animation.frame_height)+ty;
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
        
        this.draw(g, autoUpdate, 0, 0, animation.frame_width, animation.frame_height);
    }

    //draw sprite without a clipping rectangle and update it
    public void draw(Graphics2D g) {
        if (this.animation == null) {
        	throw new RuntimeException("Sprite: Sprite has not been created nor cloned");
        }
        
        this.draw(g, true, 0, 0, animation.frame_width, animation.frame_height);
    }

    //alpha accessor
    public double getAlpha() {
        return this.alpha;
    }

    //position accessors
    public int getPosX() {
        return this.pos_x;
    }

    public int getPosY() {
        return this.pos_y;
    }

    public int getWidth() {
        return this.animation.frame_width;
    }

    public int getHeight() {
        return this.animation.frame_height;
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
        if (a < 0 || a >= this.animation.animation_list.size()) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: Animation number out of index bounds");
        }
        
        this.animation_num=a;
    }

    //sets animation type, startFrame, and endFrame
    public void setAnimationProperties(int a, int frameStart, int frameEnd) {
        if (frameStart >= frameEnd) {
        	throw new IllegalArgumentException("Sprite: frameStart must be before frameEnd");
        }
        if (frameStart < 0 || frameEnd >= this.animation.max_frames) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: frameStart and/or frameEnd out of index bounds");
        }

        Animation anim=this.getAnimationByIndex(a);
        anim.start_frame=frameStart;
        anim.end_frame=frameEnd;
    }

    //frameNum modifier
    public void setFrame(int f) {
        Animation anim=getAnimationByIndex(this.animation_num);
        
        if (f < anim.start_frame || f > anim.end_frame) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: Frame number out of index bounds");
        }
        
        this.frame_num=f;
    }

    //animationFactor modifier; argument is number of updates between each frame
    public void setAnimationFactor(int factor) {
        if (factor <= 0) {
        	throw new IllegalArgumentException("Sprite: Animation factor must be greater than 0");
        }
        
        this.animation_factor=factor;
    }
    
    //animationFactor modifier; argument is the percentage the animation slows down
    public void setAnimationFactor(double factor) {
        if (factor <= 0.0) {
        	throw new IllegalArgumentException("Sprite: Animation factor must be greater than 0");
        }
        
        this.animation_factor=(int)(1.0/factor);
    }

    //position modifier
    public void setPosition(int x, int y) {
        this.pos_x=x;
        this.pos_y=y;
    }

    public void setPosX(int x) {
        this.pos_x=x;
    }

    public void setPosY(int y) {
        this.pos_y=y;
    }

    //returns animation object using array index
    private Animation getAnimationByIndex(int a) {
        if (a < 0 || a >= this.animation.animation_list.size()) {
        	throw new ArrayIndexOutOfBoundsException("Sprite: Animation number out of index bounds");
        }
        
        return (Animation)this.animation.animation_list.get(a);
    }

    //called every update cycle
    public void update() {
        //time to update the frame
        if (this.draw_num >= this.animation_factor) {
            Animation anim=getAnimationByIndex(this.animation_num);
            //update ONLY when the sprite is not paused
            if (this.paused==false) {
                this.frame_num++;
                if (this.frame_num >= anim.end_frame)
                    this.frame_num = anim.start_frame;
            }

            this.draw_num=1;
        }
        //else, increment the update counter
        else
            this.draw_num++;
    }
    
    //save sprite object to a stream
    public void save(OutputStream out) throws IOException {
    	this.animation.save(out);
    	
    	out.write(this.animation_factor);
    	out.write((int)(this.alpha*255));
    }
    
    //save sprite object to a file
	public void save(String file) throws FileNotFoundException, IOException {
    	FileOutputStream out = new FileOutputStream(file);
    	try {
	    	this.save(out);
    	}
    	catch (IOException e) {
    		out.close();
    		throw e;
    	}
    	finally {
	    	out.close();
    	}
    }
	
	//load sprite from a stream
	public void load(InputStream in) throws IOException {
		this.animation.load(in);
		
    	this.animation_factor = in.read();
    	this.alpha = (double)(in.read())/255.0;
	}
	
	//load the sprite from a file
	public void load(String file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			this.load(in);
		}
		catch (IOException e) {
			System.out.println("NOOOo");
			in.close();
			throw e;
		}
		finally {
			in.close();
		}
	}
}
