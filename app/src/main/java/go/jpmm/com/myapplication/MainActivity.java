package go.jpmm.com.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;


public class MainActivity extends ActionBarActivity {

    private boolean pendingIntroAnimation;
    private ImageView loginLogo;
    private View loginBack;
    private View loginOverlay;
    private ImageView loginBars;
    private EditText userEditTxt;
    private EditText passEditTxt;
    private View mainLayout;
    private RelativeLayout loginContent;
    private View loginControls;
    private View helpControls;
    private Button loginBtn;
    private boolean keyboardUp;
    private InputMethodManager mInputMethodManager;
    private LinearLayout loginSetup;
    private TextView needHelp;
    private View userSeparator;
    private View passSeparator;
    private View controls;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1, 0.75f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0.5f);
//        final ScaleAnimation anim2 = new ScaleAnimation(1, 1, 0.75f, 1, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0.5f);

        mainLayout =  findViewById(R.id.main_content);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            private final int DefaultKeyboardDP = 100;

            // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
            private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);

            private final Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                // Convert the dp to pixels.
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, getResources().getDisplayMetrics());


                //r will be populated with the coordinates of your view that area still visible.
                mainLayout.getWindowVisibleDisplayFrame(r);

                // Conclude whether the keyboard is shown or not.
                final int rootHeight = mainLayout.getRootView().getHeight();
                final int heightDiff = rootHeight - (r.bottom - r.top);
                AnimatorSet set = new AnimatorSet();
                if (heightDiff >= estimatedKeyboardHeight) { // if more than 100 pixels, its probably a keyboard...

                    float userHeight = 0;
                    float passwordHeight = 0;
                    if (passEditTxt.isFocused()) {
                        LinearLayout.LayoutParams passSeparatorLP = (LinearLayout.LayoutParams) passSeparator.getLayoutParams();
                        passwordHeight = passEditTxt.getHeight() + passSeparator.getHeight() + passSeparatorLP.bottomMargin;
                        LinearLayout.LayoutParams userSeparatorLP = (LinearLayout.LayoutParams) passSeparator.getLayoutParams();
                        userHeight = userEditTxt.getHeight() + userSeparator.getHeight() + userSeparatorLP.bottomMargin + needHelp.getHeight() + helpControls.getHeight();
                    }

                    float loginButtonHeight = rootHeight - (loginBtn.getY() + helpControls.getPaddingTop() + passwordHeight);
                    float loginControlsHeight = rootHeight - (loginControls.getY() + loginControls.getPaddingBottom() + loginButtonHeight + userHeight);

//                    LinearLayout.LayoutParams loginLogoLP = (LinearLayout.LayoutParams) loginLogo.getLayoutParams();
                    float loginLogoHeight = -loginLogo.getY();
//                    float loginLogoHeight = loginLogoLP.topMargin;


                    loginBars.setPivotX(loginBars.getWidth()/2);
                    loginBars.setPivotY(loginBars.getHeight()/2);
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1, 0.75f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 0.75f);
                    PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", loginLogoHeight);


                    ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(loginBars, scaleX, scaleY, translateY);
                    ObjectAnimator anim1 = ObjectAnimator.ofFloat(loginControls, "translationY", -100);
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(loginBtn, "translationY", loginLogoHeight);
                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(loginLogo, "translationY", loginLogoHeight);

                    ObjectAnimator anim5 = ObjectAnimator.ofFloat(controls, "translationY", -250);

                    /*ObjectAnimator anim4 = ObjectAnimator.ofFloat(userEditTxt, "translationY", -100);
                    ObjectAnimator anim5 = ObjectAnimator.ofFloat(userSeparator, "translationY", -100);
                    ObjectAnimator anim6 = ObjectAnimator.ofFloat(passEditTxt, "translationY", -100);
                    ObjectAnimator anim7 = ObjectAnimator.ofFloat(passSeparator, "translationY", -100);*/

//                    set.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)controls.getLayoutParams();
//                            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)loginBtn.getLayoutParams();
////                            lp.height = 400;
//                            lp1.height = 4000;
//                            loginBtn.setLayoutParams(lp1);
//                        }
//                    });

                    set.playTogether(anim,anim3,anim5);
//                    set.playTogether(anim,anim1,anim3,anim2,anim4,anim5,anim6,anim7);
                    set.start();

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)controls.getLayoutParams();
//                            RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)loginBtn.getLayoutParams();
                            lp.height = 800;
