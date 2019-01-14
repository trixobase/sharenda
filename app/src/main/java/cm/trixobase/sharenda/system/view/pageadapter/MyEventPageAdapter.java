package cm.trixobase.sharenda.system.view.pageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cm.trixobase.sharenda.domain.fragment.event.ForeignFragment;
import cm.trixobase.sharenda.domain.fragment.event.HistoricFragment;
import cm.trixobase.sharenda.domain.fragment.event.OwnerFragment;
import cm.trixobase.sharenda.domain.ui.UiActivity;

/**
 * Created by noumianguebissie on 5/15/18.
 */

public class MyEventPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private static String title1;
    private static String title2;
    private static String title3;

    public MyEventPageAdapter(FragmentManager fm,
                              String firstTitle,
                              String secondTitle,
                              String thirthTitle,
                              List<UiActivity> ownerActivityist,
                              UiActivity priorityActivity,
                              List<UiActivity> foreignActivityist,
                              List<UiActivity> historicActivityist) {

        super(fm);
        title1 = firstTitle;
        title2 = secondTitle;
        title3 = thirthTitle;

        fragments = new ArrayList();
        Fragment frag = new OwnerFragment(ownerActivityist, priorityActivity);
        fragments.add(frag);
        frag = new ForeignFragment(foreignActivityist);
        fragments.add(frag);
        frag = new HistoricFragment(historicActivityist);
        fragments.add(frag);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return title1;
            case 1:
                return title2;
            case 2:
                return title3;
            default:
                    return "MyEventPageAdapter Title Page";
        }
    }

}
