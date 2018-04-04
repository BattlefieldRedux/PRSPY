package pt.uturista.prspy.view.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import pt.uturista.prspy.R;
import pt.uturista.prspy.view.BaseNavigationActivity;
import pt.uturista.prspy.view.settings.fragment.SettingsFragment;

public class SettingsActivity extends BaseNavigationActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected boolean requiresInternet() {
        return false;
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
