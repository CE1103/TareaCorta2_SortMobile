package org.ce1103.sortmobile.sorter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;

class SortItem extends TextView implements Comparable<SortItem> {

    private int mValue;


    public SortItem(Context context) {
        super(context);
    }

    public SortItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SortItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setAlpha(0.4f);
        }
    }

    void setFixed(boolean isFixed) {
        if (isFixed) {
            post(new Runnable() {
                @Override
                public void run() {
                    setEnabled(false);
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height * mValue / SortContainer.MAX_VALUE);
    }

    void setValue(int value) {
        mValue = value;
        setText(String.valueOf(mValue));
    }

    void checkIn(final boolean block) {
        animY(block, 0);
    }

    private void animY(final boolean block, final int y) {
        final CountDownLatch latch = new CountDownLatch(1);
        post(new Runnable() {
            @Override
            public void run() {
                if (y > 0) {
                    setSelected(true);
                }
                animate().translationY(y)
                        .setDuration(90)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                latch.countDown();
                            }
                        }).start();
            }
        });
        if (block) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void checkOut(final boolean block) {
        animY(block, getPaddingBottom());
    }


    public void animX(final boolean block, final int x) {
        final CountDownLatch latch = new CountDownLatch(1);
        post(new Runnable() {
            @Override
            public void run() {
                animate().translationX(x)
                        .setDuration(90)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                latch.countDown();
                            }
                        }).start();
            }
        });
        if (block) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    boolean less(SortItem item) {
        return this.compareTo(item) < 0;
    }

    @Override
    public int compareTo(SortItem o) {
        return this.mValue - o.mValue;
    }
}
