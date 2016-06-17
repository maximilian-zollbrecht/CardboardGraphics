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
    private static final FloatColor[] clothColor = {
            new FloatColor(0.6f, 0.75f, 0.47f, 1f),
            new FloatColor(0.61f, 0.76f, 0.48f, 1f)
    };
    private static final FloatColor skinColor = new FloatColor(0.80f, 0.61f, 0.45f, 1f);
    private static final FloatColor noseColor = new FloatColor(0.62f, 0.46f, 0.32f, 1f);
    private static final FloatColor eyeColor = new FloatColor(0, 0.99f, 0.7f, 1f);
    private static final FloatColor pupilColor = new FloatColor(0.05f, 0.05f, 0.05f, 1f);
    private static final FloatColor mouthColor = new FloatColor(0.5f, 0.27f, 0.2f, 1f);
    private static final FloatColor[] shotColors = {
            new FloatColor(0.99f, 0.1f, 0.1f, 1f),
            new FloatColor(0.99f, 0.49f, 0.1f, 1f),
            new FloatColor(0.99f, 0.99f, 0.1f, 1f),
            new FloatColor(0.1f, 0.99f, 0.1f, 1f),
            new FloatColor(0.1f, 0.99f, 0.99f, 1f),
            new FloatColor(0.99f, 0.1f, 0.99f, 1f),
    };

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
        Cone upper = new Cone(TESSELLATION, .9f*SIZE, 2*SIZE, clothColor);
        VertexComponent hat = new VertexComponent(0,.5f*SIZE + HEIGHT,OFFSET, upper);
        hat.setDiffuse(.3f);
        hat.setSpecular(.15f);
        components.add(hat);

        Sphere middle = new Sphere(TESSELLATION, SIZE , skinColor);
        VertexComponent head = new VertexComponent(0,HEIGHT,OFFSET, middle);
        head.setDiffuse(.4f);
        head.setSpecular(.2f);
        components.add(head);

        Sphere eye1 = new Sphere(EYE_TESSELLATION, 0.15f*SIZE, eyeColor);
        Sphere pupil1 = new Sphere(EYE_TESSELLATION, 0.08f*SIZE, pupilColor);
        pupil1.move(0, 0, .08f);

        VertexComponent leftEye = new VertexComponent(eye1, pupil1){
            @Override
            public void animate(int animationFrame) {
                float progress = (float) Math.sin(animationFrame/100d);
                scale(1f+Math.max(progress, 0));
            }
        };
        leftEye.setX(.3f*SIZE);
        leftEye.setY(.2f*SIZE + HEIGHT);
        leftEye.setZ(.9f*SIZE + OFFSET);
        leftEye.setSpecular(1f);
        components.add(leftEye);

        Sphere eye2 = new Sphere(EYE_TESSELLATION, 0.15f*SIZE, eyeColor);
        Sphere pupil2 = new Sphere(EYE_TESSELLATION, 0.08f*SIZE, pupilColor);
        pupil2.move(0, 0, .08f);
        VertexComponent rightEye = new VertexComponent(eye2, pupil2){
            @Override
            public void animate(int animationFrame) {
                float progress = (float) Math.sin((animationFrame-100)/100d);
                scale(1f+Math.max(progress, 0));
            }
        };
        rightEye.setX(-.3f*SIZE);
        rightEye.setY(.2f*SIZE + HEIGHT);
        rightEye.setZ(.9f*SIZE + OFFSET);
        rightEye.setSpecular(1f);
        components.add(rightEye);

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

        Cylinder shot = new Cylinder(TESSELLATION, .2f, 15f, shotColors);
        CylinderNormalLines cylinderNormalLines = new CylinderNormalLines(shot);
        VertexComponent rainbowShot = new VertexComponent(0, -.33f*SIZE + HEIGHT, OFFSET, shot, cylinderNormalLines){
            @Override
            public void animate(int animationFrame) {
                if(animationFrame%1000<100) {
                    scale(1f, animationFrame % 100 / 100f, 1f);
                } else {
                    scale(1f, .0025f, 1f);
                }
            }
        };
        rainbowShot.setRx(90f);
        rainbowShot.setSpecular(1f);
        components.add(rainbowShot);

        Cone noseCone = new Cone(TESSELLATION, 0.25f*SIZE, 0.35f*SIZE, noseColor);
        VertexComponent nose = new VertexComponent(0,-.1f*SIZE + HEIGHT,.9f*SIZE + OFFSET, noseCone);
        nose.setDiffuse(.45f);
        nose.setSpecular(.5f);
        components.add(nose);

        Cone ear1 = new Cone(4, 0.15f*SIZE, 1f*SIZE, noseColor);
        VertexComponent rightEar =new VertexComponent(-.9f*SIZE, HEIGHT, OFFSET, ear1){
            @Override
            public void animate(int animationFrame) {
                float angle = (float) Math.sin(animationFrame/100d)*90f;
                rotateZ(angle*.25f + 22.5f);
            }
        };
        rightEar.setDiffuse(.4f);
        components.add(rightEar);

        Cone ear2 = new Cone(4, 0.15f*SIZE, 1f*SIZE, noseColor);
        VertexComponent leftEar = new VertexComponent(.9f*SIZE, HEIGHT, OFFSET, ear2){
            @Override
            public void animate(int animationFrame) {
                float angle = (float) Math.sin(animationFrame/100d)*90f;
                rotateZ(angle*-.25f - 22.5f);
            }
        };
        leftEar.setDiffuse(.4f);
        components.add(leftEar);

        Cone lower = new Cone(TESSELLATION, SIZE*1.5f, SIZE, clothColor);
        VertexComponent body = new VertexComponent(0, -1.5f*SIZE + HEIGHT, OFFSET, lower);
        body.setDiffuse(.2f);
        components.add(body);

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
