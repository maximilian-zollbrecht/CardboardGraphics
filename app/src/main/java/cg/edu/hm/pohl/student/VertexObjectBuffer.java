package cg.edu.hm.pohl.student;

import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;

import java.nio.FloatBuffer;

/**
 * Created by Maximilian on 18.05.2016.
 */
public class VertexObjectBuffer {
    private final VertexObject object;
    private final FloatBuffer verticesBuffer, colorsBuffer, normalsBuffer;

    public VertexObjectBuffer(VertexObject object) {
        this.object = object;

        this.verticesBuffer = CGUtils.createFloatBuffer(object.getVertices());
        this.colorsBuffer = CGUtils.createFloatBuffer(object.getColors());
        this.normalsBuffer = CGUtils.createFloatBuffer(object.getNormals());
    }

    public FloatBuffer getVerticesBuffer() {
        return verticesBuffer;
    }

    public FloatBuffer getColorsBuffer() {
        return colorsBuffer;
    }

    public FloatBuffer getNormalsBuffer() {
        return normalsBuffer;
    }

    public VertexObject getObject() {
        return object;
    }
}
