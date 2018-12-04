package com.example.blescanning;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder>
{
    private Context mContext;
    private List<Lecture> lectureList;

    public LectureAdapter(Context mContext, List<Lecture> lectureList)
    {
        this.mContext = mContext;
        this.lectureList = lectureList;
    }

    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_listlectures, null);

        return new LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder lectureViewHolder, int i)
    {
        Lecture newLecture = lectureList.get(i);
        String newString = newLecture.getStartTime() + " - " + newLecture.getEndTime();

        lectureViewHolder.lectureId.setText(newLecture.getCourse_id());
        lectureViewHolder.name.setText(newLecture.getName());
        lectureViewHolder.startEndTime.setText(newString);
    }

    @Override
    public int getItemCount()
    {
        return lectureList.size();
    }

    class LectureViewHolder extends RecyclerView.ViewHolder
    {
        TextView lectureId, name, startEndTime;

        public LectureViewHolder(@NonNull View itemView)
        {
            super(itemView);

            lectureId = itemView.findViewById(R.id.course_id);
            name = itemView.findViewById(R.id.name);
            startEndTime = itemView.findViewById(R.id.start_end_time);
        }
    }
}
