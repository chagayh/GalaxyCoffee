package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;


public class ChooseCoffeeWindow extends AppCompatActivity {

    ViewPager viewPager;
    private Context mContext;
    TextView textView1, textView2;
    ImageButton addCoffeeButton, goToNextButton, cart;
    Data.CoffeeTypes coffeeType = Data.CoffeeTypes.MilkyWayCoffee;
    Data data = Data.getSingleton();
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;
    NotificationBadge badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coffee_window);
        mContext = this;
        badge = findViewById(R.id.badge);
        data.badge = badge;
        badge.setNumber(data.shopCart.size());

        viewPager = findViewById(R.id.viewPager);
        cart = findViewById(R.id.shop_cart_view);
        sliderDotsPanel = findViewById(R.id.SliderDots1);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,
                new int[]{R.drawable.coffee1, R.drawable.coffee2,
                        R.drawable.coffee3, R.drawable.coffee4}, Data.STATE_COFFEE);
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

        goToNextButton = findViewById(R.id.proceed_to_cart);
        goToNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(addIntent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(addIntent);
            }
        });


        addCoffeeButton = findViewById(R.id.proceed_coffee);
        addCoffeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coffee coffee = new Coffee(coffeeType, Data.getPriceByType(coffeeType));
                if (data.addItem(coffee) == Data.ADD_SUCCESS) {
                    badge.setNumber(data.shopCart.size());

                    Toast.makeText(mContext, Data.getCoffeeName(coffeeType) + " Added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Only 4 Items Allowed Per User", Toast.LENGTH_SHORT).show();
                }
//                Intent addIntent = new Intent(getApplicationContext(), ChooseMilkWindow.class);
//                startActivity(addIntent);
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
                String setToText;
                if (position == 0) {
                    coffeeType = Data.CoffeeTypes.MilkyWayCoffee;
                    setToText = Data.getCoffeeName(coffeeType) + " " + String.valueOf(Data.getPriceByType(coffeeType)) + " $";
                    textView1.setText(setToText);
                } else if (position == 1) {
                    coffeeType = Data.CoffeeTypes.SpaceUnicornCoffee;
                    setToText = Data.getCoffeeName(coffeeType) + " " + String.valueOf(Data.getPriceByType(coffeeType)) + " $";
                    textView1.setText(setToText);
                } else if (position == 2) {
                    coffeeType = Data.CoffeeTypes.NebulaCoffee;
                    setToText = Data.getCoffeeName(coffeeType) + " " + String.valueOf(Data.getPriceByType(coffeeType)) + " $";
                    textView1.setText(setToText);

                } else if (position == 3) {
                    coffeeType = Data.CoffeeTypes.CelestialCoffee;
                    setToText = Data.getCoffeeName(coffeeType) + " " + String.valueOf(Data.getPriceByType(coffeeType)) + " $";
                    textView1.setText(setToText);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

//    @Override
//    public void onBackPressed() {
//        //your method call
//        Intent addIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(addIntent);
//    }

}