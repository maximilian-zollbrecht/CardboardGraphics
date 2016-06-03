package cg.edu.hm.pohl.student;

/**
 * Created by Maximilian on 18.05.2016.
 * This Class represents an upright cone described by three float array of vertices for its top and three for its bottom part.
 * Each part has vertices, color and normals.
 * Every 3 vertices and normals and 4 colors represent a triangle.
 * Designed to be used with OpenGLs GL_TRIANGLES;
 */
public class Cylinder extends VertexObject {

    private final int tessselation;
    private final float radius, height;

    private float[] coneVertices;
    private float[] coneColors;
    private float[] coneNormals;

    public Cylinder(int tessselation, float radius, float height, FloatColor color){
        this.radius = radius;
        this.height = height;
        this.tessselation = tessselation;

        final int verticesNumber = tessselation * 3 * 3 * 2 * 2;
        final double deltaAngle = (Math.PI / (tessselation / 2));

        coneVertices = new float[verticesNumber];
        coneColors = new float[verticesNumber / 3 * 4];
        coneNormals = new float[verticesNumber];

        int index = 0;

        // offset for vertices and colors
        int vertexIndex = 0;
        int colorIndex = 0;

        System.out.println(verticesNumber);
        for(float angle = 0.0f; angle < Math.PI*2.0; angle += deltaAngle) {
            System.out.println(vertexIndex);
            // Calculate x and z of the cone
            float x1 = (float) (radius * Math.sin(angle));
            float z1 = (float) (radius * Math.cos(angle));
            float x2 = (float) (radius * Math.sin(angle + deltaAngle));
            float z2 = (float) (radius * Math.cos(angle + deltaAngle));

            // First Triangle
            // First vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Second vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = x1;
            coneNormals[vertexIndex + 1] = .5f;
            coneNormals[vertexIndex + 2] = z1;
            vertexIndex += 3;
            // Third vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = x2;
            coneNormals[vertexIndex + 1] = .5f;
            coneNormals[vertexIndex + 2] = z2;
            vertexIndex += 3;

            // Second Triangle
            // First vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Second vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = x1;
            coneNormals[vertexIndex + 1] = .5f;
            coneNormals[vertexIndex + 2] = z1;
            vertexIndex += 3;
            // Third vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = x2;
            coneNormals[vertexIndex + 1] = .5f;
            coneNormals[vertexIndex + 2] = z2;
            vertexIndex += 3;

            // First bottom vertex
            coneVertices[vertexIndex] = 0;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = 0;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Second bottom vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Third bottom vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;

            // First top vertex
            coneVertices[vertexIndex] = 0;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = 0;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Second top vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Third top vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;

            for(int i=0; i<coneColors.length; i+=4){
                coneColors[i] = color.getR();
                coneColors[i+1] = color.getG();
                coneColors[i+2] = color.getB();
                coneColors[i+3] = color.getA();
            }
        }
    }

    @Override
    public float[] getVertices() {
        return coneVertices;
    }

    @Override
    public float[] getColors() {
        return coneColors;
    }

    @Override
    public float[] getNormals() {
        return coneNormals;
    }
}
