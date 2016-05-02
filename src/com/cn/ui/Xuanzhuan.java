package com.cn.ui;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Xuanzhuan {
	// static Bitmap b2;
	public static Bitmap rotate(Bitmap b, int degrees) {
		Bitmap b2 = null;
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, b.getWidth() / 2, b.getHeight() / 2);
			try {
				b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
						m, true);
				if (b != b2) {
					b = b2;

				}
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
			} 
		}
		return b;
	}
}
