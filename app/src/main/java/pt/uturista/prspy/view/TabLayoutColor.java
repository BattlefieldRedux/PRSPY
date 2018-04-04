package pt.uturista.prspy.view;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;


public class TabLayoutColor implements ViewPager.OnPageChangeListener {
    @ColorInt
    private final static int[] TAB_COLORS = {Color.parseColor("#3399CC"), Color.parseColor("#FF1A1F")};
    private final TabLayout mTabLayout;

    public TabLayoutColor(@NonNull TabLayout tabLayout) {
        mTabLayout = tabLayout;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position + 1 < TAB_COLORS.length) {
            int color = blendColors(TAB_COLORS[position], TAB_COLORS[position + 1], positionOffset);
            mTabLayout.setSelectedTabIndicatorColor(color);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @ColorInt
    public static int blendColors(@ColorInt int iColor, @ColorInt int fColor, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(fColor) * ratio) + (Color.red(iColor) * inverseRation);
        float g = (Color.green(fColor) * ratio) + (Color.green(iColor) * inverseRation);
        float b = (Color.blue(fColor) * ratio) + (Color.blue(iColor) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }
}
