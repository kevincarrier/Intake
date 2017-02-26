package org.intakers.intake;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Hai Dang on 2/25/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numTabs;

    private TabFrag1 tab1;
    private ManualAddFragment tab3;
    private TabFrag1 tab2;

    public PagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                tab1 = new TabFrag1();
                return tab1;
            case 1:
                tab2 = new TabFrag1();
                return tab2;
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
