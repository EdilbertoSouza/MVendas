package br.com.mvendas.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtils {

	public static Bitmap lerImagemBitmap(File arquivo) {
		if(arquivo.getParentFile() == null || (arquivo.getParentFile() != null && !arquivo.getParentFile().exists())){
			arquivo.getParentFile().mkdir();
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(arquivo.getAbsolutePath(), options);
		
		return bitmap;
	}

	public static boolean salvaImagemBitmap(Bitmap bitmap, File arquivo) {		
		try {
			if(arquivo.getParentFile() == null || (arquivo.getParentFile() != null && !arquivo.getParentFile().exists())){
				arquivo.getParentFile().mkdir();
			}
			
			FileOutputStream out = new FileOutputStream(arquivo);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
			
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f) {
		if(f == null){
			return null;
		}
		
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

		} catch (FileNotFoundException e) {

		}

		return null;
	}
	
}
