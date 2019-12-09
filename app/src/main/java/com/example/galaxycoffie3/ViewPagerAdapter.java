package com.example.galaxycoffie3;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private int[] images;
    private int state;

    public ViewPagerAdapter(Context context, int[] array, int state) {
        this.mContext = context;
        images = array;
        this.state = state;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        // not replaced
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* empty, here we can add click action to our slider*/
            }
        });


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}