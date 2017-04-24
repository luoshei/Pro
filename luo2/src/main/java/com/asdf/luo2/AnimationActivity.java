package com.asdf.luo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asdf.luo2.adapter.MyAdapter;
import com.asdf.luo2.util.BindIdUtil;
import com.asdf.luo2.util.BindView;

import java.util.ArrayList;

public class AnimationActivity extends Activity {
    private static final String TAG = "AnimationActivity";
    @BindView(R.id.product)
    private ImageView product;
    @BindView(R.id.cart)
    private TextView cart;
    @BindView(R.id.lv)
    private ListView lv;


    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        BindIdUtil.register(this);

        TranslateAnimation ta = new TranslateAnimation(this,null);

        MyAdapter ma = new MyAdapter(this);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //创建与Item的数量一致的ArrayList<View>
            ArrayList<ArrayList<View>> cache = new ArrayList<ArrayList<View>>();
            Bitmap[] bitmaps;
            @Override
            public void onItemClick(AdapterView<?> parent, View views, int position, long id) {
                /**第一次初始化数据**/
                if(bitmaps == null) bitmaps = new Bitmap[lv.getChildCount()];
                while(cache.size() < lv.getChildCount()){
                    cache.add(new ArrayList<View>());
                }
                final View viewSrc = views.findViewById(R.id.src);
                if(bitmaps[position]==null) {

                    Bitmap bitmap = convertViewToBitmap(viewSrc);
                    bitmaps[position] = bitmap;
                }
                ViewGroup vg = (ViewGroup)views.findViewById(R.id.fl_container);
                Log.e(TAG, "onItemClick: " + "geshu:---" + ((ViewGroup)vg).getChildCount());
                ArrayList<View> cur = cache.get(position);
                for (View v:cur){
                    if(v.getTag(R.string.isOk).equals(true)){
                        startAnimation(v);
                        return;
                    }
                }

                ImageView iv = new ImageView(AnimationActivity.this);
                iv.setImageBitmap(bitmaps[position]);
                iv.setScaleX(0.7f);
                iv.setScaleY(0.7f);
                iv.setLayoutParams(viewSrc.getLayoutParams());//让克隆的View的布局参数和原始的View一致
                cur.add(iv);
                vg.addView(iv);
                startAnimation(iv);
            }
            public void startAnimation(final View view){

                int[] cartXY = new int[2];
                cart.getLocationOnScreen(cartXY);
                TranslateAnimation taX = new TranslateAnimation(0,cartXY[0],0,0);
                TranslateAnimation taY = new TranslateAnimation(0,0,0,cartXY[1]);
                AlphaAnimation aa = new AlphaAnimation(1,0.4f);
                taY.setInterpolator(new AnticipateInterpolator());
                AnimationSet as = new AnimationSet(false);
                as.addAnimation(taX);
                as.addAnimation(taY);
                as.addAnimation(aa);
                as.setDuration(1000);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(as);
                view.setTag(R.string.isOk,false);
                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        cart.startAnimation(new ScaleAnimation(1,1.2f,1,1.2f,0.5f,0.5f));
                        view.setVisibility(View.GONE);
                        view.setTag(R.string.isOk,true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
    }
}
