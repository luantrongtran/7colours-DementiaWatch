package ifn372.sevencolors.dementiawatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by lua on 6/09/2015.
 */
public class BitMapUtils {
    public static Bitmap getMutableBitmapFromResourceFromResource(Drawable drawable, int color){
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap muttableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas myCanvas = new Canvas(muttableBitmap);

        int myColor = muttableBitmap.getPixel(0, 0);
        ColorFilter filter = new LightingColorFilter(myColor, color);

        Paint pnt = new Paint();
        pnt.setColorFilter(filter);
        myCanvas.drawBitmap(muttableBitmap, 0, 0, pnt);

        return muttableBitmap;
    }
}
