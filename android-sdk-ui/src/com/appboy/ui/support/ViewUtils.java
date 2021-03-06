package com.appboy.ui.support;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.appboy.support.AppboyLogger;

public class ViewUtils {
  private static final String TAG = AppboyLogger.getAppboyLogTag(ViewUtils.class);
  private static final int TABLET_SMALLEST_WIDTH_DP = 600;
  private static int sDisplayHeight;

  public static void removeViewFromParent(View view) {
    if (view != null) {
      if (view.getParent() instanceof ViewGroup) {
        final ViewGroup parent = (ViewGroup) view.getParent();
        setFocusableInTouchModeAndRequestFocus(parent);
        parent.removeView(view);
      }
    }
  }

  public static void setFocusableInTouchModeAndRequestFocus(View view) {
    try {
      view.setFocusableInTouchMode(true);
      view.requestFocus();
    } catch (Exception e) {
      AppboyLogger.e(TAG, "Caught exception while setting view to focusable in touch mode and requesting focus.", e);
    }
  }

  /**
   * Retrieve the coordinate of the top of the "available area where content can be placed and
   * remain visible to users" within the FrameLayout view. In most cases, this will be equivalent
   * to the height of the status bar.
   *
   * @param view
   * @return
   */
  public static int getTopVisibleCoordinate(View view) {
    Rect rectangle = new Rect();
    view.getWindowVisibleDisplayFrame(rectangle);
    return rectangle.top;
  }

  /**
   * Retrieve the size of the display in dp. The display height includes all screen decorations,
   * such as the status bar.
   *
   * @param activity
   * @return
   */
  public static int getDisplayHeight(Activity activity) {
    if (sDisplayHeight > 0) {
      return sDisplayHeight;
    }
    Display display = activity.getWindowManager().getDefaultDisplay();
    Point point = new Point();
    display.getSize(point);
    sDisplayHeight = point.y;
    return sDisplayHeight;
  }

  public static double convertDpToPixels(Activity activity, double valueInDp) {
    double density = activity.getResources().getDisplayMetrics().density;
    return valueInDp * density;
  }

  public static boolean isRunningOnTablet(Activity activity) {
    return activity.getResources().getConfiguration().smallestScreenWidthDp
        >= TABLET_SMALLEST_WIDTH_DP;
  }

  /**
   * Remove a previously installed global layout callback in an API gated fashion.
   *
   * @param viewTreeObserver
   * @param onGlobalLayoutListener
   */
  @TargetApi(16)
  public static void removeOnGlobalLayoutListenerSafe(ViewTreeObserver viewTreeObserver,
                                                      ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
      viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
    } else {
      viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }
  }

  /**
   * Safely calls {@link Activity#setRequestedOrientation(int)}
   */
  public static void setActivityRequestedOrientation(@NonNull Activity activity, int requestedOrientation) {
    try {
      activity.setRequestedOrientation(requestedOrientation);
    } catch (Exception e) {
      AppboyLogger.e(TAG, "Failed to set requested orientation " + requestedOrientation + " for activity class: " + activity.getLocalClassName(), e);
    }
  }
}
