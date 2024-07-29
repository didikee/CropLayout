package com.github.croper.cropwindow.edge;//package com.didikee.croper.cropwindow.edge;
//
//import android.graphics.RectF;
//import android.support.annotation.NonNull;
//
//import AspectRatioUtil;
//
///**
// * user author: didikee
// * create time: 3/20/19 5:26 PM
// * description:
// */
//public class EdgeHolder {
//    public static final int MIN_CROP_LENGTH_PX = 40;
//    public static final int LEFT = 0;
//    public static final int TOP = 1;
//    public static final int RIGHT = 2;
//    public static final int BOTTOM = 3;
//
//    private float mLeft;
//    private float mTop;
//    private float mRight;
//    private float mBottom;
//
//    /**
//     * Sets the coordinate of the Edge. The coordinate will represent the x-coordinate for LEFT and
//     * RIGHT Edges and the y-coordinate for TOP and BOTTOM edges.
//     *
//     * @param coordinate the position of the edge
//     */
//    public void setCoordinate(int who, float coordinate) {
//        switch (who) {
//            case LEFT:
//                mLeft = coordinate;
//                break;
//            case TOP:
//                mTop = coordinate;
//                break;
//            case RIGHT:
//                mRight = coordinate;
//                break;
//            case BOTTOM:
//                mBottom = coordinate;
//                break;
//            default:
//                //do nothing
//                break;
//        }
//    }
//
//    /**
//     * Add the given number of pixels to the current coordinate position of this Edge.
//     *
//     * @param distance the number of pixels to add
//     */
//    public void offset(int who, float distance) {
//        switch (who) {
//            case LEFT:
//                mLeft += distance;
//                break;
//            case TOP:
//                mTop += distance;
//                break;
//            case RIGHT:
//                mRight += distance;
//                break;
//            case BOTTOM:
//                mBottom += distance;
//                break;
//            default:
//                //do nothing
//                break;
//        }
//    }
//
//    /**
//     * Gets the coordinate of the Edge
//     *
//     * @return the Edge coordinate (x-coordinate for LEFT and RIGHT Edges and the y-coordinate for
//     * TOP and BOTTOM edges)
//     */
//    public float getCoordinate(int who) {
//        switch (who) {
//            case LEFT:
//                return mLeft;
//            case TOP:
//                return mTop;
//            case RIGHT:
//                return mRight;
//            case BOTTOM:
//                return mBottom;
//            default:
//                //do nothing
//                break;
//        }
//        return 0;
//    }
//
//
//    /**
//     * Sets the Edge to the given x-y coordinate but also adjusting for snapping to the image bounds
//     * and parent view border constraints.
//     *
//     * @param x               the x-coordinate
//     * @param y               the y-coordinate
//     * @param imageRect       the bounding rectangle of the image
//     * @param imageSnapRadius the radius (in pixels) at which the edge should snap to the image
//     */
//    public void adjustCoordinate(int who, float x, float y, @NonNull RectF imageRect, float imageSnapRadius, float aspectRatio) {
//
//        switch (who) {
//            case LEFT:
//                mLeft = adjustLeft(x, imageRect, imageSnapRadius, aspectRatio);
//                break;
//            case TOP:
//                mTop = adjustTop(y, imageRect, imageSnapRadius, aspectRatio);
//                break;
//            case RIGHT:
//                mRight = adjustRight(x, imageRect, imageSnapRadius, aspectRatio);
//                break;
//            case BOTTOM:
//                mBottom = adjustBottom(y, imageRect, imageSnapRadius, aspectRatio);
//                break;
//        }
//    }
//
//
//    /**
//     * Adjusts this Edge position such that the resulting window will have the given aspect ratio.
//     *
//     * @param aspectRatio the aspect ratio to achieve
//     */
//    public void adjustCoordinate(int who, float aspectRatio) {
//
//        final float left = mLeft;
//        final float top = mTop;
//        final float right = mRight;
//        final float bottom = mBottom;
//
//        switch (who) {
//            case LEFT:
//                mLeft = AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio);
//                break;
//            case TOP:
//                mTop = AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio);
//                break;
//            case RIGHT:
//                mRight = AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio);
//                break;
//            case BOTTOM:
//                mBottom = AspectRatioUtil.calculateBottom(left, top, right, aspectRatio);
//                break;
//        }
//    }
//
//    /**
//     * Returns whether or not you can re-scale the image based on whether any edge would be out of
//     * bounds. Checks all the edges for a possibility of jumping out of bounds.
//     *
//     * @param edge        the Edge that is about to be expanded
//     * @param imageRect   the rectangle of the picture
//     * @param aspectRatio the desired aspectRatio of the picture
//     *
//     * @return whether or not the new image would be out of bounds.
//     */
//    public boolean isNewRectangleOutOfBounds(int who, int edge,/*@NonNull Edge edge,*/@NonNull RectF imageRect, float aspectRatio) {
//
////        float offset = edge.snapOffset(imageRect);
//        float offset = snapOffset(who, imageRect);
//        switch (who) {
//            case LEFT:
//                if (edge == TOP) {
//
//                    final float top = imageRect.top;
//                    final float bottom = mBottom - offset;
//                    final float right = mRight;
//                    final float left = AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//
//                } else if (edge == BOTTOM) {
//
//                    final float bottom = imageRect.bottom;
//                    final float top = mTop - offset;
//                    final float right = mRight;
//                    final float left = AspectRatioUtil.calculateLeft(top, right, bottom, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//                }
//                break;
//
//            case TOP:
//
//                if (edge == LEFT) {
//
//                    final float left = imageRect.left;
//                    final float right = mRight - offset;
//                    final float bottom = mBottom;
//                    final float top = AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//
//                } else if (edge == RIGHT) {
//
//                    final float right = imageRect.right;
//                    final float left = mLeft - offset;
//                    final float bottom = mBottom;
//                    final float top = AspectRatioUtil.calculateTop(left, right, bottom, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//                }
//                break;
//
//            case RIGHT:
//
//                if (edge == TOP) {
//
//                    final float top = imageRect.top;
//                    final float bottom = mBottom - offset;
//                    final float left = mLeft;
//                    final float right = AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//
//                } else if (edge == BOTTOM) {
//
//                    final float bottom = imageRect.bottom;
//                    final float top = mTop - offset;
//                    final float left = mLeft;
//                    final float right = AspectRatioUtil.calculateRight(left, top, bottom, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//                }
//                break;
//
//            case BOTTOM:
//
//                if (edge == LEFT) {
//
//                    final float left = imageRect.left;
//                    final float right = mRight - offset;
//                    final float top = mTop;
//                    final float bottom = AspectRatioUtil.calculateBottom(left, top, right, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//
//                } else if (edge == RIGHT) {
//
//                    final float right = imageRect.right;
//                    final float left = mLeft - offset;
//                    final float top = mTop;
//                    final float bottom = AspectRatioUtil.calculateBottom(left, top, right, aspectRatio);
//
//                    return isOutOfBounds(top, left, bottom, right, imageRect);
//
//                }
//                break;
//        }
//        return true;
//    }
//
//    /**
//     * Returns whether the new rectangle would be out of bounds.
//     *
//     * @param imageRect the Image to be compared with
//     *
//     * @return whether it would be out of bounds
//     */
//    private boolean isOutOfBounds(float top, float left, float bottom, float right, @NonNull RectF imageRect) {
//        return (top < imageRect.top || left < imageRect.left || bottom > imageRect.bottom || right > imageRect.right);
//    }
//
//    /**
//     * Snap this Edge to the given image boundaries.
//     *
//     * @param imageRect the bounding rectangle of the image to snap to
//     *
//     * @return the amount (in pixels) that this coordinate was changed (i.e. the new coordinate
//     * minus the old coordinate value)
//     */
//    public float snapToRect(int who, @NonNull RectF imageRect) {
//
//        float oldCoordinate = 0f;
//        float newCoordinate = 0f;
//
//        switch (who) {
//            case LEFT:
//                oldCoordinate = mLeft;
//                mLeft = imageRect.left;
//                newCoordinate = mLeft;
//                break;
//            case TOP:
//                oldCoordinate = mTop;
//                mTop = imageRect.top;
//                newCoordinate = mTop;
//                break;
//            case RIGHT:
//                oldCoordinate = mRight;
//                mRight = imageRect.right;
//                newCoordinate = mRight;
//                break;
//            case BOTTOM:
//                oldCoordinate = mBottom;
//                mBottom = imageRect.bottom;
//                newCoordinate = mBottom;
//                break;
//        }
//
//        return newCoordinate - oldCoordinate;
//    }
//
//    /**
//     * Returns the potential snap offset of snapToRect, without changing the coordinate.
//     *
//     * @param imageRect the bounding rectangle of the image to snap to
//     *
//     * @return the amount (in pixels) that this coordinate was changed (i.e. the new coordinate
//     * minus the old coordinate value)
//     */
//    public float snapOffset(int who, @NonNull RectF imageRect) {
//
//        final float oldCoordinate;
//        final float newCoordinate;
//
//        switch (who) {
//            case LEFT:
//                oldCoordinate = mLeft;
//                newCoordinate = imageRect.left;
//                break;
//            case TOP:
//                oldCoordinate = mTop;
//                newCoordinate = imageRect.top;
//                break;
//            case RIGHT:
//                oldCoordinate = mRight;
//                newCoordinate = imageRect.right;
//                break;
//            default: // BOTTOM
//                oldCoordinate = mBottom;
//                newCoordinate = imageRect.bottom;
//                break;
//        }
//
//        return newCoordinate - oldCoordinate;
//    }
//
//    /**
//     * Gets the current width of the crop window.
//     */
//    public float getWidth() {
//        return mRight - mLeft;
//    }
//
//    /**
//     * Gets the current height of the crop window.
//     */
//    public float getHeight() {
//        return mBottom - mTop;
//    }
//
//    /**
//     * Determines if this Edge is outside the inner margins of the given bounding rectangle. The
//     * margins come inside the actual frame by SNAPRADIUS amount; therefore, determines if the point
//     * is outside the inner "margin" frame.
//     */
//    public boolean isOutsideMargin(int who, @NonNull RectF rect, float margin) {
//
//        final boolean result;
//
//        switch (who) {
//            case LEFT:
//                result = mLeft - rect.left < margin;
//                break;
//            case TOP:
//                result = mTop - rect.top < margin;
//                break;
//            case RIGHT:
//                result = rect.right - mRight < margin;
//                break;
//            default: // BOTTOM
//                result = rect.bottom - mBottom < margin;
//                break;
//        }
//        return result;
//    }
//
//    // Private Methods /////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * Get the resulting x-position of the left edge of the crop window given the handle's position
//     * and the image's bounding box and snap radius.
//     *
//     * @param x               the x-position that the left edge is dragged to
//     * @param imageRect       the bounding box of the image that is being cropped
//     * @param imageSnapRadius the snap distance to the image edge (in pixels)
//     *
//     * @return the actual x-position of the left edge
//     */
//    private float adjustLeft(float x, @NonNull RectF imageRect, float imageSnapRadius, float aspectRatio) {
//
//        final float resultX;
//
//        if (x - imageRect.left < imageSnapRadius) {
//
//            resultX = imageRect.left;
//
//        } else {
//
//            // Select the minimum of the three possible values to use
//            float resultXHoriz = Float.POSITIVE_INFINITY;
//            float resultXVert = Float.POSITIVE_INFINITY;
//
//            // Checks if the window is too small horizontally
//            if (x >= mRight - MIN_CROP_LENGTH_PX) {
//                resultXHoriz = mRight - MIN_CROP_LENGTH_PX;
//            }
//            // Checks if the window is too small vertically
//            if (((mRight - x) / aspectRatio) <= MIN_CROP_LENGTH_PX) {
//                resultXVert = mRight - (MIN_CROP_LENGTH_PX * aspectRatio);
//            }
//            resultX = Math.min(x, Math.min(resultXHoriz, resultXVert));
//        }
//        return resultX;
//    }
//
//    /**
//     * Get the resulting x-position of the right edge of the crop window given the handle's position
//     * and the image's bounding box and snap radius.
//     *
//     * @param x               the x-position that the right edge is dragged to
//     * @param imageRect       the bounding box of the image that is being cropped
//     * @param imageSnapRadius the snap distance to the image edge (in pixels)
//     *
//     * @return the actual x-position of the right edge
//     */
//    private float adjustRight(float x, @NonNull RectF imageRect, float imageSnapRadius, float aspectRatio) {
//
//        final float resultX;
//
//        // If close to the edge...
//        if (imageRect.right - x < imageSnapRadius) {
//
//            resultX = imageRect.right;
//
//        } else {
//
//            // Select the maximum of the three possible values to use
//            float resultXHoriz = Float.NEGATIVE_INFINITY;
//            float resultXVert = Float.NEGATIVE_INFINITY;
//
//            // Checks if the window is too small horizontally
//            if (x <= mLeft + MIN_CROP_LENGTH_PX) {
//                resultXHoriz = mLeft + MIN_CROP_LENGTH_PX;
//            }
//            // Checks if the window is too small vertically
//            if (((x - mLeft) / aspectRatio) <= MIN_CROP_LENGTH_PX) {
//                resultXVert = mLeft + (MIN_CROP_LENGTH_PX * aspectRatio);
//            }
//            resultX = Math.max(x, Math.max(resultXHoriz, resultXVert));
//        }
//        return resultX;
//    }
//
//    /**
//     * Get the resulting y-position of the top edge of the crop window given the handle's position
//     * and the image's bounding box and snap radius.
//     *
//     * @param y               the x-position that the top edge is dragged to
//     * @param imageRect       the bounding box of the image that is being cropped
//     * @param imageSnapRadius the snap distance to the image edge (in pixels)
//     *
//     * @return the actual y-position of the top edge
//     */
//    private float adjustTop(float y, @NonNull RectF imageRect, float imageSnapRadius, float aspectRatio) {
//
//        final float resultY;
//
//        if (y - imageRect.top < imageSnapRadius) {
//
//            resultY = imageRect.top;
//
//        } else {
//
//            // Select the minimum of the three possible values to use
//            float resultYVert = Float.POSITIVE_INFINITY;
//            float resultYHoriz = Float.POSITIVE_INFINITY;
//
//            // Checks if the window is too small vertically
//            if (y >= mBottom - MIN_CROP_LENGTH_PX)
//                resultYHoriz = mBottom - MIN_CROP_LENGTH_PX;
//
//            // Checks if the window is too small horizontally
//            if (((mBottom - y) * aspectRatio) <= MIN_CROP_LENGTH_PX)
//                resultYVert = mBottom - (MIN_CROP_LENGTH_PX / aspectRatio);
//
//            resultY = Math.min(y, Math.min(resultYHoriz, resultYVert));
//        }
//        return resultY;
//    }
//
//    /**
//     * Get the resulting y-position of the bottom edge of the crop window given the handle's
//     * position and the image's bounding box and snap radius.
//     *
//     * @param y               the x-position that the bottom edge is dragged to
//     * @param imageRect       the bounding box of the image that is being cropped
//     * @param imageSnapRadius the snap distance to the image edge (in pixels)
//     *
//     * @return the actual y-position of the bottom edge
//     */
//    private float adjustBottom(float y, @NonNull RectF imageRect, float imageSnapRadius, float aspectRatio) {
//
//        final float resultY;
//
//        if (imageRect.bottom - y < imageSnapRadius) {
//
//            resultY = imageRect.bottom;
//
//        } else {
//
//            // Select the maximum of the three possible values to use
//            float resultYVert = Float.NEGATIVE_INFINITY;
//            float resultYHoriz = Float.NEGATIVE_INFINITY;
//
//            // Checks if the window is too small vertically
//            if (y <= mTop + MIN_CROP_LENGTH_PX) {
//                resultYVert = mTop + MIN_CROP_LENGTH_PX;
//            }
//            // Checks if the window is too small horizontally
//            if (((y - mTop) * aspectRatio) <= MIN_CROP_LENGTH_PX) {
//                resultYHoriz = mTop + (MIN_CROP_LENGTH_PX / aspectRatio);
//            }
//            resultY = Math.max(y, Math.max(resultYHoriz, resultYVert));
//        }
//        return resultY;
//    }
//
//}
