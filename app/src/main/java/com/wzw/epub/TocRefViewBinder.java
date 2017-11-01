package com.wzw.epub;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzw.gitbook.R;

import me.drakeet.multitype.ItemViewBinder;
import nl.siegmann.epublib.domain.TOCReference;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class TocRefViewBinder extends ItemViewBinder<TOCReference, TocRefViewBinder.Holder> implements View.OnClickListener {

    @Override
    public void onClick(View view) {
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    @NonNull @Override
    protected Holder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_toc_ref_view, parent, false);
        Holder holder = new Holder(root);
        root.setOnClickListener(this);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, @NonNull TOCReference bookInfo) {
        holder.tvTitle.setText(bookInfo.getTitle());
    }

}
