package go.jpmm.com.myapplication;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;


public class MainActivity extends ActionBarActivity {

    private boolean pendingIntroAnimation;
    private ImageView loginLogo;
    private ImageView loginBack;
    private ImageView loginBars;
    private EditText userEditTxt;
    private EditText passEditTxt;
    private LinearLayout mainLayout;
    private View loginControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainLayout = (LinearLayout) findViewById(R.id.login_content);

        loginControls = findViewById(R.id.login_controls);

        loginBack = (ImageView) findViewById(R.id.login_back);

        loginLogo = (ImageView) findViewById(R.id.login_logo);
        loginBars = (ImageView) findViewById(R.id.login_bars);

        userEditTxt = (EditText) findViewById(R.id.login_user_name_editbox);
        passEditTxt = (EditText) findViewById(R.id.login_password_editbox);

//        passEditTxt.setError("Error");

        if (savedInstanceState==null){
            pendingIntroAnimation = true;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (pendingIntroAnimation){
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {

        float loginLogoHeight = loginLogo.getHeight() + loginLogo.getPaddingBottom() + loginLogo.getPaddingTop();
        ViewHelper.setTranslationY(loginLogo,-loginLogoHeight);
        ViewHelper.setAlpha(loginControls, 0f);

//        ViewHelper.setTranslationX(userEditTxt, -userEditTxt.getWidth());

        ObjectAnimator anim = ObjectAnimator.ofFloat(loginLogo, "translationY", -loginLogoHeight, 0f);
        anim.setDuration(500)
                .setStartDelay(1000);
        anim.start();

        /*anim = ObjectAnimator.ofFloat(userEditTxt, "translationX", -userEditTxt.getWidth(), 0f);
        anim.setDuration(300)
                .setStartDelay(500);
        anim.start();*/

        anim = ObjectAnimator.ofFloat(loginControls, "alpha", 0f, 1f);
        anim.setDuration(1000)
                .setStartDelay(1000);
        anim.start();

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
//        ObjectAnimator scaleYIn = ObjectAnimator.ofFloat(loginBars, "scaleY", 0f, 1f);
//        scaleYIn.setRepeatCount(-1);
//        scaleYIn.setRepeatMode(Animation.REVERSE);
//        scaleYIn.setInterpolator(new LinearInterpolator());
//        scaleYIn.setDuration(300)
//                .setStartDelay(500);
//        scaleYIn.start();

       /* PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 0f, 50f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 0f, 50f);
        ObjectAnimator.ofPropertyValuesHolder(loginBars, pvhX, pvhY).start();*/

//        ViewHelper.setScaleY(loginBars, 0f);
        /*Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.decelerate_interpolator);
        animation.setDuration(1000);
        loginBars.startAnimation(animation);*/

        ScaleAnimation anim1 = new ScaleAnimation(1.5f, 1, 1.5f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(700);
//        anim1.setStartOffset(300);
//        anim1.setInterpolator(new DecelerateInterpolator());
        anim1.setFillAfter(true);
        loginBack.startAnimation(anim1);
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
