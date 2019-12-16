package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.io.Serializable;
import java.util.ArrayList;


public class ChooseCoffeeWindow extends AppCompatActivity {

    private static final int EMPTY = 0;
    ViewPager viewPager;
    private Context mContext;
    TextView textView1, textView2;
    LottieAnimationView  goToNextButton, cart;
    Data.CoffeeTypes coffeeType = Data.CoffeeTypes.MilkyWayCoffee;
    Data data = Data.getSingleton();
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;
    NotificationBadge badge;
    ConstraintLayout myLayout;
    LottieAnimationView addCoffeeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coffee_window);
        mContext = this;
        myLayout = findViewById(R.id.activity_window1);
        AnimationDrawable animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        badge = findViewById(R.id.badge);
        data.badge = badge;
        badge.setNumber(data.shopCart.size());

        viewPager = findViewById(R.id.viewPager);
        cart = findViewById(R.id.shop_cart_view);
        sliderDotsPanel = findViewById(R.id.SliderDots1);

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,
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

//        goToNextButton = findViewById(R.id.proceed_to_cart);
//        goToNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (data.shopCart.size() != EMPTY) {
//                    Intent addIntent = new Intent(getApplicationContext(), ShoppingCart.class);
//                    startActivity(addIntent);
//                } else {
//                    Toast.makeText(mContext, "Your Cart is Empty\n" +
//                            "Please Choose at least one item to continue", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.shopCart.size() != EMPTY) {
                    Intent addIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                    startActivity(addIntent);
                } else {
                    Toast.makeText(mContext, "Your Cart is Empty\n" +
                            "Please Choose at least one item to continue", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addCoffeeButton = findViewById(R.id.proceed_coffee);
        addCoffeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coffee coffee = new Coffee(coffeeType, Data.getPriceByType(coffeeType));
                if (data.addItem(coffee) == Data.ADD_SUCCESS) {
                    addCoffeeButton.setEnabled(false);
                    cart.setEnabled(false);
                    addCoffeeButton.setProgress(0);
                    addCoffeeButton.playAnimation();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            data.currentIndex = data.shopCart.size() - 1;
                            data.layout = myLayout;
                            myLayout.setAlpha((float) 0.35);
                            Intent addIntent = new Intent(getApplicationContext(), ChooseMilkWindow.class);
                            startActivityForResult(addIntent, 1);
                        }
                    }, 700);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast toast = Toast.makeText(mContext, Data.getCoffeeName(coffeeType) + " Added to Cart", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,55);
                toast.show();
                cart.setProgress(0);
                cart.playAnimation();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        badge.setNumber(data.shopCart.size());
                    }
                }, 300);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                data.shopCart.remove(data.shopCart.size() - 1);
            }
        }
        myLayout.setAlpha(1);
        addCoffeeButton.setEnabled(true);
        cart.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //your method call
        Intent addIntent = new Intent(getApplicationContext(), MainActivity.class);
        addIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(addIntent);
        finish();
    }

}