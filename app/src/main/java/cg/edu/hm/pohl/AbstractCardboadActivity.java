package cg.edu.hm.pohl;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import ba.pohl1.hm.edu.vrlibrary.base.Options;
import ba.pohl1.hm.edu.vrlibrary.base.Shader;
import ba.pohl1.hm.edu.vrlibrary.base.manager.CollisionManager;
import ba.pohl1.hm.edu.vrlibrary.base.manager.InstancedRendererManager;
import ba.pohl1.hm.edu.vrlibrary.base.manager.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.base.manager.TransformationManager;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRDrawableNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import cg.edu.hm.pohl.student.StudentScene;

import static ba.pohl1.hm.edu.vrlibrary.util.BAConstants.Z_FAR;
import static ba.pohl1.hm.edu.vrlibrary.util.BAConstants.Z_NEAR;

/**
 * Created by Pohl on 14.04.2016.
 */
public class AbstractCardboadActivity extends CardboardActivity implements CardboardView.StereoRenderer{

    public static Shader shader;
    public static Shader gridShader;
    public static Shader colorShader;

    private VRRoom vrRoom;
    private VRCamera camera;
    private StudentScene studentScene;

    private VRNavigator navigator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_scene);

        final CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setSettingsButtonEnabled(false);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        CollisionManager.getInstance().init(1, 1, 1, 200, 200, 200);
    }

    @Override
    public void onCardboardTrigger() {
        navigator.onCardboardTrigger();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        camera.updateCamera(headTransform);
        navigator.navigate(0.02f);
        TransformationManager.getInstance().applyTransformations(1);
    }

    @Override
    public void onDrawEye(Eye eye) {
        // Setup GL options
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        if(Options.CULLING_ENABLED) {
            GLES30.glEnable(GLES30.GL_CULL_FACE);
        } else {
            GLES30.glDisable(GLES30.GL_CULL_FACE);
        }
        GLES30.glCullFace(Options.CULLING_MODE);
        GLES30.glFrontFace(Options.FRONT_FACE);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        // Update camera and light
        camera.updateCamera(eye);

        // Retrieve projection matrix from eye
        float[] projection = eye.getPerspective(Z_NEAR, Z_FAR);

        // Update focus recognition
        FocusManager.getInstance().updateFocusTargets(camera);

        // Draw all the objects
        RendererManager.getInstance().render(camera.getView(), projection);
        InstancedRendererManager.getInstance().drawInstanced(camera.getView(), projection);
        if(navigator instanceof VRDrawableNavigator) {
            ((VRDrawableNavigator) navigator).onDraw(camera.getView(), projection);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        shader = new Shader(this, R.raw.vertex, R.raw.fragment);
        gridShader = new Shader(this, R.raw.grid_vertex, R.raw.grid_fragment);
        colorShader = new Shader(this, R.raw.color_vertex, R.raw.color_fragment);

        camera = new VRCamera();
        camera.setCanMoveInY(false);
        vrRoom = new VRRoom();
        studentScene = new StudentScene();

        navigator = new Navigator(camera, vrRoom, studentScene);
    }

    @Override
    public void onRendererShutdown() {
        colorShader.dispose();
        gridShader.dispose();
        shader.dispose();
    }

    @Override
    protected void onDestroy() {
        vrRoom.dispose();
        studentScene.dispose();
        FocusManager.getInstance().dispose();
        CollisionManager.getInstance().dispose();
        RendererManager.getInstance().dispose();
        super.onDestroy();
    }
}
