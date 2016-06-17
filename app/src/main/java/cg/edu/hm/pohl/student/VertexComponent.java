package cg.edu.hm.pohl.student;

import android.opengl.GLES20;
import android.opengl.Matrix;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.VRObject;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;
import cg.edu.hm.pohl.CardboardGraphicsActivity;
import cg.edu.hm.pohl.DataStructures;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_UNSIGNED_INT;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static cg.edu.hm.pohl.student.VertexObject.FLOATS_PER_COLOR;
import static cg.edu.hm.pohl.student.VertexObject.FLOATS_PER_NORMAL;
import static cg.edu.hm.pohl.student.VertexObject.FLOATS_PER_VERTEX;

/**
 * Created by Maximilian on 03.06.2016.
 */
public class VertexComponent extends VRComponent {
    private static final String TAG = "VertexComponent";

    public static final boolean DRAW_CYLINDER_NORMALS = true;

    private int animationFrame = 0;

    private Shader shader;

    private DataStructures.Matrices matrices = new DataStructures.Matrices();
    private DataStructures.Locations locations = new DataStructures.Locations();
    private DataStructures.AnimationParameters animation = new DataStructures.AnimationParameters();

    private float[] lightpos = { 0f, 5f, -4f, 1f };
    private float[] lightpos_eye = new float[4];

    private DataStructures.LightParameters light = new DataStructures.LightParameters();

    private Map<VertexObject, VertexObjectBuffer> buffers = new HashMap<>();

    private float x, y, z;
    private float rx, ry, rz;

    private float[] ambient, diffuse, specular;

    public VertexComponent(VertexObject... objects){
        this(0, 0, 0, 0, 0, 0, objects);
    }

    public VertexComponent(float x, float y, float z, VertexObject... objects){
        this(x, y, z, 0, 0, 0, objects);
    }

    public VertexComponent(float x, float y, float z, float rx, float ry, float rz, VertexObject... objects) {
        shader = CardboardGraphicsActivity.studentSceneShader;

        // Set position
        this.y = y;
        this.z = z;
        this.x = x;

        this.rx = rx;
        this.ry = ry;
        this.rz = rz;

        DataStructures.LightParameters defaultLight = new DataStructures.LightParameters();
        this.ambient = defaultLight.ambient;
        this.diffuse = defaultLight.diffuse;
        this.specular = defaultLight.specular;

        // Add Objects
        for(VertexObject object : objects){
            buffers.put(object, new VertexObjectBuffer(object));
        }

        // Get the shader's attribute and uniform handles used to delegate data from
        // the CPU to the GPU
        locations.vertex_in = shader.getAttribute("vertex_in");
        locations.color_in = shader.getAttribute("color_in");
        locations.normal_in = shader.getAttribute("normal_in");
        locations.pvm = shader.getUniform("pvm");
        locations.vm = shader.getUniform("vm");
        locations.lightpos = shader.getUniform("lightpos");
        locations.light_ambient = shader.getUniform("light.ambient");
        locations.light_diffuse = shader.getUniform("light.diffuse");
        locations.light_specular = shader.getUniform("light.specular");
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public void setAmbient(float ambient) {
        this.ambient = new float[]{ambient, ambient, ambient, 1f};
    }

    public void setDiffuse(float diffuse) {
        this.diffuse = new float[]{diffuse, diffuse, diffuse, 1f};
    }

    public void setSpecular(float specular) {
        this.specular = new float[]{specular, specular, specular, 1f};
    }

    public void draw(final float[] view, float[] projection) {
        // Use the shader
        shader.use();

        // Set the identity of the matrix
        identity();

        // Transform the shape
        translateX(x);
        translateY(y);
        translateZ(z);

        rotateX(rx);
        rotateY(ry);
        rotateZ(rz);

        animate(animationFrame++);

        // Update collision box bounds
        updateBounds(this);

        // Calculate light position in the eye space
        Matrix.multiplyMV(lightpos_eye, 0, view, 0, lightpos, 0);

        // Calculate Model-View and Model-View-Projection matrices
        Matrix.multiplyMM(matrices.vm, 0, view, 0, getFloat16(), 0);
        Matrix.multiplyMM(matrices.pvm, 0, projection, 0, matrices.vm, 0);

        // Put the parameters into the shaders
        glUniformMatrix4fv(locations.pvm, 1, false, matrices.pvm, 0);
        glUniformMatrix4fv(locations.vm, 1, false, matrices.vm, 0);
        glUniform4fv(locations.lightpos, 1, lightpos_eye, 0);

        glUniform4fv(locations.light_ambient, 1, ambient, 0);
        glUniform4fv(locations.light_diffuse, 1, diffuse, 0);
        glUniform4fv(locations.light_specular, 1, specular, 0);

        // Finally draw the objects
        drawObjects();

        // Check for possible errors.
        // If there is one, it is most probably related to an issue with the
        // shader params. Copy this line under every 'glUniform' method call
        // to identify the source of the error.
        CGUtils.checkGLError(TAG, "Error while drawing!");
    }

    private void drawObjects() {
        for(Map.Entry<VertexObject, VertexObjectBuffer> entry : buffers.entrySet()){
            glVertexAttribPointer(locations.vertex_in, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, entry.getValue().getVerticesBuffer());
            glVertexAttribPointer(locations.color_in, FLOATS_PER_COLOR, GL_FLOAT, false, 0, entry.getValue().getColorsBuffer());
            glVertexAttribPointer(locations.normal_in, FLOATS_PER_NORMAL, GL_FLOAT, false, 0, entry.getValue().getNormalsBuffer());

            if(entry.getKey() instanceof CylinderNormalLines){
                glDrawArrays(GL_LINES, 0, entry.getKey().getVertices().length / FLOATS_PER_VERTEX);
            } else {
                glDrawArrays(GLES20.GL_TRIANGLES, 0, entry.getKey().getVertices().length / FLOATS_PER_VERTEX);
            }
        }
    }

    public void animate(int animationFrame){}

}
