package com.uos.calculator.thread;

import com.uos.calculator.util.FileManager;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.ImageView;

public class BlurTask {

	private Drawable wallpaperDra;
	private Bitmap wallpaperBmp;
	private Context context;
	private ImageView bg2;
	private Animation fadeIn;
	// private static File cacheDir, compressedWP, compressCount, performanceOK;
	private WallpaperBlurThread wbt;

	private static BlurManager sBlurManager;

	public BlurTask(Context context) {
		this.context = context;
	}

	public BlurTask(Context context, ImageView bg2, Animation fadeIn) {
		this.context = context;
		this.bg2 = bg2;
		this.fadeIn = fadeIn;
		// cacheDir = context.getCacheDir();
		// compressedWP = new File(cacheDir, "compressedWP");
		// compressCount = new File(cacheDir, "compressCount");
		// performanceOK = new File(cacheDir, "performanceOK");

		sBlurManager = BlurManager.getInstance();
	}

	public void launch() {
		WallpaperManager wm = WallpaperManager.getInstance(context);
		try {
			wallpaperBmp = (((BitmapDrawable) wm.getDrawable()).getBitmap());
		} catch (SecurityException se) {
		}

		// if (performanceOK.exists()) {
		// compressedWP.delete();
		/*
		 * } else if (compressedWP.exists() && !compressTimes.exists()) {
		 * System.out.println("A"); BitmapFactory.Options options = new
		 * BitmapFactory.Options(); options.inPreferredConfig =
		 * Bitmap.Config.ARGB_8888; wallpaperBmp =
		 * BitmapFactory.decodeFile(compressedWP.getPath(), options);
		 * //compressedWP.delete();
		 */
		// } else

		/*
		 * if (compressCount.exists() && !performanceOK.exists()) {
		 * System.out.println("B"); cacheWallpaper(); BitmapFactory.Options
		 * options = new BitmapFactory.Options(); options.inPreferredConfig =
		 * Bitmap.Config.ARGB_8888; wallpaperBmp =
		 * BitmapFactory.decodeFile(compressedWP.getPath(), options); //
		 * compressedWP.delete(); }
		 */

		if (wallpaperBmp != null && wallpaperBmp.getByteCount() > 4) {
			wbt = new WallpaperBlurThread(this);
			wbt.start();
		}
	}

	public void reportState(int state) {
		int outState = 0;

		switch (state) {
		case WallpaperBlurThread.BLUR_STATE_COMPLETED:
			/*
			 * compressedWP.delete(); if (!compressCount.exists()) { try {
			 * performanceOK.createNewFile(); } catch (IOException e) { } }
			 */
			outState = BlurManager.TASK_COMPLETE;
			new WallpaperSavingThread(this).start();
			break;
		case WallpaperBlurThread.BLUR_STATE_OUT_OF_MEMORY:
			// cacheWallpaper();

			/*
			 * int count; if (compressCount.exists()) { count = (Integer)
			 * FileManager.read(compressCount); count++; } else { count = 1; }
			 * FileManager.write(compressCount, count); // launch();
			 */
			break;
		case WallpaperBlurThread.BLUR_STATE_ERROR:
			break;
		}
		sBlurManager.handleState(this, outState);
	}

	public void setWallpaper(Bitmap wallpaperBM) {
		this.wallpaperBmp = wallpaperBM;
		this.wallpaperDra = new BitmapDrawable(context.getResources(),
				wallpaperBM);
	}

	public Drawable getWallpaperDrawable() {
		return wallpaperDra;
	}

	public Bitmap getWallpaperBitmap() {
		return wallpaperBmp;
	}

	public ImageView getBg2() {
		return bg2;
	}

	public Context getContext() {
		return context;
	}

	public Animation getFadeIn() {
		return fadeIn;
	}

	/*
	 * private void cacheWallpaper() { if (wallpaperBmp != null) { int quality =
	 * (int) (100 / Math.pow(2, (Integer) FileManager.read(compressCount))); try
	 * { FileOutputStream fos = new FileOutputStream(compressedWP);
	 * wallpaperBmp.compress(Bitmap.CompressFormat.PNG, quality, fos); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } } }
	 */

	public WallpaperBlurThread getWallpaperBlurThread() {
		return wbt;
	}

}
