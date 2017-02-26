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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ManualAddFragment extends Fragment {

    public static ManualAddFragment newInstance(){
        return new ManualAddFragment();
    }

    Button submit;
    EditText foodInput;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manual_add , container, false);


        submit = (Button) v.findViewById(R.id.submitButton);

        foodInput = (EditText) v.findViewById(R.id.inputFood);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Hi" , "Hi");
                Toast.makeText(getActivity().getApplicationContext() , foodInput.getText().toString() , Toast.LENGTH_SHORT ).show();
            }
        });


        return v;
    }

}
