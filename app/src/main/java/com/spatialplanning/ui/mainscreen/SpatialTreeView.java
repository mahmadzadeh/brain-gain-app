package com.spatialplanning.ui.mainscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.monkeyladder.R;
import com.spatialplanning.game.SlotId;
import com.spatialplanning.game.SpatialTree;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpatialTreeView extends View {

    // Target frame matches the reference image: one central vertical stick
    // and three horizontal sticks (top, middle, bottom).
    private static final float CENTER_X = 0.50f;
    private static final float Y_TOP = 0.22f;
    private static final float Y_MID = 0.40f;
    private static final float Y_BOT = 0.58f;

    private static final Map<SlotId, float[]> SLOT_POSITIONS = buildSlotPositions();

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint emptyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int ballColor;
    private int selectedBallColor;

    private SpatialTree targetTree;
    private final Set<SlotId> visibleAnimatedSlots = new HashSet<>();
    private SlotId selectedSlot;
    private OnSlotTappedListener listener;
    private float ballRadius;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable animationStep;
    private boolean isAnimatingRoundStart = false;

    public interface OnSlotTappedListener {
        void onSlotTapped(SlotId slotId);
    }

    public SpatialTreeView(Context context) {
        super(context);
        init();
    }

    public SpatialTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpatialTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int lineColor = getResources().getColor(R.color.colorPrimaryDark, null);
        ballColor = getResources().getColor(R.color.colorPrimary, null);
        selectedBallColor = getResources().getColor(R.color.monkeyLadderCellLight, null);

        linePaint.setColor(lineColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        ballPaint.setColor(ballColor);
        ballPaint.setStyle(Paint.Style.FILL);

        emptyPaint.setColor(lineColor);
        emptyPaint.setStyle(Paint.Style.STROKE);
        emptyPaint.setStrokeWidth(3f);

        textPaint.setColor(getResources().getColor(R.color.white, null));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setTree(SpatialTree tree) {
        stopRoundStartAnimation();
        this.targetTree = tree;
        invalidate();
    }

    public void animateRoundStart(SpatialTree tree) {
        stopRoundStartAnimation();
        this.targetTree = tree;
        this.selectedSlot = null;
        this.visibleAnimatedSlots.clear();
        this.isAnimatingRoundStart = true;
        invalidate();

        final SlotId[] revealOrder = new SlotId[]{
                SlotId.TOP_LEFT,
                SlotId.TOP_RIGHT,
                SlotId.MID_LEFT_OUTER,
                SlotId.MID_LEFT_INNER,
                SlotId.MID_RIGHT_INNER,
                SlotId.MID_RIGHT_OUTER,
                SlotId.BOTTOM_LEFT_OUTER,
                SlotId.BOTTOM_LEFT_MIDDLE,
                SlotId.BOTTOM_LEFT_INNER,
                SlotId.BOTTOM_RIGHT_INNER,
                SlotId.BOTTOM_RIGHT_MIDDLE,
                SlotId.BOTTOM_RIGHT_OUTER
        };
        final int[] index = {0};
        animationStep = new Runnable() {
            @Override
            public void run() {
                while (index[0] < revealOrder.length &&
                        (targetTree == null || !targetTree.hasBallAt(revealOrder[index[0]]))) {
                    index[0]++;
                }
                if (index[0] >= revealOrder.length) {
                    isAnimatingRoundStart = false;
                    animationStep = null;
                    invalidate();
                    return;
                }
                visibleAnimatedSlots.add(revealOrder[index[0]]);
                index[0]++;
                invalidate();
                handler.postDelayed(this, 70L);
            }
        };
        handler.postDelayed(animationStep, 70L);
    }

    public void setSelectedSlot(SlotId slotId) {
        this.selectedSlot = slotId;
        invalidate();
    }

    public void clearSelection() {
        this.selectedSlot = null;
        invalidate();
    }

    public void setOnSlotTappedListener(OnSlotTappedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (targetTree == null) return;

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        ballRadius = size * 0.0675f;
        textPaint.setTextSize(ballRadius * 1.1f);
        linePaint.setStrokeWidth(size * 0.03f);

        drawFrame(canvas, w, h);
        drawSlots(canvas, w, h);
    }

    private void drawFrame(Canvas canvas, int w, int h) {
        // Single central vertical stick.
        float[] vTop = toPixel(CENTER_X, Y_TOP - 0.08f, w, h);
        float[] vBottom = toPixel(CENTER_X, Y_BOT + 0.10f, w, h);
        canvas.drawLine(vTop[0], vTop[1], vBottom[0], vBottom[1], linePaint);

        // Horizontal sticks are independent from ball centers.
        // Balls are placed on pegs along these lines, not on the junction points.
        float[] topL = toPixel(CENTER_X - 0.24f, Y_TOP, w, h);
        float[] topR = toPixel(CENTER_X + 0.24f, Y_TOP, w, h);
        canvas.drawLine(topL[0], topL[1], topR[0], topR[1], linePaint);

        float[] midL = toPixel(CENTER_X - 0.408f, Y_MID, w, h);
        float[] midR = toPixel(CENTER_X + 0.408f, Y_MID, w, h);
        canvas.drawLine(midL[0], midL[1], midR[0], midR[1], linePaint);

        float[] botL = toPixel(CENTER_X - 0.576f, Y_BOT, w, h);
        float[] botR = toPixel(CENTER_X + 0.576f, Y_BOT, w, h);
        canvas.drawLine(botL[0], botL[1], botR[0], botR[1], linePaint);
    }

    private void drawSlots(Canvas canvas, int w, int h) {
        for (SlotId slot : SlotId.values()) {
            float[] center = slotCenter(slot, w, h);
            float cx = center[0];
            float cy = center[1];

            boolean hasBall = targetTree != null && targetTree.hasBallAt(slot);
            if (hasBall && isAnimatingRoundStart && !visibleAnimatedSlots.contains(slot)) {
                continue;
            }

            if (hasBall) {
                ballPaint.setColor(slot == selectedSlot ? selectedBallColor : ballColor);
                canvas.drawCircle(cx, cy, ballRadius, ballPaint);

                int value = targetTree.ballAt(slot);
                float textY = cy - (textPaint.descent() + textPaint.ascent()) / 2;
                canvas.drawText(String.valueOf(value), cx, textY, textPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isAnimatingRoundStart) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN && listener != null && targetTree != null) {
            float x = event.getX();
            float y = event.getY();
            int w = getWidth();
            int h = getHeight();
            float tapRadius = ballRadius * 1.5f;

            for (SlotId slot : SlotId.values()) {
                float[] center = slotCenter(slot, w, h);
                float dx = x - center[0];
                float dy = y - center[1];
                if (dx * dx + dy * dy <= tapRadius * tapRadius) {
                    performClick();
                    listener.onSlotTapped(slot);
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private float[] toPixel(float normX, float normY, int viewW, int viewH) {
        float padding = ballRadius > 0 ? ballRadius + 8 : viewW * 0.08f;
        float usableW = viewW - 2 * padding;
        float usableH = viewH - 2 * padding;
        return new float[]{padding + normX * usableW, padding + normY * usableH};
    }

    private float[] slotCenter(SlotId slot, int viewW, int viewH) {
        float[] norm = SLOT_POSITIONS.get(slot);
        if (norm == null) {
            throw new IllegalStateException("No coordinates configured for slot: " + slot);
        }
        return toPixel(norm[0], norm[1], viewW, viewH);
    }

    /**
     * Ball coordinates on pegs, matched to the provided reference:
     *
     *             [1]   [2]
     *         [3] [4]   [5] [6]
     *      [7] [8] [9] [ ] [ ] [ ]
     *
     * The vertical stick runs at CENTER_X and crosses bars at joints with no balls.
     */
    private static Map<SlotId, float[]> buildSlotPositions() {
        Map<SlotId, float[]> pos = new EnumMap<>(SlotId.class);

        float x0 = CENTER_X - 0.425f;
        float x1 = CENTER_X - 0.255f;
        float x2 = CENTER_X - 0.085f;
        float x3 = CENTER_X + 0.085f;
        float x4 = CENTER_X + 0.255f;
        float x5 = CENTER_X + 0.425f;

        // Top bar: 2 pegs
        pos.put(SlotId.TOP_LEFT, new float[]{x2, Y_TOP});
        pos.put(SlotId.TOP_RIGHT, new float[]{x3, Y_TOP});

        // Middle bar: 4 pegs
        pos.put(SlotId.MID_LEFT_OUTER, new float[]{x1, Y_MID});
        pos.put(SlotId.MID_LEFT_INNER, new float[]{x2, Y_MID});
        pos.put(SlotId.MID_RIGHT_INNER, new float[]{x3, Y_MID});
        pos.put(SlotId.MID_RIGHT_OUTER, new float[]{x4, Y_MID});

        // Bottom bar: 6 pegs (left 3 used in solved state)
        pos.put(SlotId.BOTTOM_LEFT_OUTER, new float[]{x0, Y_BOT});
        pos.put(SlotId.BOTTOM_LEFT_MIDDLE, new float[]{x1, Y_BOT});
        pos.put(SlotId.BOTTOM_LEFT_INNER, new float[]{x2, Y_BOT});
        pos.put(SlotId.BOTTOM_RIGHT_INNER, new float[]{x3, Y_BOT});
        pos.put(SlotId.BOTTOM_RIGHT_MIDDLE, new float[]{x4, Y_BOT});
        pos.put(SlotId.BOTTOM_RIGHT_OUTER, new float[]{x5, Y_BOT});

        return pos;
    }

    private void stopRoundStartAnimation() {
        if (animationStep != null) {
            handler.removeCallbacks(animationStep);
            animationStep = null;
        }
        isAnimatingRoundStart = false;
        visibleAnimatedSlots.clear();
    }
}
