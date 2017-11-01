package com.wzw.epub;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.wzw.gitbook.R;
import com.wzw.gitbook.epub.FileUtils;

import java.io.File;

import me.drakeet.multitype.ItemViewBinder;
import nl.siegmann.epublib.domain.TOCReference;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class EpubContentViewBinder extends ItemViewBinder<TOCReference, EpubContentViewBinder.Holder> implements View.OnClickListener {

    String unzipDir;

    public EpubContentViewBinder(String unzipDir) {
        this.unzipDir = unzipDir;
    }

    @Override
    public void onClick(View view) {
    }

    static class Holder extends RecyclerView.ViewHolder {

        WebView webView;
        TextView tvPage;

        Holder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.wv_content);
            tvPage = itemView.findViewById(R.id.tv_page);
        }
    }

    @NonNull @Override
    protected Holder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_epub_content_view, parent, false);
        Holder holder = new Holder(root);
        root.setOnClickListener(this);
        root.setMinimumHeight(parent.getHeight());
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, @NonNull TOCReference tocReference) {
        String pageHref = tocReference.getResource().getHref();

        holder.webView.loadUrl("file://" + unzipDir + File.separator + pageHref);

        holder.tvPage.setText(String.format("%d/%d", getPosition(holder) + 1, getAdapter().getItemCount()));
    }

}
