/**
 * CS349 Winter 2014
 */
package com.example.a4;
import android.graphics.*;
import android.util.Log;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
public class Fruit{
    private Path path = new Path();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix transform = new Matrix();
    private Path stroke = new Path();
    public float   fallingNumber;
    public float   expandNumber;
    public float   droppingNumber;
    public float   counter;


    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
    Fruit(float[] points) {
        init();
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);

        }
        this.path.moveTo(points[0], points[1]);
    }

    Fruit(Region region) {
        init();
        this.path = region.getBoundaryPath();
    }

    Fruit(Path path) {
        init();
        this.path = path;
    }

    private void init() {
        this.paint.setColor(Color.BLUE);
        this.paint.setStrokeWidth(5);
        fallingNumber =randomWithRange(-10,10);
        expandNumber  = randomWithRange(1,3);
        droppingNumber = randomWithRange(5,9);
        counter=0;
    }

    public void move()
    {
        this.translate((float)(fallingNumber*0.2),(float)(0.057741935*2*(counter*counter)/expandNumber-11.43));
        counter+=0.23;
        //System.out.print(counter+" ");
    }
    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }

    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
        canvas.drawPath(getTransformedPath(), paint);
        // TODO END CS349
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */

    public boolean intersects(PointF p1, PointF p2) {
        // TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in 
        // rotate path and create region for comparison
        if(contains(p1)||contains(p2))
            return false;
        stroke.reset();
        if(p1.y==p2.y)
        {
            stroke.moveTo(p1.x,p1.y);
            stroke.lineTo(p1.x + 1, p1.y+1);
            stroke.lineTo(p2.x+1, p2.y+1);
            stroke.lineTo(p2.x, p2.y);
        }
        else
        {
            stroke.moveTo(p1.x,p1.y);
            stroke.lineTo((float)(p1.x + 0.5), p1.y);
            stroke.lineTo((float)(p2.x+0.5), p2.y);
            stroke.lineTo(p2.x, p2.y);
            stroke.close();
        }
        Log.d("Fruit","stroke is "+p1.toString()+p2.toString());

        Region temp=new Region();
        Region temp2=new Region();
        temp.setPath(stroke,new Region(0,0,MainActivity.displaySize.x,MainActivity.displaySize.y));
        temp2.setPath(getTransformedPath(),new Region(0,0,MainActivity.displaySize.x,MainActivity.displaySize.y));
        return (temp2.op(temp, Region.Op.INTERSECT));

        //return false;


        // TODO END CS349
        //return false;
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        boolean valid = region.setPath(getTransformedPath(), new Region(0,0,MainActivity.displaySize.x,MainActivity.displaySize.y));
        return valid && region.contains((int) p1.x, (int) p1.y);
    }

    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */

    int randomWithRange(int min, int max) //helper function
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public Fruit[] split(PointF p1, PointF p2) {
       // Log.d("Fruit","in split");
    	Path topPath = null;
    	Path bottomPath = null;
        Region topArea=new Region();
        Region bottomArea=new Region();
        Region fruit=new Region();

        if(p1.x<p2.x)
        {
            PointF temp;
            temp=p2;
            p2=p1;
            p1=temp;


        }

    	// TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in
        // rotate region
        // define region masks and use to split region into top and bottom
        Path temp= new Path(); // use point to construct a rect
        temp.reset();
        if(p1.y<=p2.y)
        {
            if(Math.abs(p1.y-p2.y)<50)
            {
                temp.moveTo(p1.x, p1.y);
                temp.lineTo(p1.x + 80, p1.y+120);
                temp.lineTo(p2.x-100,p2.y+120);
                temp.lineTo(p2.x,p2.y);
                temp.lineTo(p1.x,p1.y);

            }
            else
           {
                temp.moveTo(p1.x, p1.y);
                temp.lineTo(p1.x + 80, p1.y+120);
                temp.lineTo(p2.x-80,p2.y+120);
                temp.lineTo(p2.x,p2.y);
                temp.lineTo(p1.x,p1.y);
            }
        }
        else
        {
            if(Math.abs(p1.y-p2.y)<50)
            {
                temp.moveTo(p1.x, p1.y);
                temp.lineTo(p1.x + 20, p1.y+120);
                temp.lineTo(p2.x-20,p2.y+120);
                temp.lineTo(p2.x,p2.y);
                temp.lineTo(p1.x,p1.y);

            }
            else
            {
                temp.moveTo(p1.x, p1.y);
                temp.lineTo(p1.x + 80, p1.y+120);
                temp.lineTo(p2.x-80,p2.y+120);
                temp.lineTo(p2.x,p2.y);
                temp.lineTo(p1.x,p1.y);
            }

        }
        temp.close();
        bottomArea.setPath(temp, new Region(0, 0, MainActivity.displaySize.x, MainActivity.displaySize.y)); // set bottomArea to that rect
        fruit.setPath(getTransformedPath(),new Region(0,0,MainActivity.displaySize.x,MainActivity.displaySize.y)); //change fruit to region
        bottomArea.op(fruit, Region.Op.INTERSECT);  //intersect rect and fruit
        topArea.setPath(getTransformedPath(),new Region(0,0,MainActivity.displaySize.x,MainActivity.displaySize.y)); //change toparea to be the fruit
        topArea.op(bottomArea, Region.Op.DIFFERENCE);  // subtract bottomarea from fruit
        //bottomPath.op(bottomPath, Path.Op.INTERSECT);
        topPath=new Path(topArea.getBoundaryPath());
        bottomPath=new Path(bottomArea.getBoundaryPath());
        Fruit topF=new Fruit(topPath);
        Fruit botF=new Fruit(bottomPath);
        if(p1.y<p2.y)
            topF.expandNumber=-botF.expandNumber;
        else
            botF.expandNumber=-topF.expandNumber;

        // TODO END CS349
        topF.setFillColor(Color.RED);
        botF.setFillColor(Color.RED);
        if (topPath != null && bottomPath != null) {
           return new Fruit[] { topF, botF };
        }
        return new Fruit[0];
    }
}
