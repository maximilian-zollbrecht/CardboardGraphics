package cg.edu.hm.pohl.student;

import android.renderscript.Float4;

/**
 * Created by Maximilian on 18.05.2016.
 */
public class Sphere extends VertexObject {

    private static final int FLOATS_PER_VERTEX = 3;
    private static final int FLOATS_PER_COLOR = 4;
    private static final int FLOATS_PER_NORMAL = 3;

    private final int tessselation;
    private final float radius;

    private final float[] vertices, colors, normals;

    public Sphere(int tessselation, float radius, FloatColor color) {
        this.tessselation = tessselation;
        this.radius = radius;

        final int vertexNumber = tessselation * tessselation/2 * 3 * 3 *2;

        vertices = new float[vertexNumber];
        colors = new float[vertexNumber/3*4];
        normals = new float[vertexNumber];

        int vertexIndex = 0;
        int colorIndex = 0;
        final float deltaAngle = (float) Math.PI/tessselation*2;
        float x1max=1f, x2max=1f, y1max=0, y2max=0, z1=1f, z2=1f;
        // Vertical slices
        for(int i=1; i<=tessselation; i++) {
            float angle = i*deltaAngle;
            x1max = x2max;
            y1max = y2max;
            x2max = (float)(radius*Math.cos(angle));
            y2max = (float)(radius*Math.sin(angle));

            // Horizontal Slices
            z1 = 1f;
            z2 = 1f;
            for (int j = 1; j <= tessselation / 2; j++) {
                z1 = z2;
                z2 = (float) (radius * Math.cos(deltaAngle * j));

                float z1Multiplier = (float) Math.abs(Math.sin(deltaAngle * (j-1)));
                float z2Multiplier = (float) Math.abs(Math.sin(deltaAngle * j));

                // First Triangle
                // First vertex
                vertices[vertexIndex] = x1max*z1Multiplier;
                vertices[vertexIndex + 1] = z1;
                vertices[vertexIndex + 2] = y1max*z1Multiplier;
                normals[vertexIndex] = x1max*z1Multiplier;
                normals[vertexIndex + 1] = z1;
                normals[vertexIndex + 2] = y1max*z1Multiplier;
                // Second vertex
                vertices[vertexIndex + 3] = x2max*z1Multiplier;
                vertices[vertexIndex + 4] = z1;
                vertices[vertexIndex + 5] = y2max*z1Multiplier;
                normals[vertexIndex + 3] = x2max*z1Multiplier;
                normals[vertexIndex + 4] = z1;
                normals[vertexIndex + 5] = y2max*z1Multiplier;
                // Third vertex
                vertices[vertexIndex + 6] = x1max*z2Multiplier;
                vertices[vertexIndex + 7] = z2;
                vertices[vertexIndex + 8] = y1max*z2Multiplier;
                normals[vertexIndex + 6] = x1max*z2Multiplier;
                normals[vertexIndex + 7] = z2;
                normals[vertexIndex + 8] = y1max*z2Multiplier;

                // Second Triangle
                // First vertex
                vertices[vertexIndex + 9] = x1max*z2Multiplier;
                vertices[vertexIndex + 10] = z2;
                vertices[vertexIndex + 11] = y1max*z2Multiplier;
                normals[vertexIndex + 9] = x1max*z2Multiplier;
                normals[vertexIndex + 10] = z2;
                normals[vertexIndex + 11] = y1max*z2Multiplier;
                // Second vertex
                vertices[vertexIndex + 12] = x2max*z1Multiplier;
                vertices[vertexIndex + 13] = z1;
                vertices[vertexIndex + 14] = y2max*z1Multiplier;
                normals[vertexIndex + 12] = x2max*z1Multiplier;
                normals[vertexIndex + 13] = z1;
                normals[vertexIndex + 14] = y2max*z1Multiplier;
                // Third vertex
                vertices[vertexIndex + 15] = x2max*z2Multiplier;
                vertices[vertexIndex + 16] = z2;
                vertices[vertexIndex + 17] = y2max*z2Multiplier;
                normals[vertexIndex + 15] = x2max*z2Multiplier;
                normals[vertexIndex + 16] = z2;
                normals[vertexIndex + 17] = y2max*z2Multiplier;

                for(int k=0; k<6*4; k+=4){
                    colors[colorIndex+k] = color.getR();
                    colors[colorIndex+k+1] = color.getG();
                    colors[colorIndex+k+2] = color.getB();
                    colors[colorIndex+k+3] = color.getA();
                }

                vertexIndex += 6*3;
                colorIndex += 6*4;
            }
        }
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    @Override
    public float[] getColors() {
        return colors;
    }

    @Override
    public float[] getNormals() {
        return normals;
    }

}