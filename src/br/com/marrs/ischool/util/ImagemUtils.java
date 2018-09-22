package br.com.marrs.ischool.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.DisplayMetrics;
import br.com.marrs.ischool.MainActivity;

public class ImagemUtils {
	
	
    
    public static Bitmap decodeBitmapFromFile(File filePath, int reqWidth, int regHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, regHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath.getAbsolutePath(), options);
    }
    
    public static Bitmap decodeBitmapFromUri(Uri imagem, int reqWidth, int regHeight) throws IOException {

    	InputStream imageStream = MainActivity.instance.getContentResolver().openInputStream(imagem);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream,null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, regHeight);
        options.inJustDecodeBounds = false;
        imageStream = MainActivity.instance.getContentResolver().openInputStream(imagem);
        return  BitmapFactory.decodeStream(imageStream,null, options);
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int regHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    

	    int inSampleSize = 1;
	
	    if (height > regHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > regHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
    
    public static Bitmap circledBitmap(Bitmap bitmap) {
    	
    	Bitmap output;
    	
		 output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		 Canvas canvas = new Canvas(output);

		 int color = Color.RED;
		 Paint paint = new Paint();
		 Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		 RectF rectF = new RectF(rect);

		 paint.setAntiAlias(true);
		 canvas.drawARGB(0, 0, 0, 0);
		 paint.setColor(color);
		 canvas.drawOval(rectF, paint);

		 paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		 canvas.drawBitmap(bitmap, rect, rect, paint);

		 bitmap.recycle();

		 return output;
    }
    
    
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}

}
