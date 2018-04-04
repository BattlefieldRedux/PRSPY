package pt.uturista.prspy.view.servers.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TeamPageAdapter<T extends TeamPageAdapter.TitledFragment> extends FragmentPagerAdapter {

    private static final String TAG = "TeamPageAdapter";

    private final T mOpforFragment;
    private final T mBluforFragment;


    public TeamPageAdapter(FragmentManager fm, int viewPagerID, @NonNull T defaultOpforFragment, @NonNull T defaultBlueforFragment) {
        super(fm);

        // FragmentPagerAdapter will check if these fragments were already created
        // and so are we
        T opforFragment = getFragment(fm, viewPagerID, 0);
        if (opforFragment == null)
            opforFragment = defaultOpforFragment;

        T bluforFragment = getFragment(fm, viewPagerID, 1);
        if (bluforFragment == null)
            bluforFragment = defaultBlueforFragment;

        mOpforFragment = opforFragment;
        mBluforFragment = bluforFragment;
    }

    public T getFragment(FragmentManager fm, int viewPagerID, int position) {
        String name = makeFragmentName(viewPagerID, getItemId(position));

        // Supposedly the tag is unique enough to make sure we can cast safely
        return (T) fm.findFragmentByTag(name);
    }


    @Override
    public Fragment getItem(int position) {
        // this is only called when FragmentPagerAdapter does not find a cached fragment
        if (position == 0)
            return mOpforFragment;
        else
            return mBluforFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
            return mOpforFragment.getTitle();
        else
            return mBluforFragment.getTitle();
    }

    /**
     * This is a copy of the method used in FragmentPagerAdapter to generate a tag for the committed
     * fragment so we can find it later.
     *
     * @see FragmentPagerAdapter#makeFragmentName
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    public static abstract class TitledFragment extends Fragment{
        public abstract String getTitle();
    }
}
