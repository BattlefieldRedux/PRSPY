/*
 * Copyright (c) 2018 uturista.pt
 *
 * Licensed under the Attribution-NonCommercial 4.0 International (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.uturista.prspy.view.widget;

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
