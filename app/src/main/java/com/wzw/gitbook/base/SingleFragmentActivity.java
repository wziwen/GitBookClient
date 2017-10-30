package com.wzw.gitbook.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * Created by ziwen.wen on 2017/10/30.
 */
public class SingleFragmentActivity extends AppCompatActivity {

    public static void show(Context context, Class<? extends Fragment> target, Bundle bundle) {
        Intent intent = new Intent(context, SingleFragmentActivity.class);
        intent.putExtra("target", target.getName());
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String target = bundle.getString("target");
        try {
            Class clz = Class.forName(target);
            Fragment fragment = (Fragment) clz.newInstance();
            fragment.setArguments(getIntent().getBundleExtra("bundle"));

            FrameLayout layout = new FrameLayout(this);
            // TODO: 2017/10/30 使用真正的id
            int id = android.support.v7.appcompat.R.id.action_container;
            layout.setId(id);
            setContentView(layout);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(id, fragment)
                    .commit();

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
