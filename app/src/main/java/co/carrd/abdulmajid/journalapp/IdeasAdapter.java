package co.carrd.abdulmajid.journalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.carrd.abdulmajid.journalapp.model.Idea;
import co.carrd.abdulmajid.journalapp.util.DateTimeUtils;

/**
 * Created by Abdulmajid on 6/30/18.
 */

public class IdeasAdapter extends RecyclerView.Adapter<IdeasAdapter.IdeasHolder> {

    private List<Idea> ideas;
    private OnIdeaClickListener onIdeaClickListener;
    private Context context;
    private SimpleDateFormat sdf;

    IdeasAdapter(Context context, OnIdeaClickListener onIdeaClickListener) {
        this.context = context;
        ideas = new ArrayList<>();
        this.onIdeaClickListener = onIdeaClickListener;
        this.sdf = new SimpleDateFormat(DateTimeUtils.SERVER_DATE_TIME_PATTERN, Locale.getDefault());
    }

    class IdeasHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mBoldLetter, mIdeaTitle, mIdeaBrief, mDateRel, mTagOne, mTagTwo, mTagThree;
        IdeasHolder(View v) {
            super(v);
            mBoldLetter = v.findViewById(R.id.tv_bold_letter);
            mIdeaTitle = v.findViewById(R.id.tv_idea_title);
            mIdeaBrief = v.findViewById(R.id.tv_idea_brief_description);
            mDateRel = v.findViewById(R.id.tv_date_created);
            mTagOne = v.findViewById(R.id.tv_tag_one);
            mTagTwo = v.findViewById(R.id.tv_tag_two);
            mTagThree = v.findViewById(R.id.tv_tag_three);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIdeaClickListener.onClick(itemView, ideas.get(getAdapterPosition()));
                }
            });

        }
    }

    @NonNull
    @Override
    public IdeasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.idea_list_item, parent, false);
        return new IdeasHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeasHolder holder, int position) {
        Idea idea = ideas.get(position);
        holder.mBoldLetter.setText(idea.getTitle().substring(0, 1));
        holder.mIdeaTitle.setText(idea.getTitle());
        holder.mIdeaBrief.setText(idea.getDescription());


        try {
            holder.mDateRel.setText(DateTimeUtils.getRelativeTime(sdf.parse(idea.getCreatedAt())));
        } catch (Exception err){err.printStackTrace();}

        String [] tags = idea.getWordTags().split(",");

        if (tags.length > 0) {
            holder.mTagOne.setVisibility(View.VISIBLE);
            holder.mTagOne.setText(tags[0]);
        } else {
            holder.mTagOne.setVisibility(View.GONE);
        }

        if (tags.length > 1) {
            holder.mTagTwo.setVisibility(View.VISIBLE);
            holder.mTagTwo.setText(tags[1]);
        } else {
            holder.mTagTwo.setVisibility(View.GONE);
        }

        if (tags.length > 2) {
            holder.mTagThree.setVisibility(View.VISIBLE);
            holder.mTagThree.setText(tags[2]);
        } else {
            holder.mTagThree.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ideas.size();
    }


    public interface OnIdeaClickListener {
        void onClick(View view, Idea idea);
    }

    void setIdeas(List<Idea> ideas){
        this.ideas = ideas;
        notifyDataSetChanged();
    }

    void updateList(){
        notifyDataSetChanged();
    }

    Idea retrieveTask(int position){
        return ideas.get(position);
    }
}
