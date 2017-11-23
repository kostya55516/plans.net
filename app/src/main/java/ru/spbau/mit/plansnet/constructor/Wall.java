package ru.spbau.mit.plansnet.constructor;

import android.graphics.Point;
import android.util.Log;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Wall extends ru.spbau.mit.plansnet.constructor.MapObjectLinear {

    private static ITextureRegion textureRegion;

    public MapObject copy() {
        MapObject result = new Wall(getVertexBufferObjectManager());
        result.setPosition(getPosition());
        return result;
    }

    public Wall(VertexBufferObjectManager vertexBufferObjectManager) {
        super(textureRegion, vertexBufferObjectManager);
    }

    public static void setTexture(ITextureRegion texture) {
        textureRegion = texture;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (MAP.getTouchState() != 1) {
            return false;
        } else {
            MAP.removeObject(this);
        }
        return false;
    }
}