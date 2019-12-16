package com.example.galaxycoffie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;


public class ShoppingCart extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    LottieAnimationView addAnotherButton;
    Button goToPayment;
    TextView totalPrice;
    Data data = Data.getSingleton();
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        mContext = this;
        totalPrice = findViewById(R.id.total_price);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        goToPayment = findViewById(R.id.goToPayment);
        addAnotherButton = findViewById(R.id.returnToCoffee);
        //update user's order in cart screen
        for (int orderNum = 0; orderNum < data.shopCart.size(); orderNum++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field, null);
            // Add the new row before the add field button.
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            TextView order = rowView.findViewById(R.id.cur_order);
            TextView price = rowView.findViewById(R.id.price);
            Button milk = rowView.findViewById(R.id.choose_milk);
            AnimationDrawable animationDrawable = (AnimationDrawable) milk.getBackground();
            animationDrawable.setEnterFadeDuration(2000);
            animationDrawable.setExitFadeDuration(4000);
            animationDrawable.start();
            ImageView image = rowView.findViewById(R.id.imageView);
            Coffee coffee = data.shopCart.get(orderNum);
            order.setText(Data.getCoffeeName(coffee.getCoffeeType()));
            String priceStr = Data.getPriceByType(coffee.getCoffeeType()) + "$";
            price.setText(priceStr);
            milk.setText(Data.getMilkName(coffee.getMilkType()));
            image.setImageResource(data.returnImageId(coffee.getCoffeeType()));
        }
        String totalTextView = "Total: " + data.getTotalPrice() + "$";
        totalPrice.setText(totalTextView);
        //move to payment screen
        goToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edge cases
                if (checkIfMilkSet()) {
                    Intent addIntent = new Intent(getApplicationContext(), Payment.class);
                    startActivity(addIntent);

                } else if (data.shopCart.size() == 0) {
                    Toast.makeText(mContext, "Shopping Cart is Empty\n" +
                            " Add Items by pressing the '+' Button", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "You Forgot to set Milk to Some\n" +
                            "of your items.\n", Toast.LENGTH_SHORT).show();
                }

            }
        });

        addAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.shopCart.size() < Data.MAX_ITEM_AMOUNT) {
                    addAnotherButton.setProgress(0);
                    addAnotherButton.playAnimation();
                    addAnotherButton.setEnabled(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent addIntent = new Intent(getApplicationContext(), ChooseCoffeeWindow.class);
                            startActivity(addIntent);
                            finish();
                        }
                    }, 800);
                } else {
                    Toast.makeText(mContext, "Only 4 Items Allowed Per User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        finish();
    }

    /*
       This function gets the current index of the current line index in order
     */
    private int getLineIdx(View v) {
        ViewParent field = v.getParent();
        ViewParent linearLayout = field.getParent();
        return ((ViewGroup) linearLayout).indexOfChild((View) field);
    }

    /*
    controls what happens when the user presses the choose milk button
     */
    public void onChooseMilk(View v) {
        v.setEnabled(false);
        data.currentIndex = getLineIdx(v);
        data.inShopCart = true;
        ConstraintLayout myLayout = findViewById(R.id.activity_shopping_cart);
        myLayout.setAlpha((float) 0.35);
        Intent addIntent = new Intent(getApplicationContext(), ChooseMilkWindow.class);
        startActivityForResult(addIntent, 1);
    }

    /*
    controls what happens when the user presses the 'x' button
    */
    public void onDelete(View v) {
        data.shopCart.remove(getLineIdx(v));
        parentLinearLayout.removeView((View) v.getParent());
        String totalTextView = "Total: " + data.getTotalPrice() + "$";
        totalPrice.setText(totalTextView);
    }

    /*
   controls what happens when the user presses the go back(->) button
    */
    @Override
    public void onBackPressed() {
        //your method call
        data.badge.setNumber(data.shopCart.size());
        super.onBackPressed();
    }

    /*
    This function checks weather the user chose milk for each milk he ordered
     */
    boolean checkIfMilkSet() {
        if (data.shopCart.size() == 0) {
            return false;
        }
        for (Coffee coffee : data.shopCart) {
            if (coffee.getMilkType() == Data.MilkTypes.NoMilk) {
                return false;
            }
        }
        return true;
    }

}
