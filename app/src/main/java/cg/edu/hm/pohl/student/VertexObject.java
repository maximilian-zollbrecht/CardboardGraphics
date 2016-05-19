package cg.edu.hm.pohl.student;

/**
 * Created by Maximilian on 18.05.2016.
 */
public abstract class VertexObject {
    public static final int FLOATS_PER_VERTEX = 3;
    public static final int FLOATS_PER_COLOR = 4;
    public static final int FLOATS_PER_NORMAL = 3;

    public abstract float[] getVertices();
    public abstract float[] getColors();
    public abstract float[] getNormals();

    public void move(float x, float z, float y){
        for(int i = 0; i<getVertices().length; i=i+3){
            getVertices()[i] = getVertices()[i]+x;
            getVertices()[i+1] = getVertices()[i+1]+z;
            getVertices()[i+2] = getVertices()[i+2]+y;
        }
    }
}
