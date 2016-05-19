package cg.edu.hm.pohl;

import ba.pohl1.hm.edu.vrlibrary.maths.Vector3;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.arrow.LockedArrowTapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.waypoint.WaypointNavigator;
import ba.pohl1.hm.edu.vrlibrary.ui.AbstractCardboardActivity;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;
import ba.pohl1.hm.edu.vrlibrary.model.shapes.VRRoom;
import cg.edu.hm.pohl.student.JetFuelCantMeltDankMemes;

/**
 * Created by Pohl on 14.04.2016.
 */
public class CardboardGraphicsActivity extends AbstractCardboardActivity {

    public static Shader studentSceneShader;
    private static final int WAYPOINTS = 6;
    private static final float WAYPOINTS_DISTANCE = 2.5f;

    private JetFuelCantMeltDankMemes jetFuelCantMeltDankMemes;

    @Override
    protected int getCardboardViewId() {
        return R.id.cardboard_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.student_scene;
    }

    @Override
    protected float getMoveModifier() {
        return 0.035f;
    }

    @Override
    protected VRComponent createScene() {
        CardboardGraphics.camera.translateY(2f);
        CardboardGraphics.camera.setCanMoveInY(false);
        setNavigator(new LockedArrowTapNavigator());

        final VRRoom vrRoom = new VRRoom(25, 10, 25);

        jetFuelCantMeltDankMemes = new JetFuelCantMeltDankMemes();
        vrRoom.add(jetFuelCantMeltDankMemes);
        return vrRoom;
    }

    @Override
    public void setNavigator(VRNavigator navigator) {
        super.setNavigator(navigator);
        if(navigator instanceof WaypointNavigator) {
            setWaypoints();
        }
    }

    @Override
    protected void initShaders() {
        super.initShaders();
        studentSceneShader = new Shader(R.raw.vertex, R.raw.fragment);
    }

    @Override
    public void onRendererShutdown() {
        super.onRendererShutdown();
        studentSceneShader.dispose();
    }

    private void setWaypoints() {
        // Clear all default way points
        getNavigator().dispose();

        final Vector3 scenePos = jetFuelCantMeltDankMemes.getPosition();
        float x = scenePos.x;
        float y = scenePos.y;
        float z = scenePos.z;
        float radius = WAYPOINTS_DISTANCE;
        float deltaAngle = 360 / WAYPOINTS;
        for(int angle = 0; angle < 360; angle += deltaAngle) {
            final float rX = (float) Math.cos(Math.toRadians(angle)) * radius;
            final float rZ = (float) Math.sin(Math.toRadians(angle)) * radius;
            ((WaypointNavigator) getNavigator()).addWaypointAt(0.2f, x + rX, y + 1f, z + rZ);
        }
    }
}
