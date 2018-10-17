package org.ce1103.sortmobile.sorter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.ce1103.sortmobile.MainActivity;
import org.ce1103.sortmobile.R;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SortContainer extends LinearLayout implements Sorter {

    static final int MAX_VALUE = 100;
    public static SortItem[] mArr = new SortItem[10];

    public SortContainer(Context context) {
        super(context);
    }

    public SortContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SortContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void play() {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                sort();
            }
        });
        executor.shutdown();
    }


    private void refill() {
        final CountDownLatch latch = new CountDownLatch(1);
        post(new Runnable() {
            @Override
            public void run() {
                removeAllViews();
                for (int i = 0; i < mArr.length; i++) {
                    mArr[i].setTranslationX(0);
                    mArr[i].setTranslationY(0);
                    addView(mArr[i]);
                }
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sort() {
        switch (MainActivity.sType) {
            case INSERTION:
                insertionSort();
                break;
            case BUBBLE:
                bubbleSort();
                break;
        }
    }

    protected void exchange(int i, int j) {
        final SortItem a = mArr[i];
        final SortItem b = mArr[j];
        final int x = (int) (b.getX() - a.getX());
        a.animX(false, x);
        b.animX(true, -x);

        a.checkIn(false);
        b.checkIn(true);

        final SortItem tmp = mArr[i];
        mArr[i] = mArr[j];
        mArr[j] = tmp;


        refill();
    }

    private void insertionSort() {
        final int animY = getPaddingBottom();
        for (int i = 1; i < mArr.length; i++) {
            final SortItem current = mArr[i];
            current.checkOut(true);
            int j = i;
            while (j > 0) {
                mArr[j - 1].checkOut(false);
                mArr[j].checkOut(true);
                if (mArr[j].less(mArr[j - 1])) {
                    exchange(j - 1, j);
                } else {
                    mArr[j - 1].checkIn(false);
                    mArr[j].checkIn(true);
                    break;
                }
                j--;
            }
            current.checkIn(true);
            pause();
        }
        done();
    }

    public  void bubbleSort() {

        int n = mArr.length;
        final int animY = getPaddingBottom();
        while (true) {
            boolean exchanged = false;
            for (int i = 1; i < n; i++) {
                mArr[i - 1].checkOut(false);
                mArr[i].checkOut(true);

                if (mArr[i].less(mArr[i - 1])) {
                    exchange(i - 1, i);
                    exchanged = true;
                } else {
                    mArr[i - 1].checkIn(false);
                    mArr[i].checkIn(true);
                }
            }
            if (!exchanged) {
                break;
            }
            mArr[--n].setFixed(true);
        }
        for (int i = 0; i < n; i++) {
            mArr[i].setFixed(true);
        }
    }


    @Override
    public void done() {
        post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mArr.length; i++) {
                    final SortItem sortItem = mArr[i];
                    sortItem.setFixed(true);
                }
            }
        });
    }

    private void pause() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void renew() {
        final Random random = new Random();
        for (int i = 0; i < mArr.length; i++) {
            final SortItem item = (SortItem) LayoutInflater.from(getContext())
                    .inflate(R.layout.sort_item, SortContainer.this, false);
            mArr[i] = item;
            int value = random.nextInt(MAX_VALUE);
            if (value < 10) {
                value += 10;
            }
            item.setValue(value);
        }
        removeAllViews();
        for (int i = 0; i < mArr.length; i++) {
            addView(mArr[i]);
        }
        post(new Runnable() {
            @Override
            public void run() {
                final int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    final View view = getChildAt(i);
                    view.setScaleY(0);
                    view.setPivotY(view.getBottom());
                    view.animate()
                            .setDuration(i * 20).scaleY(1.0f).start();
                }
            }
        });
    }
}
