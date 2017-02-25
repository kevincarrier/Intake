package org.intakers.intake;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ManualAddFragment extends Fragment {

    public static ManualAddFragment newInstance(){
        return new ManualAddFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual_add , container, false);
    }

}
