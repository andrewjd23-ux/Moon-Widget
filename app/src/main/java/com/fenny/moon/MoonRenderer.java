package com.fenny.moon;

import android.graphics.Bitmap;
import android.graphics.Color;

public final class MoonRenderer {
    private MoonRenderer() {}

    public static Bitmap render(MoonPhase phase) {
        int size = 192;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        double angle = 2 * Math.PI * phase.cycle;
        // Orthographic sphere: the sun moves from behind the moon to in front.
        double lightX = Math.sin(angle);
        double lightZ = -Math.cos(angle);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                double nx = (x + 0.5 - size / 2.0) / (size / 2.0 - 2);
                double ny = (y + 0.5 - size / 2.0) / (size / 2.0 - 2);
                double r2 = nx * nx + ny * ny;
                if (r2 > 1) continue;
                double nz = Math.sqrt(1 - r2);
                boolean lit = nx * lightX + nz * lightZ >= 0;
                double shade = 0.78 + 0.22 * nz;
                int red = lit ? (int) (242 * shade) : 40;
                int green = lit ? (int) (237 * shade) : 50;
                int blue = lit ? (int) (218 * shade) : 69;
                int alpha = (int) (255 * Math.min(1, (1 - Math.sqrt(r2)) * size / 2));
                bitmap.setPixel(x, y, Color.argb(alpha, red, green, blue));
            }
        }
        return bitmap;
    }
}
