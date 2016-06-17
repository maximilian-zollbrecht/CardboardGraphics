package cg.edu.hm.pohl.student;

/**
 * Created by Maximilian on 18.05.2016.
 * This Class represents an upright cone described by three float array of vertices for its top and three for its bottom part.
 * Each part has vertices, color and normals.
 * Every 3 vertices and normals and 4 colors represent a triangle.
 * Designed to be used with OpenGLs GL_TRIANGLES;
 */
public class Cone extends VertexObject {

    private final int tessselation;
    private final float radius, height;

    private float[] coneVertices;
    private float[] coneColors;
    private float[] coneNormals;

    public Cone(int tessselation, float radius, float height, FloatColor... colors){
        if(colors.length==0) {
            colors = new FloatColor[]{new FloatColor(1f, 1f, 1f, 1f)};
        }

        this.radius = radius;
        this.height = height;
        this.tessselation = tessselation;

        final int verticesNumber = tessselation * 3 * 3 * 2;
        final double deltaAngle = (Math.PI / (tessselation / 2));

        coneVertices = new float[verticesNumber];
        coneColors = new float[verticesNumber / 3 * 4];
        coneNormals = new float[verticesNumber];

        float angle = 0f;
        for(int index = 0; index < tessselation; index++) {
            angle = (float) (index*deltaAngle);
        //for(float angle = 0.0f; angle < Math.PI*2.0; angle += deltaAngle) {
            // Calculate x and z of the cone
            float x1 = (float) (radius * Math.sin(angle));
            float z1 = (float) (radius * Math.cos(angle));
            float x2 = (float) (radius * Math.sin(angle + deltaAngle));
            float z2 = (float) (radius * Math.cos(angle + deltaAngle));

            // Calculate offset for vertices and colors
            final int vertexIndex = index * 18;
            final int colorIndex = index * 24;

            // First vertex
            coneVertices[vertexIndex] = 0;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = 0;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = 1f;
            coneNormals[vertexIndex + 2] = 0;
            // Second vertex
            coneVertices[vertexIndex + 3] = x1;
            coneVertices[vertexIndex + 4] = 0;
            coneVertices[vertexIndex + 5] = z1;
            coneNormals[vertexIndex + 3] = x1;
            coneNormals[vertexIndex + 4] = .5f;
            coneNormals[vertexIndex + 5] = z1;
            // Third vertex
            coneVertices[vertexIndex + 6] = x2;
            coneVertices[vertexIndex + 7] = 0;
            coneVertices[vertexIndex + 8] = z2;
            coneNormals[vertexIndex + 6] = x2;
            coneNormals[vertexIndex + 7] = .5f;
            coneNormals[vertexIndex + 8] = z2;

            // First bottom vertex
            coneVertices[vertexIndex + 9] = 0;
            coneVertices[vertexIndex + 10] = 0;
            coneVertices[vertexIndex + 11] = 0;
            coneNormals[vertexIndex + 9] = 0;
            coneNormals[vertexIndex + 10] = 1f;
            coneNormals[vertexIndex + 11] = 0;
            // Second bottom vertex
            coneVertices[vertexIndex + 12] = x2;
            coneVertices[vertexIndex + 13] = 0;
            coneVertices[vertexIndex + 14] = z2;
            coneNormals[vertexIndex + 12] = 0;
            coneNormals[vertexIndex + 13] = 1f;
            coneNormals[vertexIndex + 14] = 0;
            // Third bottom vertex
            coneVertices[vertexIndex + 15] = x1;
            coneVertices[vertexIndex + 16] = 0;
            coneVertices[vertexIndex + 17] = z1;
            coneNormals[vertexIndex + 15] = 0;
            coneNormals[vertexIndex + 16] = 1f;
            coneNormals[vertexIndex + 17] = 0;

            FloatColor color = colors[index%colors.length];

            coneColors[colorIndex] = color.getR();
            coneColors[colorIndex + 1] = color.getG();
            coneColors[colorIndex + 2] = color.getB();
            coneColors[colorIndex + 3] = color.getA();

            coneColors[colorIndex + 4] = color.getR();
            coneColors[colorIndex + 5] = color.getG();
            coneColors[colorIndex + 6] = color.getB();
            coneColors[colorIndex + 7] = color.getA();

            coneColors[colorIndex + 8] = color.getR();
            coneColors[colorIndex + 9] = color.getG();
            coneColors[colorIndex + 10] = color.getB();
            coneColors[colorIndex + 11] = color.getA();


            coneColors[colorIndex + 12] = color.getR();
            coneColors[colorIndex + 13] = color.getG();
            coneColors[colorIndex + 14] = color.getB();
            coneColors[colorIndex + 15] = color.getA();

            coneColors[colorIndex + 16] = color.getR();
            coneColors[colorIndex + 17] = color.getG();
            coneColors[colorIndex + 18] = color.getB();
            coneColors[colorIndex + 19] = color.getA();

            coneColors[colorIndex + 20] = color.getR();
            coneColors[colorIndex + 21] = color.getG();
            coneColors[colorIndex + 22] = color.getB();
            coneColors[colorIndex + 23] = color.getA();
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
