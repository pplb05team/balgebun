package pplb05.balgebun.costumer.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wahid Nur Rohman on 3/23/2016.
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment>fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags;

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
    }

    public void addFragments(Fragment fr, String title){
        fragments.add(fr);
        tabTitles.add(title);

    }

    public void addFragment(ArrayList<Fragment> fragments, ArrayList<String> tabTitles ){
        this.fragments = fragments;
        this.tabTitles = tabTitles;

    }

    @Override
    public Fragment getItem(int position) {
        Log.d("MY_FRAGMENT", ""+position + " : " + tabTitles.get(position));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }
}
