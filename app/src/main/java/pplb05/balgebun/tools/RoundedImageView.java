package pplb05.balgebun.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import pplb05.balgebun.R;

/**
 * Created by Wahid Nur Rohman on 4/28/2016.
 */
public class RoundedImageView extends ImageView {
        private final float radius = getContext().getResources().getDimension(R.dimen.corner_radius);
        private RectF mSrcRect = new RectF();
        private RectF mDstRect = new RectF();
        private Path mClipPath = new Path();

        public RoundedImageView(Context context) {
        super(context);
    }

        public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

        public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null && getImageMatrix() != null) {
            mSrcRect.set(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
            getImageMatrix().mapRect(mDstRect, mSrcRect);
            mClipPath.reset();
            mClipPath.addRoundRect(mDstRect, radius, radius, Path.Direction.CW);
            canvas.clipPath(mClipPath);
        }
        super.onDraw(canvas);
    }
}