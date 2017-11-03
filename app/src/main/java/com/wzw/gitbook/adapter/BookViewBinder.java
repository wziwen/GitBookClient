package com.wzw.gitbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liulishuo.filedownloader.FileDownloader;
import com.wzw.epub.EpubReaderActivity;
import com.wzw.gitbook.R;
import com.wzw.gitbook.WebActivity;
import com.wzw.gitbook.download.TasksManager;
import com.wzw.gitbook.download.TasksManagerModel;
import com.wzw.gitbook.entity.BookInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ziwen.wen on 2017/10/24.
 */
public class BookViewBinder extends ItemViewBinder<BookInfo, BookViewBinder.Holder> implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        BookInfo bookInfo = (BookInfo) view.getTag();
        if (R.id.btn_download == view.getId()) {
            TasksManagerModel model = TasksManager.getImpl()
                    .addTask(bookInfo.getTitle(), bookInfo.getUrls().getDownload().getEpub());
            FileDownloader.getImpl().create(model.getUrl())
                    .setPath(model.getPath())
                    .setCallbackProgressTimes(100)
                    .start();

            view.setVisibility(View.GONE);
            return;
        }
        // // TODO: 2017/10/25 保存到浏览记录
        if (getDownloadStatus(bookInfo) == 1) {
            TasksManagerModel model = TasksManager.getImpl().getById(bookInfo.getDownloadId());
            EpubReaderActivity.start(view.getContext(), model.getPath(), model.getName());
        } else {
            showContent(view.getContext(), bookInfo.getUrls().getContent());
        }
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDesc;
        TextView tvAuthor;
        TextView tvStars;
        TextView tvLastUpdateTime;
        ImageView ivAvatar;
        Button btnDownload;
        TextView tvDownloadStatus;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvStars = itemView.findViewById(R.id.tv_stars);
            tvLastUpdateTime = itemView.findViewById(R.id.tv_last_update_time);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            btnDownload = itemView.findViewById(R.id.btn_download);
            tvDownloadStatus = itemView.findViewById(R.id.tv_download_status);
        }
    }

    @NonNull @Override
    protected Holder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_book_view, parent, false);
        Holder holder = new Holder(root);
        root.setOnClickListener(this);
        holder.btnDownload.setOnClickListener(this);
        return holder;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss.SSS");
    @Override
    protected void onBindViewHolder(@NonNull Holder holder, @NonNull BookInfo bookInfo) {
        holder.tvTitle.setText(bookInfo.getTitle());
        if (TextUtils.isEmpty(bookInfo.getDescription())) {
            holder.tvDesc.setVisibility(View.GONE);
        } else {
            holder.tvDesc.setVisibility(View.VISIBLE);
            holder.tvDesc.setText(bookInfo.getDescription());
        }
        holder.tvAuthor.setText(bookInfo.getAuthor().getName());
        holder.tvStars.setText(String.format("%d Star", bookInfo.getCounts().getStars()));

        holder.itemView.setTag(bookInfo);
        holder.btnDownload.setTag(bookInfo);

        String avatar = bookInfo.getAuthor().getUrls().getAvatar();
        // https://s.gravatar.com 图片下的头像都显示不出来, 这里直接不请求了
        if (avatar != null && avatar.startsWith("https://s.gravatar.com")) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.baby_cats)
                    .into(holder.ivAvatar);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(bookInfo.getAuthor().getUrls().getAvatar())
                    .apply(RequestOptions.errorOf(R.drawable.baby_cats))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivAvatar);
        }

        int downloadStatus = getDownloadStatus(bookInfo);
        if (downloadStatus == 0) {
            holder.btnDownload.setVisibility(View.VISIBLE);
            holder.tvDownloadStatus.setVisibility(View.GONE);
        } else {
            holder.btnDownload.setVisibility(View.GONE);
            holder.tvDownloadStatus.setVisibility(View.VISIBLE);
            holder.tvDownloadStatus.setText(
                    downloadStatus == 1 ? "downloaded" : "downloading"
            );
        }


        Date date = null;
        try {
            date = simpleDateFormat.parse(bookInfo.getDates().getBuild().replace("T", " ").replace("Z", ""));
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(holder.itemView.getContext());
            String lastUpdateTime = dateFormat.format(date);
            holder.tvLastUpdateTime.setText(lastUpdateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvLastUpdateTime.setText("");
        }
    }

    private int getDownloadStatus(BookInfo bookInfo) {
        int downloadId = bookInfo.getDownloadId();
        if (downloadId == -1) {
            String url = bookInfo.getUrls().getDownload().getEpub();
            downloadId = TasksManager.getImpl().generateId(url, TasksManager.getImpl().createPath(url));
            bookInfo.setDownloadId(downloadId);
        }
        // has not download
        TasksManagerModel model = TasksManager.getImpl().getById(downloadId);
        if (model != null ) {
            int status = TasksManager.getImpl().getStatus(model.getId(), model.getPath());
            return TasksManager.getImpl().isDownloaded(status) ? 1 : 2;
        }
        return 0;
    }

    private void showContent(Context activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }
}
