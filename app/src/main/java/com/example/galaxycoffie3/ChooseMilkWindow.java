package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ChooseMilkWindow extends AppCompatActivity {

    ViewPager viewPager;
    TextView textView1;
    ImageButton imageButton;
    Data data = Data.getSingleton();
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;

    Data.MilkTypes currMilk = Data.MilkTypes.RegularMilk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_milk_window);

        viewPager = findViewById(R.id.viewPager);

        sliderDotsPanel = findViewById(R.id.SliderDots1);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,
                new int[]{R.drawable.regular_milk, R.drawable.soy_milk,
                        R.drawable.almond_milk, R.drawable.rice_milk}, Data.STATE_MILK);
        ;

        viewPager.setAdapter(viewPagerAdapter);

        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotsPanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        imageButton = findViewById(R.id.proceed_coffee);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.shopCart.get(data.currentIndex).setMilkType(currMilk);
                Intent addIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(addIntent);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                textView1 = findViewById(R.id.coffee_type);
                if (position == 0) {
                    textView1.setText("Regular Milk");
                    currMilk = Data.MilkTypes.RegularMilk;
                } else if (position == 1) {
                    textView1.setText("Soy Milk");
                    currMilk = Data.MilkTypes.SoyMilk;
                } else if (position == 2) {
                    textView1.setText("Almond Milk");
                    currMilk = Data.MilkTypes.AlmondMik;

                } else if (position == 3) {
                    textView1.setText("Rice Milk");
                    currMilk = Data.MilkTypes.RiceMilk;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}