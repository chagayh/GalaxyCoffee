package com.example.galaxycoffie3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Calendar;

public class Payment extends AppCompatActivity implements View.OnTouchListener {
    /**
     * This activity represent the payment activity of the app.
     * This is the activity where the user pay for the coffee he ordered.
     */

    Data data = Data.getSingleton();
    private ImageView  check_button;
    private ImageView question_mark_button;
    private EditText mDateEntryField;
    private EditText creditCardField;
    private EditText cvvField;
    private ProgressBar prg;
    private Handler mHandler;
    private boolean isValidDate = false;
    private boolean isValidCard = false;
    private boolean isValidCvv = false;
    private boolean paymentDone = false;
    private ObjectAnimator progressAnimator;


    /**
     * this function responsible to initialize all the elements in this activity, get all the
     * relevant user data from the Data object and display the order details.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        check_button = findViewById(R.id.buttonImage);
        question_mark_button = findViewById(R.id.buttonImage1);
        mDateEntryField = findViewById(R.id.date_entry_field);
        mDateEntryField.addTextChangedListener(mDateEntryWatcher);

        creditCardField = findViewById(R.id.creditCard);
        creditCardField.addTextChangedListener(creditCardWatcher);

        cvvField = findViewById(R.id.md_pass);
        cvvField.addTextChangedListener(cvvWatcher);

        prg = findViewById(R.id.progressBar);
        progressAnimator = ObjectAnimator.ofInt(prg, "progress", 0, 100);
        progressAnimator.setDuration(2000);
        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationCancel(animation);
                Intent addIntent = new Intent(getApplicationContext(), End.class);
                startActivity(addIntent);
            }
        });
        question_mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Payment.this).create();
                alertDialog.setTitle("How can i get a coupon?");
                alertDialog.setMessage("Upload your order to instegram and tag us: #Galaxycoffee in" +
                        " order to get a coupon to your inbox for your next visit");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        question_mark_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(Payment.this,"long click",Toast.LENGTH_LONG);
                return false;
            }
        });

        creditCardField.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
        check_button.setOnTouchListener(this);
        cartDisplay();
    }

    /**
     * a function to display all the cart elements the user ordered
     */
    private void cartDisplay()
    {
        TextView orderDetails = findViewById(R.id.orderDetails);
        TextView totalOrder = findViewById(R.id.totalOrder);
        float total = 0;
        ArrayList<Coffee> cart = data.shopCart;
        for (int i = 0; i < cart.size(); i++)
        {
            Coffee currCoffee = cart.get(i);
            total += data.getPriceByType(currCoffee.getCoffeeType());
            orderDetails.append(data.getCoffeeName(currCoffee.getCoffeeType()));
            orderDetails.append(",\t" + data.getMilkName(currCoffee.getMilkType()));
            orderDetails.append(",\t" + data.getPriceByType(currCoffee.getCoffeeType()));
            orderDetails.append("$\n");
        }
        totalOrder.setText("\nTOTAL - " + total + "$");
    }

    /**
     * this object checks the text inserted to the CVV box
     */
    private TextWatcher cvvWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length() < 3) {
                isValid = false;
            }

            if (!isValid) {
                cvvField.setError("Enter a valid cvv number (3 digits)");
            }
            else {
                cvvField.setError(null);
            }
            isValidCvv = isValid;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * this object checks the text inserted to the credit card number box
     */
    private  TextWatcher creditCardWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length() < 19) {
                isValid = false;
            }

            if (!isValid) {
                creditCardField.setError("Enter a valid credit card number (16 digits)");
            }
            else {
                creditCardField.setError(null);
            }
            isValidCard = isValid;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * this object checks the text inserted to the date box
     */
    private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length()==2 && before ==0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working)>12) {
                    isValid = false;
                } else {
                    working+="/";
                    mDateEntryField.setText(working);
                    mDateEntryField.setSelection(working.length());
                }
            }
            else if (working.length()==7 && before ==0) {
                String enteredYear = working.substring(3);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (Integer.parseInt(enteredYear) < currentYear) {
                    isValid = false;
                }
            } else if (working.length()!=7) {
                isValid = false;
            }

            if (!isValid) {
                mDateEntryField.setError("Enter a valid date: MM/YYYY");
            } else {
                mDateEntryField.setError(null);
            }
            isValidDate = isValid;

        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };

    /**
     * this object responsible to execute the progress bar around the V key
     */
    Runnable mAction = new Runnable() {
        @Override public void run() {
            if(!paymentDone && isValidDate && isValidCard && isValidCvv){
                mDateEntryField.setError(null);
                creditCardField.setError(null);
                cvvField.setError(null);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressAnimator.start();
                    }
                }, 1);
                paymentDone = true;
            }
            else{
                if(isValidDate){
                    mDateEntryField.setError(null);
                }
                else{
                    mDateEntryField.setError("Enter a valid date: MM/YYYY");
                }
                if(isValidCard){
                    creditCardField.setError(null);
                }
                else{
                    creditCardField.setError("Enter a valid credit card number (16 digits)");
                }
                if(isValidCvv){
                    cvvField.setError(null);
                }
                else{
                    cvvField.setError("Enter a valid cvv number (3 digits)");
                }
            }
        }
    };

    /**
     * this function execute when the user tap on the screen. This functions checks on which element
     * the user pressed and continue accordingly
     * @param v
     * @param event
     * @return
     */
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println(event.getAction());

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler = new Handler();
                mHandler.postDelayed(mAction, 10);
                break;
            default:
                if (mHandler == null) {
                    return true;
                }
                mHandler = null;
                break;
        }
        return false;
    }


    /**
     * Formatting a credit card number: #### #### #### #####
     */
    public static class CreditCardNumberFormattingTextWatcher implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (lock || s.length() > 16) {
                return;
            }
            lock = true;
            for (int i = 4; i < s.length(); i += 5) {
                if (s.toString().charAt(i) != ' ') {
                    s.insert(i, " ");
                }
            }
            lock = false;
        }
    }

    /**
     * Formatting an expression date for credit card
     */
    public static class CreditCardExpFormattingTextWatcher implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (lock || s.length() > 5) {
                return;
            }
            lock = true;
            for (int i = 2; i < s.length(); i += 2) {
                if (s.toString().charAt(i) != ' ') {
                    s.insert(i, " ");
                }
            }
            lock = false;
        }
    }
}