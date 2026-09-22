package com.ipst.registromultimedia.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.ipst.registromultimedia.R;
import com.ipst.registromultimedia.model.Member;

/**
 * Clase utilitaria para la generación y renderizado dinámico de avatares de perfil con iniciales y colores.
 * 
 * Cumple estrictamente con las responsabilidades de la capa de Presentación en la arquitectura MVVM Clean Architecture.
 * Soporta de manera segura las identificaciones de layout de G1 (Grupo 1: R.id.img_perfil_item) y G2 (R.id.ivAvatar).
 * Respeta los recursos centralizados en res/values (colors.xml, arrays.xml, dimens.xml, strings.xml).
 */
public class AvatarUtils {

    /**
     * Paleta constante de colores ARGB primitivos por defecto (Respaldo / Fallback para G1 y Pruebas Unitarias).
     * 
     * Razón de existencia:
     * 1. Pruebas Unitarias Locales (JVM sin Context de Android):
     *    Durante la ejecución de pruebas en la JVM (app/src/test), el objeto Context es nulo o una maqueta.
     *    Esta paleta permite probar la lógica de generación de color e iniciales sin depender de Android.
     * 2. Resiliencia y Fallback en Tiempo de Ejecución (G1):
     *    Si en algún entorno o layout el Context es nulo o no se puede cargar R.array.avatar_colors desde res/values,
     *    la aplicación utiliza estos colores predefinidos de Material Design para evitar NullPointerException o crashes.
     */
    private static final int[] DEFAULT_PALETTE = new int[] {
        0xFF006341, // Verde Institucional
        0xFF1E88E5, // Azul
        0xFFE53935, // Rojo
        0xFF8E24AA, // Púrpura
        0xFF00897B, // Verde Agua (Teal)
        0xFFFB8C00, // Naranja
        0xFF3949AB, // Índigo
        0xFFD81B60, // Rosa
        0xFF43A047, // Verde
        0xFF5E35B1, // Púrpura Oscuro
        0xFF00ACC1, // Cian
        0xFF6D4C41  // Marrón
    };

