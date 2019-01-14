package cm.trixobase.sharenda.system.view.pageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.domain.fragment.phone.ContactFragment;
import cm.trixobase.sharenda.domain.fragment.phone.GroupFragment;
import cm.trixobase.sharenda.domain.ui.UiContact;
import cm.trixobase.sharenda.domain.ui.UiGroup;

/**
 * Created by noumianguebissie on 6/8/18.
 */

public class MyPhonePageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private static String title1;
    private static String title2;

    public MyPhonePageAdapter(FragmentManager fm,
                              String firstTitle,
                              String secondTitle,
                              List<UiContact> contacts,
                              List<UiGroup> groups,
                              int[] numbers) {

        super(fm);
        title1 = firstTitle;
        title2 = secondTitle;

        fragments = new ArrayList();
        Fragment frag = new ContactFragment(contacts);
        fragments.add(frag);
        frag = new GroupFragment(groups, numbers);
        fragments.add(frag);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return title1;
            case 1:
                return title2;
            default:
                return "MyPhonePageAdapter Title Page";
        }
    }

}
