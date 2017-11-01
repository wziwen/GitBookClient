package com.wzw.epub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wzw.gitbook.R;
import com.wzw.gitbook.epub.Constant;
import com.wzw.gitbook.epub.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubReaderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private RecyclerView rvContent;
    private MultiTypeAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub_reader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        mFilePath = bundle.getString("file_path");
        mFileName = bundle.getString("file_name");

        rvChapter = (RecyclerView) findViewById(R.id.rv_chapter);
        rvChapter.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter = new MultiTypeAdapter();
        chapterAdapter.register(TOCReference.class, new TocRefViewBinder());
        rvChapter.setAdapter(chapterAdapter);

        String unzipDir = Constant.PATH_EPUB + "/" + FileUtils.md5(mFilePath);

        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        rvContent .setLayoutManager(new LinearLayoutManager(this));
        contentAdapter= new MultiTypeAdapter();
        contentAdapter.register(TOCReference.class, new EpubContentViewBinder(unzipDir));
        rvContent.setAdapter(contentAdapter);


        loadBook(unzipDir);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.epub_reader, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadBook(String unzipDir) {
        try {
            // 打开书籍
            EpubReader reader = new EpubReader();
            InputStream is = new FileInputStream(mFilePath);
            mBook = reader.readEpub(is);

            mTocReferences = (ArrayList<TOCReference>) mBook.getTableOfContents().getTocReferences();
            mSpineReferences = mBook.getSpine().getSpineReferences();

            List<TOCReference> list = flatTocRef();
            chapterAdapter.setItems(list);
            contentAdapter.setItems(list);

            // 解压epub至缓存目录
            FileUtils.unzipFile(mFilePath, unzipDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<TOCReference> flatTocRef() {
        List<TOCReference> list = new ArrayList<>();
        flatTocRef(list, mTocReferences);
        return list;
    }

    private void flatTocRef(List<TOCReference> list, List<TOCReference> mTocReferences) {
        for (TOCReference tocReference : mTocReferences) {
            list.add(tocReference);
            if (tocReference.getChildren() != null && tocReference.getChildren().size() > 0) {
                flatTocRef(list, tocReference.getChildren());
            }
        }
    }
}
