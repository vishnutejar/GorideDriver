package com.tranxit.enterprise.data.network;

public interface FloatingViewListener {

    /**
     * FloatingViewを終了する際に呼び出されます。
     */
    void onFinishFloatingView();

    /**
     * Callback when touch action finished.
     *
     * @param isFinishing Whether FloatingView is being deleted or not.
     * @param x           x coordinate
     * @param y           y coordinate
     */
    void onTouchFinished(boolean isFinishing, int x, int y);

}
