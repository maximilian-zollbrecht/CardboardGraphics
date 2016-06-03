package cg.edu.hm.pohl.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.rendering.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;
import cg.edu.hm.pohl.CardboardGraphicsActivity;
import cg.edu.hm.pohl.DataStructures;

import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Pohl on 14.04.2016.
 */
public class JetFuelCantMeltDankMemes extends VRComponent {

    private float time = 0;

    private static final String TAG = "JetFuelCantMeltDankMemes";
    private static final int TESSELLATION = 32;
    private static final int EYE_TESSELLATION = 12;
    private static final float SIZE = .8f;
    private static final float HEIGHT = 2f;
    private static final float OFFSET = -4f;
    private static final FloatColor clothColor = new FloatColor(0.6f, 0.75f, 0.47f, 1f);
    private static final FloatColor skinColor = new FloatColor(0.80f, 0.61f, 0.45f, 1f);
    private static final FloatColor noseColor = new FloatColor(0.79f, 0.55f, 0.4f, 1f);
    private static final FloatColor eyeColor = new FloatColor(0, 0.99f, 0.7f, 1f);
    private static final FloatColor pupilColor = new FloatColor(0.05f, 0.05f, 0.05f, 1f);
    private static final FloatColor mouthColor = new FloatColor(0.5f, 0.27f, 0.2f, 1f);
    private static final FloatColor shotColor = new FloatColor(0.4f, 0.9f, 0.4f, 1f);

    private Map<VertexObject, VertexObjectBuffer> buffers = new HashMap<>();

    private Shader shader;

    private DataStructures.Matrices matrices = new DataStructures.Matrices();
    private DataStructures.Locations locations = new DataStructures.Locations();
    private DataStructures.AnimationParameters animation = new DataStructures.AnimationParameters();

    private float[] lightpos = { 0.f, 5.f, -4.f, 1.f };
    private float[] lightpos_eye = new float[4];

    private DataStructures.LightParameters light = new DataStructures.LightParameters();

    private final List<VertexComponent> components = new ArrayList<>();

    public JetFuelCantMeltDankMemes() {
        shader = CardboardGraphicsActivity.studentSceneShader;


        // Create Components
        Cone hat = new Cone(TESSELLATION, .9f*SIZE, 2*SIZE, clothColor);
        components.add(new VertexComponent(0,.5f*SIZE + HEIGHT,OFFSET, hat));

        Sphere middle = new Sphere(TESSELLATION, SIZE , skinColor);
        components.add(new VertexComponent(0,HEIGHT,OFFSET, middle));

        Sphere eye1 = new Sphere(EYE_TESSELLATION, 0.15f*SIZE, eyeColor);
        components.add(new VertexComponent(.3f*SIZE, .2f*SIZE + HEIGHT, .9f*SIZE + OFFSET, eye1){
            @Override
            public void animate(int animationFrame) {
                float progress = (float) Math.sin(animationFrame/100d);
                scale(1f+Math.max(progress, 0));
            }
        });

        Sphere pupil1 = new Sphere(EYE_TESSELLATION, 0.08f*SIZE, pupilColor);
        components.add(new VertexComponent(.3f*SIZE, .2f*SIZE + HEIGHT, 1f*SIZE + OFFSET, pupil1) {
            @Override
            public void animate(int animationFrame) {
                float progress = Math.max(0,(float) Math.sin(animationFrame/100d));
                scale(1f+progress);
                translateZ(progress*0.1f);
            }
        });

        Sphere eye2 = new Sphere(EYE_TESSELLATION, 0.15f*SIZE, eyeColor);
        components.add(new VertexComponent(-.3f*SIZE, .2f*SIZE + HEIGHT, .9f*SIZE + OFFSET, eye1){
            @Override
            public void animate(int animationFrame) {
                float progress = (float) Math.sin((animationFrame-100)/100d);
                scale(1f+Math.max(progress, 0));
            }
        });

        Sphere pupil2 = new Sphere(EYE_TESSELLATION, 0.08f*SIZE, pupilColor);
        components.add(new VertexComponent(-.3f*SIZE, .2f*SIZE + HEIGHT, 1f*SIZE + OFFSET, pupil1) {
            @Override
            public void animate(int animationFrame) {
                float progress = Math.max(0,(float) Math.sin((animationFrame-100)/100d));
                scale(1f+progress);
                translateZ(progress*0.1f);
            }
        });

        Cone mouth = new Cone(TESSELLATION, 0.6f*SIZE, .2f*SIZE, mouthColor);
        components.add(new VertexComponent(0,-.4f*SIZE + HEIGHT,.48f*SIZE + OFFSET, mouth){
            @Override
            public void animate(int animationFrame) {
                final int interval = 1000;
                final int openLength = 100;
                final int openCloseLength = 100;
                int pos = animationFrame%interval;

                final float xScale = 1.1f, yScale=1.5f, zScale = 1.3f;
                if(pos<openLength) {
                    // Open
                    scale(xScale, yScale, zScale);
                } else if(pos<openLength+openCloseLength) {
                    // Closing
                    float open = (openLength+openCloseLength-pos)/(float)openCloseLength;
                    scale((1-open) + xScale*open, (1-open) + yScale*open, (1-open) + zScale*open);
                } else if(pos>interval-openCloseLength) {
                    // Opening
                    float open = (pos-interval+openCloseLength)/(float)openCloseLength;
                    scale((1-open) + xScale*open, (1-open) + yScale*open, (1-open) + zScale*open);
                }
            }
        });

        Cylinder shot = new Cylinder(TESSELLATION, .2f, 5f, shotColor);
        components.add(new VertexComponent(0, -.33f*SIZE + HEIGHT, OFFSET, shot){
            @Override
            public void animate(int animationFrame) {
                rotateX(90f);
                if(animationFrame%1000<100) {
                    scale(1f, animationFrame % 100 / 100f, 1f);
                } else {
                    scale(1f, .01f, 1f);
                }
            }
        });

        Cone nose = new Cone(TESSELLATION, 0.25f*SIZE, 0.35f*SIZE, noseColor);
        components.add(new VertexComponent(0,-.1f*SIZE + HEIGHT,.9f*SIZE + OFFSET, nose));

        Cone ear1 = new Cone(4, 0.15f*SIZE, 1f*SIZE, noseColor);
        components.add(new VertexComponent(-.9f*SIZE, HEIGHT, OFFSET, ear1){
            @Override
            public void animate(int animationFrame) {
                float angle = (float) Math.sin(animationFrame/100d)*90f;
                rotateZ(angle*.25f + 22.5f);
            }
        });

        Cone ear2 = new Cone(4, 0.15f*SIZE, 1f*SIZE, noseColor);
        components.add(new VertexComponent(.9f*SIZE, HEIGHT, OFFSET, ear1){
            @Override
            public void animate(int animationFrame) {
                float angle = (float) Math.sin(animationFrame/100d)*90f;
                rotateZ(angle*-.25f - 22.5f);
            }
        });

        Cone lower = new Cone(TESSELLATION, SIZE*1.5f, SIZE, clothColor);
        components.add(new VertexComponent(0, -1.5f*SIZE + HEIGHT, OFFSET, lower));

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

        // Add VRComponents in order to be rendered
        for(VertexComponent component : components){
            RendererManager.getInstance().add(component);
        }
    }
}
