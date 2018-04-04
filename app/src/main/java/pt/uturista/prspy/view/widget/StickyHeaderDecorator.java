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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.uturista.prspy.BuildConfig;
import pt.uturista.prspy.R;


public class StickyHeaderDecorator extends RecyclerView.ItemDecoration {
    private static final String TAG = "StickyHeaderDecorator";
    private View mHeaderView;
    private final int mHeaderID;


    public StickyHeaderDecorator(@LayoutRes int headerId) {
        mHeaderID = headerId;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);

        final int childCount = parent.getChildCount();

        //checks if there's any childs, aka can we even have any header?
        if (childCount <= 0 || parent.getAdapter().getItemCount() <= 0) {
            return;
        }

        if (mHeaderView == null) {
            mHeaderView = parent.getChildAt(0);

            if(BuildConfig.DEBUG && parent.getChildViewHolder(mHeaderView).isRecyclable()){
                throw new AssertionError("StickyHeaderDecorator requires the header row to be non Recyclable!");
            }
        }

        // Always remember to save the canvas and then restore it later so that
        //  you do not cause weird bugs if multiple decorations are used
        canvas.save();
        canvas.translate(0, 0);
        mHeaderView.draw(canvas);
        canvas.restore();
    }
}
