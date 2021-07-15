package com.example.advancedmobile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {
    private MapFragmentListener listener;
    private EditText editText;
    private Button buttonOk;
    public  interface MapFragmentListener{
        void onInputASent(CharSequence input);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.map_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
    public void updateEdiText(CharSequence newText)
    {
        editText.setText(newText);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  MapFragmentListener){
            listener = (MapFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString() + "must implement Map Fragment listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}