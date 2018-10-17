package org.ce1103.sortmobile.sorter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ce1103.sortmobile.R;

public class SortFragment extends Fragment {

    public static Sorter mSorter;

    public static SortFragment newInstance() {
        final SortFragment fragment = new SortFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sort, container, false);
        final SortContainer sortView = (SortContainer) view.findViewById(R.id.sortView);

        mSorter = sortView;
        mSorter.renew();
        return view;
    }

}
