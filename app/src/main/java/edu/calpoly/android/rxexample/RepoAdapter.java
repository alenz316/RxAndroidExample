package edu.calpoly.android.rxexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.calpoly.android.rxexample.model.Repo;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private ArrayList<Repo> mRepos;

    public RepoAdapter(ArrayList<Repo> repos) {
        this.mRepos = repos;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.repo_item_view;
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.bind(mRepos.get(position));
    }

    @Override
    public int getItemCount() {
        return mRepos.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTextView;

        public RepoViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mTextView = (TextView) itemView.findViewById(R.id.title);
        }

        private void bind(Repo r) {
            Glide.with(itemView.getContext())
                    .load(r.getOwner().getAvatarUrl())
                    .into(mImageView);
            mTextView.setText(r.getName());
        }
    }
}
