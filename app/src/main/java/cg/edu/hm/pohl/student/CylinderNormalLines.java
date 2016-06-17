package cg.edu.hm.pohl.student;

/**
 * Created by Maximilian on 17.06.2016.
 */
public class CylinderNormalLines extends VertexObject {
    
    private final float[] vertices;
    private final float[] normals;
    private final float[] colors;
    
    public CylinderNormalLines(Cylinder cylinder){
        vertices = new float[cylinder.getVertices().length*2];
        normals = new float[cylinder.getVertices().length*2];

        // for each vertex
        for(int i=0; i<cylinder.getVertices().length/3; i++){
            vertices[i*6  ] = cylinder.getVertices()[i*3  ];
            vertices[i*6+1] = cylinder.getVertices()[i*3+1];
            vertices[i*6+2] = cylinder.getVertices()[i*3+2];
            vertices[i*6+3] = cylinder.getVertices()[i*3  ] + cylinder.getNormals()[i*3  ];
            vertices[i*6+4] = cylinder.getVertices()[i*3+1] + cylinder.getNormals()[i*3+1];
            vertices[i*6+5] = cylinder.getVertices()[i*3+2] + cylinder.getNormals()[i*3+2];

            normals[i*6  ] = -cylinder.getNormals()[i*3  ];
            normals[i*6+1] = -cylinder.getNormals()[i*3+1];
            normals[i*6+2] = -cylinder.getNormals()[i*3+2];
            normals[i*6+3] = cylinder.getNormals()[i*3  ];
            normals[i*6+4] = cylinder.getNormals()[i*3+1];
            normals[i*6+5] = cylinder.getNormals()[i*3+2];
        }

        colors = new float[cylinder.getColors().length*2];

        // for each vertex
        for(int i=0; i<cylinder.getColors().length/FLOATS_PER_COLOR; i++){
            colors[i*FLOATS_PER_COLOR*2  ] = cylinder.getColors()[i*FLOATS_PER_COLOR  ];
            colors[i*FLOATS_PER_COLOR*2+1] = cylinder.getColors()[i*FLOATS_PER_COLOR+1];
            colors[i*FLOATS_PER_COLOR*2+2] = cylinder.getColors()[i*FLOATS_PER_COLOR+2];
            colors[i*FLOATS_PER_COLOR*2+3] = cylinder.getColors()[i*FLOATS_PER_COLOR+3];
            colors[i*FLOATS_PER_COLOR*2+4] = cylinder.getColors()[i*FLOATS_PER_COLOR  ];
            colors[i*FLOATS_PER_COLOR*2+5] = cylinder.getColors()[i*FLOATS_PER_COLOR+1];
            colors[i*FLOATS_PER_COLOR*2+6] = cylinder.getColors()[i*FLOATS_PER_COLOR+2];
            colors[i*FLOATS_PER_COLOR*2+7] = cylinder.getColors()[i*FLOATS_PER_COLOR+3];
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