//                            lp1.height = 4000;
//                            loginBtn.setLayoutParams(lp1);
                } else {
                    if (loginControls.getTranslationY()!=0) {
                        loginBars.setPivotX(loginBars.getWidth()/2);
                        loginBars.setPivotY(loginBars.getHeight()/2);
                        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f);
                        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f);
                        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat("translationY", 0f);

                        ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(loginBars, scaleX, scaleY, translateY);
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(loginControls, "translationY", 0f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(loginBtn, "translationY", 0f);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(loginLogo, "translationY", 0f);

                        set.playTogether(anim,anim1,anim2,anim3);
                        set.start();
                    }
                }
            }
        });

        mainLayout.requestFocus();

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        loginContent = (RelativeLayout) findViewById(R.id.login_content);

        controls = findViewById(R.id.controls);
        loginControls = findViewById(R.id.login_controls);
        helpControls = findViewById(R.id.login_help_controls);
//        loginSetup = (LinearLayout) findViewById(R.id.login_setup_task);

        loginBack = findViewById(R.id.login_back);

        loginOverlay = findViewById(R.id.login_overlay);

        loginLogo = (ImageView) findViewById(R.id.login_logo);
        loginBars = (ImageView) findViewById(R.id.login_bars);

        userEditTxt = (EditText) findViewById(R.id.login_user_name_editbox);
        passEditTxt = (EditText) findViewById(R.id.login_password_editbox);

        needHelp = (TextView) findViewById(R.id.login_help_text);
        userSeparator = findViewById(R.id.login_separator_line);
        passSeparator = findViewById(R.id.login_separator_line1);

        loginBtn = (Button) findViewById(R.id.login_button);
        /*loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet set = new AnimatorSet();
                *//*translate logo up*//*
                float loginLogoHeight = loginLogo.getHeight() + loginLogo.getPaddingBottom() + loginLogo.getPaddingTop();
                ObjectAnimator translationY = ObjectAnimator.ofFloat(loginLogo, "translationY", 0f, -loginLogoHeight);
                translationY.setDuration(500);
                *//*hide login controls*//*
                ObjectAnimator alpha = ObjectAnimator.ofFloat(loginControls, "alpha", 1f, 0f);
                alpha.setDuration(500);

                *//*hide help controls*//*
                ObjectAnimator alphaHelp = ObjectAnimator.ofFloat(helpControls, "alpha", 1f, 0f);
                alphaHelp.setDuration(500);

                *//*hide help controls*//*
                ObjectAnimator alphaLogin = ObjectAnimator.ofFloat(loginBtn, "alpha", 1f, 0f);
                alphaLogin.setDuration(500);

                *//*scale login bars*//*
                loginBars.setPivotX(100);
                loginBars.setPivotY(100);
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1, 1.25f);
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 1.25f);
                ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(loginBars, scaleX, scaleY).setDuration(300);
                set.playTogether(translationY, alpha, scale, alphaHelp, alphaLogin);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginControls.setVisibility(View.GONE);
                        loginSetup.setVisibility(View.VISIBLE);
                        ScaleAnimation anim1 = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                        anim1.setRepeatCount(Animation.INFINITE);
                        anim1.setRepeatMode(Animation.REVERSE);
                        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
                        anim1.setDuration(300);
                        loginBars.startAnimation(anim1);

                        TextView task = (TextView) getLayoutInflater().inflate(R.layout.login_setup_text, null);
                        task.setText("Authenticated");
                        task.setAlpha(0f);
                        loginSetup.addView(task);
                        ObjectAnimator alpha = ObjectAnimator.ofFloat(task, "alpha", 0f, 1f);
                        alpha.setDuration(500);
                        alpha.setStartDelay(1000);
                        alpha.start();

                        task = (TextView) getLayoutInflater().inflate(R.layout.login_setup_text, null);
                        task.setText("Setup feed configuration");
                        task.setAlpha(0f);
                        loginSetup.addView(task);
                        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(task, "alpha", 0f, 1f);
                        alpha1.setDuration(500);
                        alpha1.setStartDelay(2000);
                        alpha1.start();

                        task = (TextView) getLayoutInflater().inflate(R.layout.login_setup_text, null);
                        task.setText("Setup market monitors");
                        task.setAlpha(0f);
                        loginSetup.addView(task);
                        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(task, "alpha", 0f, 1f);
                        alpha2.setDuration(500);
                        alpha2.setStartDelay(3000);
                        alpha2.start();

                        task = (TextView) getLayoutInflater().inflate(R.layout.login_setup_text, null);
                        task.setText("Loaded alerts");
                        task.setAlpha(0f);
                        loginSetup.addView(task);
                        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(task, "alpha", 0f, 1f);
                        alpha3.setDuration(500);
                        alpha3.setStartDelay(4000);
                        alpha3.start();
                    }
                });
                set.start();


            }
        });*/

        if (savedInstanceState==null){
            pendingIntroAnimation = true;
        }
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if it's the first time, start the intro animation
        if (pendingIntroAnimation){
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        //init all views in their start position
//        float loginLogoHeight = loginLogo.getHeight() + loginLogo.getPaddingBottom() + loginLogo.getPaddingTop();
//        loginLogo.setTranslationY(-loginLogoHeight);
        int y = (mainLayout.getHeight() - loginControls.getHeight()) + loginControls.getHeight();
        loginLogo.setAlpha(0f);
        moveViewToScreenCenter(loginLogo);
        userEditTxt.setTranslationY(loginControls.getHeight());
        passEditTxt.setTranslationY(loginControls.getHeight());
        needHelp.setTranslationY(loginControls.getHeight());
        userSeparator.setTranslationY(loginControls.getHeight());
        passSeparator.setTranslationY(loginControls.getHeight());
        helpControls.setTranslationY(helpControls.getHeight());
        loginBtn.setTranslationY(loginBtn.getHeight()+helpControls.getHeight());
        loginBars.setScaleY(0);

        AnimatorSet mainSet = new AnimatorSet();
        AnimatorSet contentSet = new AnimatorSet();
        final AnimatorSet loginControlsSet = new AnimatorSet();

        ObjectAnimator alphaLogo = ObjectAnimator.ofFloat(loginLogo, "alpha", 0f, 1f);
        alphaLogo.setDuration(1000);
//        alphaLogo.start();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(loginOverlay, "alpha", 1f, 0f);

        loginBack.setPivotX(loginBack.getWidth()/2);
        loginBack.setPivotY(loginBack.getHeight()/2);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 2f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 2f, 1f);
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(loginBack, scaleX, scaleY);

        ObjectAnimator transLogoY = ObjectAnimator.ofFloat(loginLogo, "translationY", 0);

        ObjectAnimator transUserY = ObjectAnimator.ofFloat(userEditTxt, "translationY", 0);
        ObjectAnimator transPassY = ObjectAnimator.ofFloat(passEditTxt, "translationY", 0);
        ObjectAnimator transUserSeparatorY = ObjectAnimator.ofFloat(userSeparator, "translationY", 0);
        ObjectAnimator transHelpY = ObjectAnimator.ofFloat(needHelp, "translationY", 0);
        ObjectAnimator transPassSeparatorY = ObjectAnimator.ofFloat(passSeparator, "translationY", 0);
        ObjectAnimator transHelpControlsY = ObjectAnimator.ofFloat(helpControls, "translationY", 0);
        ObjectAnimator transLoginBtnY = ObjectAnimator.ofFloat(loginBtn, "translationY", 0);

        loginBars.setPivotY(loginBars.getHeight());
        final ObjectAnimator scaleBarsY = ObjectAnimator.ofFloat(loginBars, "scaleY", 0f, 1f);
        scaleBarsY.setInterpolator(new DecelerateInterpolator());
        scaleBarsY.setStartDelay(500);
