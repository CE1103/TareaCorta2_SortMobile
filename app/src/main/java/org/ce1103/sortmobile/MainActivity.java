package org.ce1103.sortmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.ce1103.sortmobile.sorter.SortFragment;

public class MainActivity extends Activity {
    public enum SortType {
        INSERTION,
        BUBBLE,
    }

    private static final String TAG = "MainActivity";
    public static SortType sType = SortType.INSERTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final SortFragment fragment = SortFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        }


    }

    public void bubbleOnClick(View v) {
        sType = SortType.BUBBLE;
        Toast.makeText(this, "Bubble Sort Selected", Toast.LENGTH_LONG).show();
    }

    public void insertionOnClick(View v) {
        sType = SortType.INSERTION;
        Toast.makeText(this, "Insertion Sort Selected", Toast.LENGTH_LONG).show();
    }

    public void sortAlgorithm(View v) {
        SortFragment.mSorter.play();
    }


}