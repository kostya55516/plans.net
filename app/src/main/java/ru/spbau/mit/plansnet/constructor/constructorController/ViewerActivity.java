package ru.spbau.mit.plansnet.constructor.constructorController;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;

import java.util.concurrent.Semaphore;

import ru.spbau.mit.plansnet.R;
import ru.spbau.mit.plansnet.constructor.objects.RoomSprite;

public class ViewerActivity extends BaseConstructorActivity {

    private void createBorder(@NonNull Scene pScene) {
        Line[] border = {
                new Line(0, 0, 0, MAP_HEIGHT, 3,
                        getVertexBufferObjectManager()),
                new Line(MAP_WIDTH, 0, MAP_WIDTH, MAP_HEIGHT, 3,
                        getVertexBufferObjectManager()),
                new Line(0, 0, MAP_WIDTH, 0, 3,
                        getVertexBufferObjectManager()),
                new Line(0, MAP_HEIGHT, MAP_WIDTH, MAP_HEIGHT, 3,
                        getVertexBufferObjectManager())
        };
        for (Line line : border) {
            line.setColor(0.7f, 0.7f, 0.7f);
            pScene.attachChild(line);
        }
    }

    private void initScene(@NonNull Scene pScene) {
        pScene.setOnSceneTouchListener((pScene1, pSceneTouchEvent) -> {
            mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);
            MotionEvent event = pSceneTouchEvent.getMotionEvent();
            if (pSceneTouchEvent.isActionUp() &&
                    event.getEventTime() - event.getDownTime() < 200) {
                RoomSprite room = mMap.getRoomTouchedOrNull(pSceneTouchEvent);
                if (room != null) {
                    showParams(room);
                }
            } else {
                moveMap(pSceneTouchEvent);
            }
            return false;
        });
        pScene.setTouchAreaBindingOnActionDownEnabled(true);
        pScene.setTouchAreaBindingOnActionMoveEnabled(true);
        pScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
    }

    @Override
    protected @NonNull Scene onCreateScene() {
        final Scene scene = new Scene();
        scene.setBackground(new Background(0.95f, 0.95f, 1f));
        createBorder(scene);
        initScene(scene);
        initPinchZoomDetector();
        initSprites();
        initMap(scene);
        scene.sortChildren();
        return scene;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_viewer;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.viewerView;
    }

    public void showParams(@NonNull RoomSprite pRoom) {
        Semaphore mutex = new Semaphore(1);
        runOnUiThread(() -> {
            findViewById(R.id.viewerView).setEnabled(false);
            View paramsView = findViewById(R.id.roomPropertiesView);
            paramsView.setVisibility(View.VISIBLE);
            TextView roomName = findViewById(R.id.roomPropertiesName);
            TextView roomDescription = findViewById(R.id.roomPropertiesDescription);
            roomName.setText(pRoom.getmTitle());
            roomDescription.setText(pRoom.getmDescription());
            paramsView.findViewById(R.id.roomPropertiesOk).setOnClickListener(v1 -> {
                paramsView.setVisibility(View.GONE);
                findViewById(R.id.viewerView).setEnabled(true);
            });
        });
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}