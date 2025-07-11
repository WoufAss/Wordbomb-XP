package impl.util.image;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ImageObject {

    private BufferedImage img = null;
    @Getter
    private int textureID = 0;
    private int width, height = 0;
    public boolean loading = false;
    public boolean isLoaded = false;
    private final File file;

    public ImageObject(File file) {
        this.file = file;
    }

    public final CompletableFuture<Void> loadAsync() {
        if (loading || isLoaded) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            try {
                loading = true;
                loadImage();
                width = img.getWidth();
                height = img.getHeight();
                int[] pixels = new int[width * height];
                img.getRGB(0, 0, width, height, pixels, 0, width);

                ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int pixel = pixels[y * width + x];
                        buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                        buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                        buffer.put((byte) (pixel & 0xFF));         // Blue
                        buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                    }
                }

                buffer.flip();

                /*Minecraft.getMinecraft().addScheduledTask(() -> {
                    textureID = GL11.glGenTextures();
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

                    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

                    if (Objects.requireNonNull(GL11.glGetString(GL11.GL_EXTENSIONS)).contains("GL_EXT_texture_filter_anisotropic")) {
                        float maxAnisotropy = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
                        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
                    }
                    isLoaded = true;
                });*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadImage() throws IOException {
        if (file != null) loadImageFromDisk();
    }

    private void loadImageFromDisk() throws IOException {
        img = ImageIO.read(file);
    }

    /*
    private void loadImageFromResource() throws IOException {
        InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();
        img = ImageIO.read(inputStream);
    }

    public final void unload() {
        if (!isLoaded)
            return;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GlStateManager.deleteTexture(textureID);
    }

    public final void drawImg(float x, float y, float width, float height) {
        if(!isLoaded)
            return;

        enableTexture2D();
        enableBlend();
        glColor4f(1, 1, 1, 1);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        resetColor();
    }

    public final void drawImg(float x, float y) {
        if(!isLoaded)
            return;

        enableTexture2D();
        enableBlend();
        glColor4f(1, 1, 1, 1);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        resetColor();
    }

    public void drawImg(float x, float y, float width, float height, Color color) {
        if(!isLoaded)
            return;

        enableTexture2D();
        enableBlend();
        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        resetColor();
    }*/
}