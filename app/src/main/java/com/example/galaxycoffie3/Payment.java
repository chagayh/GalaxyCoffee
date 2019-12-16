package com.example.galaxycoffie3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
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

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Calendar;

public class Payment extends Activity implements View.OnTouchListener {
    /**
     * This activity represent the payment activity of the app.
     * This is the activity where the user pay for the coffee he ordered.
     */

    Data data = Data.getSingleton();
    private ImageView check_button;
    private ImageView question_mark_button;
    private EditText mDateEntryField;
    private EditText creditCardField;
    private EditText cvvField;
    private EditText nameField;
    private ProgressBar prg;
    private Handler mHandler;
    private boolean isValidDate = false;
    private boolean isValidCard = false;
    private boolean isValidCvv = false;
    private boolean paymentDone = false;
    private boolean isValidName = false;
    private ObjectAnimator progressAnimator;
    LottieAnimationView toggle;
    int flag = 0;
    LottieAnimationView upload;


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

        nameField = findViewById(R.id.ed_Name);
        nameField.addTextChangedListener(nameWatcher);

        prg = findViewById(R.id.progressBar);
        progressAnimator = ObjectAnimator.ofInt(prg, "progress", 0, 100);
        progressAnimator.setDuration(2000);
        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationCancel(animation);
                Intent addIntent = new Intent(getApplicationContext(), TransitionScreen.class);
                addIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addIntent);
                finish();
            }
        });
        question_mark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Payment.this).create();
                alertDialog.setTitle("What do you need my name for?");
                alertDialog.setMessage("With this name we will call you when Your order is ready");
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
                Toast.makeText(Payment.this, "long click", Toast.LENGTH_LONG);
                return false;
            }
        });

        creditCardField.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
        check_button.setOnTouchListener(this);
        cartDisplay();


//        toggle = findViewById(R.id.lav_toggle);
//        toggle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeState();
//            }
//        });
//
//        upload = findViewById(R.id.upload);
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                upload.setProgress(0);
//                upload.pauseAnimation();
//                upload.playAnimation();
//                Toast.makeText(Payment.this, "Cheers!!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private void changeState() {
        if (flag == 0) {
            toggle.setMinAndMaxProgress(0f, 0.43f); //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
            toggle.playAnimation();
            flag = 1;
            //---- Your code here------
        } else {
            toggle.setMinAndMaxProgress(0.5f, 1f);
            toggle.playAnimation();
            flag = 0;
            //---- Your code here------
        }
    }

    /**
     * a function to display all the cart elements the user ordered
     */
    private void cartDisplay() {
        TextView orderDetails = findViewById(R.id.orderDetails);
        TextView totalOrder = findViewById(R.id.totalOrder);
        float total = 0;
        ArrayList<Coffee> cart = data.shopCart;
        for (int i = 0; i < cart.size(); i++) {
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
            } else {
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
    private TextWatcher creditCardWatcher = new TextWatcher() {
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
            } else {
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
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                } else {
                    working += "/";
                    mDateEntryField.setText(working);
                    mDateEntryField.setSelection(working.length());
                }
            } else if (working.length() == 5 && before == 0) {
                String enteredYear = working.substring(3);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (Integer.parseInt("20" + enteredYear) < currentYear) {
                    isValid = false;
                }
            } else if (working.length() != 5) {
                isValid = false;
            }

            if (!isValid) {
                mDateEntryField.setError("Enter a valid date: MM/YY");
            } else {
                mDateEntryField.setError(null);
            }
            isValidDate = isValid;

        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

    };

    /**
     * this object checks the text inserted to the Name box
     */
    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            //TODO
            boolean isValid = true;

            if (working.length() < 1) {
                nameField.setError("Enter a Name");
            } else {
                nameField.setError(null);
                data.costumerName = working;
            }
            isValidName = isValid;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * this object responsible to execute the progress bar around the V key
     */
    Runnable mAction = new Runnable() {
        @Override
        public void run() {
            if (!paymentDone && isValidDate && isValidCard && isValidCvv && isValidName) {
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
            } else {
                if (isValidDate) {
                    mDateEntryField.setError(null);
                } else {
                    mDateEntryField.setError("Enter a valid date: MM/YYYY");
                }
                if (isValidCard) {
                    creditCardField.setError(null);
                } else {
                    creditCardField.setError("Enter a valid credit card number (16 digits)");
                }
                if (isValidCvv) {
                    cvvField.setError(null);
                } else {
                    cvvField.setError("Enter a valid cvv number (3 digits)");
                }
                if (isValidName) {
                    nameField.setError(null);
                } else {
                    nameField.setError("Enter a Name");
                }
            }
        }
    };

    /**
     * this function execute when the user tap on the screen. This functions checks on which element
     * the user pressed and continue accordingly
     *
     * @param v
     * @param event
     * @return
     */
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println(event.getAction());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mHandler = new Handler();
            mHandler.postDelayed(mAction, 10);
        } else {
            if (mHandler == null) {
                return true;
            }
            mHandler = null;
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