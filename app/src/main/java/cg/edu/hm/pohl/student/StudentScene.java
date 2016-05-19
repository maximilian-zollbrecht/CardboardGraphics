package cg.edu.hm.pohl.student;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.rendering.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;
import cg.edu.hm.pohl.CardboardGraphicsActivity;
import cg.edu.hm.pohl.DataStructures;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

import static cg.edu.hm.pohl.student.VertexObject.FLOATS_PER_VERTEX;
import static cg.edu.hm.pohl.student.VertexObject.FLOATS_PER_COLOR;
import static cg.edu.hm.pohl.student.VertexObject.FLOATS_PER_NORMAL;

/**
 * Created by Pohl on 14.04.2016.
 */
public class StudentScene extends VRComponent {

    private static final String TAG = "StudentScene";
    private static final int TESSELLATION = 8;
    private static final float RADIUS = 1f;
    private static final FloatColor clothColor = new FloatColor(0.52f, 0.82f, 0.47f, 1f);
    private static final FloatColor skinColor = new FloatColor(0.80f, 0.61f, 0.45f, 1f);

    private List<VertexObjectBuffer> buffers = new ArrayList<>();

    private Shader shader;

    private DataStructures.Matrices matrices = new DataStructures.Matrices();
    private DataStructures.Locations locations = new DataStructures.Locations();
    private DataStructures.AnimationParameters animation = new DataStructures.AnimationParameters();

    private float[] lightpos = { 0.f, 5.f, -4.f, 1.f };
    private float[] lightpos_eye = new float[4];

    private DataStructures.LightParameters light = new DataStructures.LightParameters();

    public StudentScene() {
        shader = CardboardGraphicsActivity.studentSceneShader;

        Cone upper = new Cone(TESSELLATION, RADIUS-0.2f, 1.0f, clothColor);
        upper.move(0,.7f,0);
        addVertexObject(upper);

        Sphere middle = new Sphere(16, 1.0f, skinColor);
        addVertexObject(middle);

        Cone lower = new Cone(TESSELLATION, RADIUS, 1.0f, clothColor);
        lower.move(0,-1.5f,0);
        addVertexObject(lower);

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
        // Add this VRComponent in order to be rendered
        RendererManager.getInstance().add(this);
    }

    public void draw(final float[] view, float[] projection) {
        // Use the shader
        shader.use();

        // Set the identity of the matrix
        identity();

        // Transform the shape
        translateZ(-5f);
        translateY(2.5f);

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
        glUniform4fv(locations.light_ambient, 1, light.ambient, 0);
        glUniform4fv(locations.light_diffuse, 1, light.diffuse, 0);
        glUniform4fv(locations.light_specular, 1, light.specular, 0);

        // Finally draw the cone
        drawObjects();

        // Check for possible errors.
        // If there is one, it is most probably related to an issue with the
        // shader params. Copy this line under every 'glUniform' method call
        // to identify the source of the error.
        CGUtils.checkGLError(TAG, "Error while drawing!");
    }

    private void drawObjects() {
        for(VertexObjectBuffer buffer : buffers){
            glVertexAttribPointer(locations.vertex_in, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, buffer.getVerticesBuffer());
            glVertexAttribPointer(locations.color_in, FLOATS_PER_COLOR, GL_FLOAT, false, 0, buffer.getColorsBuffer());
            glVertexAttribPointer(locations.normal_in, FLOATS_PER_NORMAL, GL_FLOAT, false, 0, buffer.getNormalsBuffer());

            glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.getObject().getVertices().length / FLOATS_PER_VERTEX);
        }
    }

    private void addVertexObject(VertexObject object){
        buffers.add(new VertexObjectBuffer(object));
    }
}