//        scaleBarsY.start();

        scaleBarsY.setDuration(1000);

//        contentSet.playTogether(alpha, scale, transLogoY, transControlsY);
        contentSet.playTogether(alpha, scale, transLogoY, transUserY, transPassY, transUserSeparatorY, transHelpY, transPassSeparatorY,transHelpControlsY,transLoginBtnY,scaleBarsY);
//        contentSet.playSequentially(scaleBarsY);
        contentSet.setDuration(1000);
        mainSet.playSequentially(alphaLogo, contentSet);
//        loginControlsSet.setDuration(500);
//        loginControlsSet.playTogether(transUserY, transPassY, transUserSeparatorY, transHelpY, transPassSeparatorY);
       /* set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scaleBarsY.setDuration(500);
                scaleBarsY.setInterpolator(new AccelerateInterpolator());
                scaleBarsY.start();
            }
        });*/

        /*mainSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginControlsSet.start();
            }
        });*/
//        set1.playSequentially(scaleBarsY);
//        set.playSequentially(set1);
//        mainSet.setStartDelay(1500);
//        mainSet.setDuration(1000);
        mainSet.start();

        /*ObjectAnimator anim = ObjectAnimator.ofFloat(loginLogo, "translationY", -loginLogoHeight, 0f);
        anim.setDuration(500)
                .setStartDelay(1000);
        anim.start();

        loginBars.setPivotX(100);
        loginBars.setPivotY(100);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
        ObjectAnimator.ofPropertyValuesHolder(loginBars, scaleX, scaleY).setDuration(1000).start();

        anim = ObjectAnimator.ofFloat(loginControls, "alpha", 0f, 1f);
        anim.setDuration(1000)
                .setStartDelay(1000);
        anim.start();*/




        /*ScaleAnimation anim1 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        anim1.setDuration(1000);
        loginBars.startAnimation(anim1);*/


        /*anim = ObjectAnimator.ofFloat(userEditTxt, "translationX", -userEditTxt.getWidth(), 0f);
        anim.setDuration(300)
                .setStartDelay(500);
        anim.start();*/



        /*anim = ObjectAnimator.ofFloat(userEditTxt, "alpha", 0f, 1f);
        anim.setDuration(3000)
                .setStartDelay(500);
        anim.start();

        anim = ObjectAnimator.ofFloat(passEditTxt, "alpha", 0f, 1f);
        anim.setDuration(3000)
                .setStartDelay(500);
        anim.start();*/

        /*anim = ObjectAnimator.ofFloat(loginBars, "height", 0f, loginBars.getHeight());
        anim.setDuration(300)
                .setStartDelay(500);
        anim.start();*/

