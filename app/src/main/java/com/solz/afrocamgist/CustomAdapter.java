package com.solz.afrocamgist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.solz.afrocamgist.Data.Models.TLData;
import com.solz.afrocamgist.Data.Network.GetDataService;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdapter extends  RecyclerView.Adapter<CustomAdapter.CustomAdapterViewHolder>{

    private List<TLData> mEntries;
    private Context mContext;
    RecyclerViewClickListener mListener;


    //GetDataService service;

    public CustomAdapter(Context mContext, RecyclerViewClickListener listener) {
        this.mContext = mContext;
        mListener = listener;
    }

    @NonNull
    @Override
    public CustomAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.custom_list_view, parent, false);
        return new CustomAdapterViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterViewHolder holder, int position) {

        final TLData tEntry = mEntries.get(position);



        String name = tEntry.getFirstName();
        String text = tEntry.getPostText();
        String comment =  String.valueOf(tEntry.getCommentCount());
        String likes =  String.valueOf(tEntry.getLikeCount());
        String image = tEntry.getProfileImageUrl();

        holder.like_count.setText(likes);
        holder.comment_count.setText(comment);
        holder.mainText.setText(text);
        holder.name.setText(String.format("@%s", name));

        Picasso.get().load(String.format("https://cdn.staging.afrocamgist.com/%s", image)).placeholder(R.drawable.profilepic).into(holder.imageUrl);

    }


    public void setTasks(List<TLData> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    public void clearTasks(List<TLData> entries) {
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

    public interface RecyclerViewClickListener{
        void onClick(View v, int position, List<TLData> dataEntries);
    }

    class CustomAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView imageUrl;
        ImageView deletePost;
        TextView name;
        TextView mainText;
        TextView comment_count;
        TextView like_count;
        RecyclerViewClickListener mListener;

        public CustomAdapterViewHolder(@NonNull View itemView, RecyclerViewClickListener listenert) {
            super(itemView);
            mListener = listenert;
            itemView.setOnClickListener(this);

            imageUrl = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.username);
            mainText = itemView.findViewById(R.id.mainText);
            comment_count = itemView.findViewById(R.id.commentCount);
            like_count = itemView.findViewById(R.id.likesCount);
            deletePost = itemView.findViewById(R.id.deleteImage);

            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Won't be able to recover this item!")
                            .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    final Call<JsonObject> call = MainActivity.service.deletePost("Bearer " + MainActivity.TOKEN,
                                            String.valueOf(mEntries.get(getAdapterPosition()).getPostId()));
                                    call.enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            final JsonObject body = response.body();
                                            if (body != null) {
                                                final JsonElement status = body.get("status");

                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {

                                        }
                                    });
                                    notifyItemRemoved(getAdapterPosition());
                                }
                            })
                            .show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition(), mEntries);

        }
    }
}
