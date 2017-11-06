package com.wzw.epub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.wzw.gitbook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubReaderActivity extends AppCompatActivity {

    public static void start(Context context, String filePath, String fileName) {
        Intent intent = new Intent(context, EpubReaderActivity.class);
        intent.putExtra("file_path", filePath);
        intent.putExtra("file_name", fileName);
        context.startActivity(intent);
    }

    private Book mBook;
    private ArrayList<TOCReference> mTocReferences;
    private List<SpineReference> mSpineReferences;
    private String mFilePath;
    private String mFileName;

    private RecyclerView rvChapter;
    private MultiTypeAdapter chapterAdapter;
    private MotionRecyclerView rvContent;
    private MultiTypeAdapter contentAdapter;

    private ProgressBar progressBar;
    private TocRefViewBinder tocRefViewBinder;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        setContentView(R.layout.activity_epub_reader);
        appBarLayout = (AppBarLayout) findViewById(R.id.abl_epub);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Bundle bundle = getIntent().getExtras();
        mFilePath = bundle.getString("file_path");
        mFileName = bundle.getString("file_name");
        setTitle(mFileName);

        rvChapter = (RecyclerView) findViewById(R.id.rv_chapter);
        rvChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new MultiTypeAdapter();
        tocRefViewBinder = new TocRefViewBinder(new TocRefViewBinder.OnClick() {
            @Override
            public void onItemClick(int position) {
                rvContent.scrollToPosition(position);
                tocRefViewBinder.updateCurrentPosition(position);
                closeDrawer();
            }
        });
        chapterAdapter.register(TOCReference.class, tocRefViewBinder);
        rvChapter.setAdapter(chapterAdapter);

        String unzipDir = Constant.PATH_EPUB + "/" + FileUtils.md5(mFilePath);

        rvContent = (MotionRecyclerView) findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        contentAdapter = new MultiTypeAdapter();
//        contentAdapter.register(TOCReference.class, new TocRefViewBinder());
        contentAdapter.register(TOCReference.class, new EpubContentViewBinder(unzipDir));
        rvContent.setEventListener( new MotionRecyclerView.EventListener() {
            @Override
            public void onClick() {
                toggleToolBarVisible();
            }

            @Override
            public void onPullDown() {
                showStatusBar();
            }

            @Override
            public void onPullUp() {
                hideStatusBar();
            }
        });
        rvContent.setAdapter(contentAdapter);

        loadBook(unzipDir);

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    showAppBar();
                } else {
                    hideAppBar();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.epub_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    private void loadBook(final String unzipDir) {
        showLoading();
        Observable.create(new ObservableOnSubscribe<List<TOCReference>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<TOCReference>> observable) throws Exception {
                try {
                    // 打开书籍
                    EpubReader reader = new EpubReader();
                    InputStream is = new FileInputStream(mFilePath);
                    mBook = reader.readEpub(is);

                    mTocReferences = (ArrayList<TOCReference>) mBook.getTableOfContents().getTocReferences();
                    mSpineReferences = mBook.getSpine().getSpineReferences();

                    List<TOCReference> list = flatTocRef();
                    observable.onNext(list);

                    // 解压epub至缓存目录
                    if (!new File(unzipDir).exists()) {
                        FileUtils.unzipFile(mFilePath, unzipDir);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
//                    observable.onError(e);
                }
                observable.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoading();
                    }
                }).doOnNext(new Consumer<List<TOCReference>>() {
            @Override
            public void accept(List<TOCReference> list) throws Exception {
                chapterAdapter.setItems(list);
                chapterAdapter.notifyDataSetChanged();
                contentAdapter.setItems(list);
                contentAdapter.notifyDataSetChanged();
            }
        }).subscribe();
    }

    private void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        if (progressBar == null) {
            progressBar = (ProgressBar) findViewById(R.id.pb_epub);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private List<TOCReference> flatTocRef() {
        List<TOCReference> list = new ArrayList<>();
        flatTocRef(list, mTocReferences, "");
        return list;
    }

    private void flatTocRef(List<TOCReference> list, List<TOCReference> mTocReferences, String prefix) {
        for (int i = 0; i < mTocReferences.size(); i++) {
            TOCReference tocReference = mTocReferences.get(i);
            String newPrefix = String.format("%s%d.", prefix, i);
            String newTitle = newPrefix + " " + tocReference.getTitle();
            tocReference.setTitle(newTitle);
            list.add(tocReference);
            if (tocReference.getChildren() != null && tocReference.getChildren().size() > 0) {
                flatTocRef(list, tocReference.getChildren(), newPrefix);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // re-layout root view
        getWindow().getDecorView().requestLayout();
    }

    boolean mIsActionBarVisible = true;

    public void toggleToolBarVisible() {
        if (mIsActionBarVisible) {
            toolbarAnimateHide();
        } else {
            toolbarAnimateShow();
        }
    }

    private void toolbarAnimateHide() {
        if (mIsActionBarVisible) {
                            hideStatusBar();
            mIsActionBarVisible = false;
        }
    }

    private void hideAppBar() {
        appBarLayout.animate()
                .translationY(-appBarLayout.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });
    }

    private void toolbarAnimateShow() {
        if (!mIsActionBarVisible) {
            showStatusBar();
            mIsActionBarVisible = true;
        }
    }

    private void showAppBar() {
        appBarLayout.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
//                            showStatusBar();
                    }
                });
    }

    protected void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View. SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;// | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
//        getSupportActionBar().hide();
    }

    protected void showStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View. SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;// | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
//        getSupportActionBar().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }
}
