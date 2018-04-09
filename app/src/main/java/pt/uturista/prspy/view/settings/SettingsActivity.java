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

package pt.uturista.prspy.view.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import pt.uturista.prspy.R;
import pt.uturista.prspy.view.BaseNavigationActivity;
import pt.uturista.prspy.view.settings.fragment.SettingsFragment;

public class SettingsActivity extends BaseNavigationActivity {

    private static final String TAG = "SettingsActivity";

    public SettingsActivity() {
        super(false);
    }

    @Override
    protected int getDrawerId() {
        return R.id.nav_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getFragmentManager().findFragmentByTag(SettingsFragment.TAG);

        if (fragment == null) {
            fragment = new SettingsFragment();
        }

        getFragmentManager().beginTransaction().replace(R.id.main_content_frame, fragment, SettingsFragment.TAG).commit();
    }
}
