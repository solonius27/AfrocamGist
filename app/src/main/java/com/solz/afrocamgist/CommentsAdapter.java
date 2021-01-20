package com.solz.afrocamgist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.solz.afrocamgist.Data.Models.Comments;
import com.solz.afrocamgist.Data.Models.TLData;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentAdapterViewHolder>{

    private List<Comments> mEntries;
    private Context mContext;

    public CommentsAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public CommentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.comment_custom, parent, false);
        return new CommentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapterViewHolder holder, int position) {

        final Comments tEntry = mEntries.get(position);

        String name = tEntry.getName();
        String text = tEntry.getCommentText();
        String image = tEntry.getProfilepic();

        holder.name.setText(name);
        holder.mainText.setText(text);

        Picasso.get().load(String.format("https://cdn.staging.afrocamgist.com/%s", image)).placeholder(R.drawable.profilepic).into(holder.imageUrl);

    }

    public void setTasks(List<Comments> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    public void clearTasks(List<Comments> entries) {
        mEntries = entries;
        mEntries.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mEntries == null) {
            return 0;
        }
        return mEntries.size();
    }








    class CommentAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView imageUrl;
        TextView name;
        TextView mainText;

        public CommentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUrl = itemView.findViewById(R.id.comment_image);
            name = itemView.findViewById(R.id.comment_username);
            mainText = itemView.findViewById(R.id.comment_mainText);
        }
    }
}
