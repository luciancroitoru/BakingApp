package com.example.lucia.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucia.bakingapp.R;
import com.example.lucia.bakingapp.data.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    Context mContext;
    List<Step> mDataset;
    private final StepsOnClickListener mOnClickListener;

    public interface StepsOnClickListener {
        void onItemClick(int index);
    }

    public StepsAdapter(Context context, List<Step> dataset, StepsOnClickListener listener) {
        mContext = context;
        mDataset = dataset;
        mOnClickListener = listener;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_list_item, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        String shortDescriptionText = mDataset.get(position).getStepShortDescription();
        holder.shortDescription.setText(shortDescriptionText);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shortDescription;

        public StepsViewHolder(View itemView) {
            super(itemView);
            shortDescription = itemView.findViewById(R.id.step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClick(getAdapterPosition());
        }
    }
}
