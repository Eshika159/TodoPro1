package com.example.eshika.todopro;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NoteViewHolder extends RecyclerView.ViewHolder  {
    View mview;
    TextView mtitle,mtime;
    CardView noteCard;

    public NoteViewHolder(View itemView) {
        super(itemView);
        mview=itemView;
        mtitle=mview.findViewById(R.id.note_title);
        mtime=mview.findViewById(R.id.note_time);
        noteCard=mview.findViewById(R.id.note_card);
        }
        public void setNoteTitle(String title){
        mtitle.setText(title);

        }
        public void setNotetime(String time){
        mtime.setText(time);
        }
}
