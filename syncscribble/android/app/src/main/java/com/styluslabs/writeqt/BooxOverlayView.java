package com.styluslabs.writeqt;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.onyx.android.sdk.data.note.TouchPoint;
import com.onyx.android.sdk.pen.RawInputCallback;
import com.onyx.android.sdk.pen.TouchHelper;
import com.onyx.android.sdk.pen.data.TouchPointList;

import org.libsdl.app.SDLActivity;

import java.util.Collections;
import java.util.List;

import android.view.SurfaceView;
import android.graphics.PixelFormat;

public class BooxOverlayView extends SurfaceView {

public BooxOverlayView(Context context) {
    super(context);
    setZOrderOnTop(true);
    getHolder().setFormat(PixelFormat.TRANSPARENT);
    setFocusable(false);
    // Pass all touch events through to Write
    setClickable(false);
    setLongClickable(false);
}



private long strokeStartTime;
private float strokeStartX;
private float strokeStartY;
private static final long TAP_TIMEOUT = 150; // ms
private static final float TAP_SLOP = 10.0f; // pixels
    private TouchHelper touchHelper;
    private float surfaceWidth = 1.0f;
    private float surfaceHeight = 1.0f;
    private static final int BOOX_TOUCH_DEVICE_ID = 0;
    private static final int BOOX_FINGER_ID = 0;

    public void init(float surfaceW, float surfaceH) {
        this.surfaceWidth = surfaceW;
        this.surfaceHeight = surfaceH;
        setupTouchHelper();
	onResume();
    }

public void updatePenState() {
    if (touchHelper == null) return;
    try {
        float width = MainActivity.nativeGetPenWidth();
        int color = MainActivity.nativeGetPenColor();
        float alpha = MainActivity.nativeGetPenAlpha();
        boolean pressure = MainActivity.nativeGetPenPressure();

        // Convert Write's ARGB color to Android color with alpha applied
        int a = (int)(alpha * 255);
        int androidColor = (color & 0x00FFFFFF) | (a << 24);

        touchHelper.setStrokeWidth((float)width);
        touchHelper.setStrokeColor(androidColor);
        // Note: pressure sensitivity is handled by Write's renderer,
        // Boox SDK preview uses fixed width as approximation
    } catch (Exception e) {
        android.util.Log.e("BooxOverlay", "updatePenState failed: " + e.getMessage());
    }
}

    public void updateSurfaceSize(float w, float h) {
        this.surfaceWidth = w;
        this.surfaceHeight = h;
        updateLimitRect();
    }

private void setupTouchHelper() {
    touchHelper = TouchHelper.create(this, 2, new RawInputCallback() {

	@Override
	public void onBeginRawDrawing(boolean b, TouchPoint tp) {
	    strokeStartTime = System.currentTimeMillis();
	    strokeStartX = tp.x;
	    strokeStartY = tp.y;
	    updatePenState();
	}
        @Override
        public void onRawDrawingTouchPointMoveReceived(TouchPoint tp) {}

        @Override
        public void onRawDrawingTouchPointListReceived(TouchPointList list) {}

        @Override
        public void onEndRawDrawing(boolean b, TouchPoint tp) {}

        @Override
        public void onPenActive(TouchPoint point) {
            touchHelper.setRawDrawingEnabled(true);
        }

        @Override
        public void onPenUpRefresh(android.graphics.RectF refreshRect) {
            postDelayed(() -> {
                if (touchHelper != null) {
                    touchHelper.setRawDrawingRenderEnabled(false);
                }
            }, 200);
        }

        @Override
        public void onBeginRawErasing(boolean b, TouchPoint tp) {}

        @Override
        public void onRawErasingTouchPointMoveReceived(TouchPoint tp) {}

        @Override
        public void onRawErasingTouchPointListReceived(TouchPointList list) {}

        @Override
        public void onEndRawErasing(boolean b, TouchPoint tp) {}
    });

	updateLimitRect(); // this calls setLimitRect inside
	touchHelper.setStrokeWidth(3.0f).openRawDrawing();
	touchHelper.setRawDrawingRenderEnabled(true);
	touchHelper.setPenUpRefreshTimeMs(500);
	touchHelper.enableFingerTouch(true);  // let finger touches pass through
	touchHelper.setPostInputEvent(true);  // forward all non-pen events to the view

}



private void updateLimitRect() {
    if (touchHelper == null) return;
    int w = getWidth();
    int h = getHeight();
    if (w == 0 || h == 0) return;
    final int TOP_BAR = 105;
    final int BOTTOM_BAR = 25;
    touchHelper.setStrokeColor(android.graphics.Color.BLACK);
    touchHelper.setStrokeStyle(TouchHelper.STROKE_STYLE_PENCIL);
    List<Rect> limit = Collections.singletonList(new Rect(0, TOP_BAR, w, h - BOTTOM_BAR));
    touchHelper.setLimitRect(limit, Collections.emptyList());
    updatePenState();  // apply current pen state
}

/*
private void updateLimitRect() {
    if (touchHelper == null) return;
    int w = getWidth();
    int h = getHeight();
    if (w == 0 || h == 0) return;
    
    // Toolbar heights in pixels — adjust these if layout changes
    final int TOP_BAR = 105;    // top toolbar height
    final int BOTTOM_BAR = 25;  // bottom toolbar height (1872 - 1810 = 62)
    
    List<Rect> limit = Collections.singletonList(new Rect(0, TOP_BAR, w, h - BOTTOM_BAR));
    List<Rect> exclude = Collections.emptyList();
    touchHelper.setLimitRect(limit, exclude);
}
*/
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) updateLimitRect();
    }

@Override
public boolean onTouchEvent(MotionEvent event) {
 	return false;
	}

public void onResume() {
    if (touchHelper != null) {
        touchHelper.setRawDrawingEnabled(true);
    }
}

public void onPause() {
    if (touchHelper != null) {
        touchHelper.setRawDrawingEnabled(false);
    }
}

public void onDestroy() {
    if (touchHelper != null) {
        touchHelper.closeRawDrawing();
        touchHelper = null;
    }
}

}
