package com.grandpa.mangtae;//package com.grandpa.mangtae;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grandpa.mangtae.Room.CallingData;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CallingAdapter extends RecyclerView.Adapter<CallingAdapter.ViewHolder> {
    @NonNull
    @Override
    public CallingAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(getViewSrc(viewType), parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CallingAdapter.ViewHolder holder,
            int position
    ) {
        holder.bind(dataSet.get(position));
    }

    public void  filterList(ArrayList<CallingData> filteredList) {
        dataSet = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    // view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        public ViewHolder(@NonNull View itemView, int viewType){
            super(itemView);
            this.viewType = viewType;

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
//                        System.out.println("들어옴");
//                        MediaPlayer mediaPlayer;
//                        mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.test);
//                        float speed = 0.5f;
//                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
//                        mediaPlayer.start();

                        Intent intent = new Intent(v.getContext(), DetailCallActivity.class);
                        intent.putExtra("id", dataSet.get(position).id);
                        intent.putExtra("name", dataSet.get(position).name);
                        intent.putExtra("content", dataSet.get(position).content);
                        intent.putExtra("category", dataSet.get(position).category);
                        intent.putExtra("writer", dataSet.get(position).writer);
                        intent.putExtra("audioPath", dataSet.get(position).audioPath);
                        Log.d("data","id : "+dataSet.get(position).id);
                        Log.d("data","name : "+dataSet.get(position).name);
                        Log.d("data","writer : "+dataSet.get(position).writer);
                        Log.d("data","audioPath : "+dataSet.get(position).audioPath);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(CallingData item){
            if (viewType==TYPE_FIRST_POSITION){
                bindMyList(item);
            }
            else if(viewType==TYPE_WRITER_CHANGE_POSITION) {
                bindAdminList(item);
            }
            else if(viewType==TYPE_GENERAL_POSITION){
                bindGeneralList(item);
            }
        }

        private void bindMyList(CallingData item){
            TextView textViewDividingLine = itemView.findViewById(R.id.textView_dividing_line);
            TextView textViewCallingListName = itemView.findViewById(R.id.textView_calling_list_name);
            ImageView imageViewVoiceIcon = itemView.findViewById(R.id.imageView_voice_icon);
            ImageView imageViewVideoIcon = itemView.findViewById(R.id.imageView_video_icon);
            TextView textViewName = itemView.findViewById(R.id.textView_name);
            textViewDividingLine.setVisibility(View.VISIBLE);
            textViewCallingListName.setVisibility(View.VISIBLE);
            textViewCallingListName.setText("내 전화목록");
            textViewName.setText(dataSet.get(getAdapterPosition()).name);
            if(dataSet.get(getAdapterPosition()).getCategory().equals("음성")){
                imageViewVideoIcon.setVisibility(View.GONE);
                imageViewVoiceIcon.setVisibility(View.VISIBLE);
            }
            else{
                imageViewVoiceIcon.setVisibility(View.GONE);
                imageViewVideoIcon.setVisibility(View.VISIBLE);
            }
        }

        private void bindAdminList(CallingData item){
            TextView textViewDividingLine = itemView.findViewById(R.id.textView_dividing_line);
            TextView textViewCallingListName = itemView.findViewById(R.id.textView_calling_list_name);
            ImageView imageViewVoiceIcon = itemView.findViewById(R.id.imageView_voice_icon);
            ImageView imageViewVideoIcon = itemView.findViewById(R.id.imageView_video_icon);
            TextView textViewName = itemView.findViewById(R.id.textView_name);
            textViewDividingLine.setVisibility(View.VISIBLE);
            textViewCallingListName.setVisibility(View.VISIBLE);
            textViewCallingListName.setText("기존 전화목록");
            textViewName.setText(dataSet.get(getAdapterPosition()).name);
            if(dataSet.get(getAdapterPosition()).getCategory().equals("음성")){
                imageViewVideoIcon.setVisibility(View.GONE);
                imageViewVoiceIcon.setVisibility(View.VISIBLE);
            }
            else{
                imageViewVoiceIcon.setVisibility(View.GONE);
                imageViewVideoIcon.setVisibility(View.VISIBLE);
            }
        }

        private void bindGeneralList(CallingData item){
//            TextView textViewDividingLine = itemView.findViewById(R.id.textView_dividing_line);
//            TextView textViewCallingListName = itemView.findViewById(R.id.textView_calling_list_name);
            ImageView imageViewVoiceIcon = itemView.findViewById(R.id.imageView_voice_icon);
            ImageView imageViewVideoIcon = itemView.findViewById(R.id.imageView_video_icon);
            TextView textViewName = itemView.findViewById(R.id.textView_name);
            textViewName.setText(dataSet.get(getAdapterPosition()).name);
            if(dataSet.get(getAdapterPosition()).getCategory().equals("음성")){
                imageViewVideoIcon.setVisibility(View.GONE);
                imageViewVoiceIcon.setVisibility(View.VISIBLE);
            }
            else{
                imageViewVoiceIcon.setVisibility(View.GONE);
                imageViewVideoIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    // view type
    private final int TYPE_FIRST_POSITION = 101;  // 내가 작성한 글이 있고 index가 0인 경우
    private final int TYPE_WRITER_CHANGE_POSITION = 102;  // 내가 올린 글에서 관리자가 올린 글로 바뀌는 경우
    private final int TYPE_GENERAL_POSITION = 103;    // 그 외 구분선 없이 뷰를 보여주는 경우

    private int getViewSrc(int viewType){
        return R.layout.recyclerview_calling_general;
//        if (viewType==TYPE_RECEIVED_MESSAGE){
//            return R.layout.recycler_received_message;
//        } else {
//            return R.layout.recycler_sent_message;
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position > 0){
            // 내 전화목록에서 기존 전화목록으로 바뀌는 부분이기 때문에 위에 기존전화목록 구분선 표시 필요.
            if(dataSet.get(position-1).getWriter().equals("나") && dataSet.get(position).getWriter().equals("관리자")){
                return TYPE_WRITER_CHANGE_POSITION;
            }
            // 변화가 없는 부분이기 때문에 그대로 리스트 출력
            else{
                return TYPE_GENERAL_POSITION;
            }
        }

        else{
            // 가장 첫 뷰이지만 내가 작성한 전화가 없는 경우이기 때문에 기존전화목록 구분선 표시 필요.
            if(dataSet.get(position).getWriter().equals("관리자")){
                return TYPE_WRITER_CHANGE_POSITION;
            }
            // 가장 첫 뷰이고 내가 작성한 전화가 있는 경우이기 때문에 내 전화목록 구분선 표시 필요.
            else{
                return TYPE_FIRST_POSITION;
            }
        }
    }

    // data
    private ArrayList<CallingData> dataSet = new ArrayList();

    public void submitData(ArrayList<CallingData> newData){
        dataSet = newData;
        notifyDataSetChanged();
    }
}



