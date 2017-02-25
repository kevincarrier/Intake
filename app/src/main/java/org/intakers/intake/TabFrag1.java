package org.intakers.intake;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFrag1 extends Fragment {
    Logger log = Logger.getAnonymousLogger();

    public TabFrag1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log.info("onCreateView() of Frag1 is called");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

}