    /**
     * Extrae las iniciales en mayúsculas a partir del nombre completo de un integrante.
     * 
     * Reglas de procesamiento (G1 / Dominio):
     * - Si el nombre es compuesto (ej. "Carlos Mendoza"), retorna las iniciales del primer y último nombre ("CM").
     * - Si es un único nombre (ej. "Ana"), retorna la primera letra en mayúscula ("A").
     * - Si es nulo o vacío, retorna el carácter por defecto "?".
     *
     * @param name Nombre completo del integrante.
     * @return Cadena con las iniciales procesadas en mayúsculas.
     */
    public static String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        String first = parts[0];
        String last = parts[parts.length - 1];
        StringBuilder initials = new StringBuilder();
        if (!first.isEmpty()) initials.append(first.substring(0, 1).toUpperCase());
        if (!last.isEmpty()) initials.append(last.substring(0, 1).toUpperCase());
        return initials.length() > 0 ? initials.toString() : "?";
    }

    /**
     * Obtiene un color ARGB entero determinista asociado al nombre del integrante utilizando res/values/arrays.xml.
     * 
     * Si el Context está disponible, consulta R.array.avatar_colors cargado desde colors.xml.
     * En caso contrario, recurre a la paleta estática de respaldo DEFAULT_PALETTE.
     *
     * @param context Contexto de la aplicación o vista (puede ser null en pruebas JVM).
     * @param name Nombre del integrante para calcular el hash.
     * @return Valor int del color ARGB asignado de forma determinista.
     */
    public static int getColorForName(Context context, String name) {
        if (context != null) {
            try {
                int[] colors = context.getResources().getIntArray(R.array.avatar_colors);
                if (colors != null && colors.length > 0) {
                    int hash = Math.abs(name != null ? name.trim().hashCode() : 0);
                    return colors[hash % colors.length];
                }
            } catch (Exception ignored) {
                // Caída suave hacia la paleta por defecto si no existe la matriz de recursos
            }
        }
        return getColorForName(name);
    }

    /**
     * Obtiene un color ARGB entero determinista asociado al nombre del integrante usando DEFAULT_PALETTE.
     *
     * @param name Nombre del integrante.
     * @return Valor int del color ARGB asignado según la paleta estática.
     */
    public static int getColorForName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return DEFAULT_PALETTE[0];
        }
        int hash = Math.abs(name.trim().hashCode());
        int index = hash % DEFAULT_PALETTE.length;
        return DEFAULT_PALETTE[index];
    }

    /**
     * Crea un Bitmap circular de avatar a partir de la entidad de dominio Member.
     *
     * @param context Contexto de la aplicación.
     * @param member Objeto entidad Member del modelo de dominio.
     * @param sizePx Tamaño en píxeles del lado del avatar (ancho y alto).
     * @return Imagen Bitmap circular con fondo de color e iniciales.
     */
    public static Bitmap createAvatarBitmap(Context context, Member member, int sizePx) {
        if (member == null) {
            return createAvatarBitmap(context, "?", "?", sizePx);
        }
        return createAvatarBitmap(context, member.getName(), member.getInitials(), sizePx);
    }

    /**
     * Crea un Bitmap circular de avatar a partir del nombre del integrante.
     *
     * @param context Contexto de la aplicación.
     * @param name Nombre del integrante.
     * @param sizePx Tamaño en píxeles del avatar.
     * @return Imagen Bitmap circular renderizada.
     */
    public static Bitmap createAvatarBitmap(Context context, String name, int sizePx) {
        return createAvatarBitmap(context, name, getInitials(name), sizePx);
    }

    /**
     * Crea un Bitmap circular de avatar sin necesidad de Context (para pruebas unitarias JVM).
     *
     * @param name Nombre del integrante.
     * @param sizePx Tamaño en píxeles.
     * @return Imagen Bitmap renderizada con la paleta por defecto.
     */
    public static Bitmap createAvatarBitmap(String name, int sizePx) {
        return createAvatarBitmap(null, name, getInitials(name), sizePx);
    }

    /**
     * Método central de renderizado: Dibuja un círculo relleno con el color asignado y las iniciales centradas en color blanco.
     *
     * @param context Contexto de recursos Android.
     * @param name Nombre para determinar el color de fondo.
     * @param initials Iniciales a dibujar en el centro.
     * @param sizePx Dimensión en píxeles de la imagen cuadrada resultante.
     * @return Bitmap renderizado con el avatar resultante.
     */
    public static Bitmap createAvatarBitmap(Context context, String name, String initials, int sizePx) {
        if (sizePx <= 0) {
            sizePx = 128;
        }

        Bitmap bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Pincel de fondo circular utilizando colores de res/values
        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(getColorForName(context, name));
        backgroundPaint.setStyle(Paint.Style.FILL);

        float radius = sizePx / 2f;
        canvas.drawCircle(radius, radius, radius, backgroundPaint);

        // Pincel de texto para las iniciales (0xFFFFFFFF = Blanco puro)
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setTextSize(sizePx * 0.4f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        String textToDraw = (initials != null && !initials.isEmpty()) ? initials : "?";

        Rect bounds = new Rect();
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length(), bounds);
        float y = radius + (bounds.height() / 2f) - bounds.bottom;

        canvas.drawText(textToDraw, radius, y, textPaint);

        return bitmap;
    }

    /**
     * Crea un objeto Drawable envuelto para ser asignado directamente a vistas ImageView en la UI.
     *
     * @param context Contexto para recursos.
     * @param member Entidad Member.
     * @param sizePx Tamaño del avatar.
     * @return Drawable contenedor del avatar.
     */
    public static Drawable createAvatarDrawable(Context context, Member member, int sizePx) {
        Bitmap bitmap = createAvatarBitmap(context, member, sizePx);
        return context != null ? new BitmapDrawable(context.getResources(), bitmap) : new BitmapDrawable(bitmap);
    }

    /**
     * Crea un objeto Drawable envuelto a partir del nombre del integrante.
     *
     * @param context Contexto de la app.
     * @param name Nombre del integrante.
     * @param sizePx Tamaño en píxeles.
     * @return Drawable renderizado.
     */
    public static Drawable createAvatarDrawable(Context context, String name, int sizePx) {
        Bitmap bitmap = createAvatarBitmap(context, name, sizePx);
        return context != null ? new BitmapDrawable(context.getResources(), bitmap) : new BitmapDrawable(bitmap);
    }

    /**
     * Configura de forma segura el avatar dinámico en un ImageView para layouts G1 (R.id.img_perfil_item) o G2 (R.id.ivAvatar).
     *
     * @param imageView Vista ImageView de destino.
     * @param member Entidad de dominio Member.
     */
    public static void setAvatar(ImageView imageView, Member member) {
        if (imageView == null || member == null) return;
        setAvatar(imageView, member.getName(), member.getInitials());
    }

    /**
     * Configura el avatar en un ImageView a partir del nombre del integrante.
     *
     * @param imageView Vista ImageView de destino.
     * @param name Nombre del integrante.
     */
    public static void setAvatar(ImageView imageView, String name) {
        if (imageView == null) return;
        setAvatar(imageView, name, getInitials(name));
    }

    /**
     * Configura el avatar dinámico en un ImageView a partir del nombre e iniciales calculadas.
     *
     * @param imageView Vista ImageView de destino.
     * @param name Nombre para cálculo de color.
     * @param initials Iniciales a mostrar.
     */
    public static void setAvatar(ImageView imageView, String name, String initials) {
        if (imageView == null) return;
        Context context = imageView.getContext();
        int size = imageView.getWidth();
        if (size <= 0 && imageView.getLayoutParams() != null) {
            size = imageView.getLayoutParams().width;
        }
        if (size <= 0 && context != null) {
            try {
                size = context.getResources().getDimensionPixelSize(R.dimen.avatar_size_large);
            } catch (Exception ignored) {
                size = 128;
            }
        }
        if (size <= 0) {
            size = 128; // Tamaño por defecto en px
        }

        Bitmap bitmap = createAvatarBitmap(context, name, initials, size);
        Drawable drawable = context != null ? new BitmapDrawable(context.getResources(), bitmap) : new BitmapDrawable(bitmap);
        imageView.setImageDrawable(drawable);
    }
}
