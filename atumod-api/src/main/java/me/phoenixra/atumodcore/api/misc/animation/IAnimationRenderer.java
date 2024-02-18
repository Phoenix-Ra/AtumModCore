package me.phoenixra.atumodcore.api.misc.animation;

public interface IAnimationRenderer {
    /**
     * Needs to be called every tick in the render event.
     */
     void render();

    /**
     * This overrides the specified height and width values and stretches the animation over the whole screen.
     * @param b b
     */
     void setStretchImageToScreenSize(boolean b);

     boolean isStretchedToScreenSize();

    /**
     * Only has affect if the animation isn't getting looped.
     * @param b b
     */
     void setHideAfterLastFrame(boolean b);

    /**
     * Returns true if the animation is finished.<br>
     * <b>Thats never the case if the animation is getting looped!</b>
     *
     * @return True if the animation is finished.
     */
     boolean isFinished();

     void setWidth(int width);

     int getWidth();

     void setHeight(int height);

     int getHeight();

     void setPosX(int x);

     int getPosX();

     void setPosY(int y);

     int getPosY();

     int currentFrame();

     int animationFrames();

     void resetAnimation();

     boolean isReady();

     void prepareAnimation();

     String getPath();

     void setFPS(int fps);

     boolean isGettingLooped();

     void setLooped(boolean b);

     int getFPS();

     void setOpacity(float opacity);
}