//        ObjectAnimator scaleXIn = ObjectAnimator.ofFloat(loginBars, "scaleX", 0f, 1f);
        /*ViewHelper.setPivotX(loginBars,0.00000001f);
        ViewHelper.setPivotY(loginBars,0.00000001f);
        ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(loginBars, "scaleY", 0f, 1f);
        scaleYIn.setRepeatCount(Animation.INFINITE);
        scaleYIn.setRepeatMode(Animation.REVERSE);
        scaleYIn.setInterpolator(new LinearInterpolator());
        scaleYIn.setDuration(300)
                .setStartDelay(500);
        scaleYIn.start();*/


        /*up & down*/
        /*ScaleAnimation anim1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        anim1.setRepeatCount(Animation.INFINITE);
        anim1.setRepeatMode(Animation.REVERSE);
        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        anim1.setDuration(300);
        loginBars.startAnimation(anim1);*/

        /*ViewHelper.setPivotX(loginBack,50);
        ViewHelper.setPivotY(loginBack,50);
        ViewHelper.setScaleY(loginBack,1.5f);
        ViewHelper.setScaleX(loginBack,1.5f);
        ViewPropertyAnimator.animate(loginBack).scaleX(1).scaleY(1).setDuration(1000).start();*/


//        ViewHelper.setScaleY(loginBars, 0f);
        /*Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.decelerate_interpolator);
        animation.setDuration(1000);
        loginBars.startAnimation(animation);*/

        /*ScaleAnimation anim2 = new ScaleAnimation(1.5f, 1, 1.5f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(700);
        anim2.setStartOffset(200);
        anim2.setInterpolator(new AccelerateInterpolator());
        anim2.setFillAfter(true);
        loginBack.startAnimation(anim2);*/
        /*ValueAnimator anim1 = ValueAnimator.ofFloat(0f, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = Math.round((float) valueAnimator.getAnimatedValue());
                ViewGroup.LayoutParams layoutParams = loginBars.getLayoutParams();
                layoutParams.height = val;
                loginBars.setLayoutParams(layoutParams);
            }
        });
        anim1.setDuration(300);
        anim1.setRepeatCount(-1);
        anim1.setRepeatMode(Animation.REVERSE);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.start();*/

        //prepare components before animation
        /*float loginBtnHeight = loginBtn.getY() + loginBtn.getHeight();
        float layoutHeight = mainLayout.getRootView().getHeight();
        float loginLogoHeight = loginLogo.getHeight() + loginLogo.getPaddingBottom() + loginLogo.getPaddingTop();
        float loginBarsHeight = loginBars.getHeight() + layoutHeight;
        ViewHelper.setTranslationY(loginBtn,loginBtnHeight);
        ViewHelper.setTranslationY(loginBars,-loginBarsHeight);
        ViewHelper.setTranslationY(loginLogo,-loginLogoHeight);


        loginLogo.animate()
                .translationY(0)
                .setDuration(300)
                .setStartDelay(300);

        loginBars.animate()
                .translationY(loginBarsHeight)
                .setDuration(300)
                .setStartDelay(500);

        loginBtn.animate()
                .translationY(-(loginBtnHeight+layoutHeight))
                .setDuration(300)
                .setStartDelay(700);*/

    }


    private void moveViewToScreenCenter( View view )
    {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics( dm );
        int statusBarOffset = dm.heightPixels - mainLayout.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen( originalPos );

        int xDest = dm.widthPixels/2;
        xDest -= (view.getMeasuredWidth()/2);
        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;

        view.setTranslationX(xDest - originalPos[0]);
        view.setTranslationY(yDest - originalPos[1]);

        /*TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        anim.setDuration(1000);
        anim.setFillAfter( true );
        view.startAnimation(anim);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
