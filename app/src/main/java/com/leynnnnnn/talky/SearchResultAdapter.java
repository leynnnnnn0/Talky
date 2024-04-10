package com.leynnnnnn.talky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    Context context;
    ArrayList<UserAccount> userAccounts;
    FavoriteInterface favoriteInterface;

    public SearchResultAdapter(Context context, ArrayList<UserAccount> userAccounts, FavoriteInterface favoriteInterface) {
        this.context = context;
        this.userAccounts = userAccounts;
        this.favoriteInterface = favoriteInterface;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_preview, parent, false);
        return new ViewHolder(view, favoriteInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        holder.image.setImageResource(userAccounts.get(position).getProfilePicture());
        holder.username.setText(userAccounts.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return userAccounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView username;
        public ViewHolder(@NonNull View itemView, FavoriteInterface favoriteInterface) {
            super(itemView);
            image = itemView.findViewById(R.id.accountProfilePicture);
            username = itemView.findViewById(R.id.accountUsername);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    favoriteInterface.onProfileClick(userAccounts.get(pos).getEmail(), userAccounts.get(pos).getUsername(), userAccounts.get(0).getPassword(), userAccounts.get(pos).getProfilePicture());
                }
            });
        }
    }
}
