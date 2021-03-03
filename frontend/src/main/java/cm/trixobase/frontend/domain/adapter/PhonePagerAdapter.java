package cm.trixobase.frontend.domain.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import cm.trixobase.frontend.PhoneActivity;
import cm.trixobase.frontend.R;
import cm.trixobase.frontend.domain.fragment.ContactFragment;
import cm.trixobase.frontend.domain.fragment.GroupFragment;

/*
 * Powered by Trixobase Enterprise on 08/06/18.
 * updated on 01/03/21.
 */

public class PhonePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private String[] titles;

    public PhonePagerAdapter(FragmentManager fm, PhoneActivity activity) {
        super(fm);
        titles = new String[]{activity.getService().getContext().getString(R.string.contacts), activity.getService().getContext().getString(R.string.groups)};
        this.fragments = new ArrayList<>();
        this.fragments.add(new ContactFragment(activity));
        this.fragments.add(new GroupFragment(activity.getService()));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}