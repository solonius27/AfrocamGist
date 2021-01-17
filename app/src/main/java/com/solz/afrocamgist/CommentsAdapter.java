package com.solz.afrocamgist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter {




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
