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

    public Cylinder(int tessselation, float radius, float height, FloatColor... colors){
        if(colors.length==0) {
            colors = new FloatColor[]{new FloatColor(1f, 1f, 1f, 1f)};
        }

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

        for(float angle = 0.0f; angle < Math.PI*2.0; angle += deltaAngle) {
            // Calculate x and z of the cone
            float x1 = (float) (radius * Math.sin(angle));
            float z1 = (float) (radius * Math.cos(angle));
            float x2 = (float) (radius * Math.sin(angle + deltaAngle));
            float z2 = (float) (radius * Math.cos(angle + deltaAngle));

            float x1n = (float) (x1/Math.hypot(x1, z1));
            float z1n = (float) (z1/Math.hypot(x1, z1));
            float x2n = (float) (x2/Math.hypot(x2, z2));
            float z2n = (float) (z2/Math.hypot(x2, z2));

            // First Triangle
            // First vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = x1n;
            coneNormals[vertexIndex + 1] = 0;
            coneNormals[vertexIndex + 2] = z1n;
            vertexIndex += 3;
            // Second vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = x1n;
            coneNormals[vertexIndex + 1] = 0;
            coneNormals[vertexIndex + 2] = z1n;
            vertexIndex += 3;
            // Third vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = x2n;
            coneNormals[vertexIndex + 1] = 0;
            coneNormals[vertexIndex + 2] = z2n;
            vertexIndex += 3;

            // Second Triangle
            // First vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = x2n;
            coneNormals[vertexIndex + 1] = 0;
            coneNormals[vertexIndex + 2] = z2n;
            vertexIndex += 3;
            // Second vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = height;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = x1n;
            coneNormals[vertexIndex + 1] = 0;
            coneNormals[vertexIndex + 2] = z1n;
            vertexIndex += 3;
            // Third vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = x2n;
            coneNormals[vertexIndex + 1] = 0;
            coneNormals[vertexIndex + 2] = z2n;
            vertexIndex += 3;

            // First bottom vertex
            coneVertices[vertexIndex] = 0;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = 0;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = -1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Second bottom vertex
            coneVertices[vertexIndex] = x2;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z2;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = -1f;
            coneNormals[vertexIndex + 2] = 0;
            vertexIndex += 3;
            // Third bottom vertex
            coneVertices[vertexIndex] = x1;
            coneVertices[vertexIndex + 1] = 0;
            coneVertices[vertexIndex + 2] = z1;
            coneNormals[vertexIndex] = 0;
            coneNormals[vertexIndex + 1] = -1f;
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


            FloatColor color = colors[index%colors.length];

            // System.out.println("Color : "+color.getR()+" "+color.getG()+" "+color.getB());
            for(int i=0; i<12*4; i+=4){
                coneColors[colorIndex+i] = color.getR();
                coneColors[colorIndex+i+1] = color.getG();
                coneColors[colorIndex+i+2] = color.getB();
                coneColors[colorIndex+i+3] = color.getA();
            }
            colorIndex += 12*4;

            index++;
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

    public float[] getNormalLines() {
        float[] result = new float[coneVertices.length*2];

        // for each vertex
        for(int i=0; i<coneVertices.length/3; i++){
            result[i*6  ] = coneVertices[i*3  ];
            result[i*6+1] = coneVertices[i*3+1];
            result[i*6+2] = coneVertices[i*3+2];
            result[i*6+3] = coneVertices[i*3  ] + coneNormals[i*3  ];
            result[i*6+4] = coneVertices[i*3+1] + coneNormals[i*3+1];
            result[i*6+5] = coneVertices[i*3+2] + coneNormals[i*3+2];
        }

        return result;
    }

    public float[] getNormalColors() {
        float[] result = new float[coneColors.length*2];

        // for each vertex
        for(int i=0; i<coneVertices.length/FLOATS_PER_COLOR; i++){
            result[i*FLOATS_PER_COLOR*2  ] = coneColors[i*FLOATS_PER_COLOR  ];
            result[i*FLOATS_PER_COLOR*2+1] = coneColors[i*FLOATS_PER_COLOR+1];
            result[i*FLOATS_PER_COLOR*2+2] = coneColors[i*FLOATS_PER_COLOR+2];
            result[i*FLOATS_PER_COLOR*2+3] = coneColors[i*FLOATS_PER_COLOR+3];
            result[i*FLOATS_PER_COLOR*2+4] = coneColors[i*FLOATS_PER_COLOR  ];
            result[i*FLOATS_PER_COLOR*2+5] = coneColors[i*FLOATS_PER_COLOR+1];
            result[i*FLOATS_PER_COLOR*2+6] = coneColors[i*FLOATS_PER_COLOR+2];
            result[i*FLOATS_PER_COLOR*2+7] = coneColors[i*FLOATS_PER_COLOR+3];
        }

        return result;
    }

}
