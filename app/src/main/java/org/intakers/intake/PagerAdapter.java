package org.intakers.intake;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Hai Dang on 2/25/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numTabs;

    private GraphFragment tab1;
    private ManualAddFragment tab3;
    private TabFrag2 tab2;

    public PagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){

            case 0:
                tab2 = new TabFrag2();
                return tab2;
            case 1:
                tab1 = new GraphFragment();
                return tab1;

            case 2:
                tab3 = new ManualAddFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }


}
