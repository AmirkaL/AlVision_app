package com.example.alinvision.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alinvision.R;

import java.util.List;

public class VideoBlockAdapter extends RecyclerView.Adapter<VideoBlockAdapter.VideoBlockViewHolder> {
    private List<VideoBlock> videoBlocks;
    private Context context;

    private int[] imageArray = {R.drawable.img, R.drawable.img2, R.drawable.img3};
    private int nextImageIndex = 0;

    public VideoBlockAdapter(List<VideoBlock> videoBlocks, Context context) {
        this.videoBlocks = videoBlocks;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoBlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_block_item, parent, false);
        return new VideoBlockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoBlockViewHolder holder, int position) {
        VideoBlock videoBlock = videoBlocks.get(position);
        holder.bind(videoBlock);
    }

    @Override
    public int getItemCount() {
        return videoBlocks.size();
    }

    class VideoBlockViewHolder extends RecyclerView.ViewHolder {
        private ImageView videoThumbnailImageView;
        private EditText captionEditText;
        private TextWatcher captionTextWatcher;

        VideoBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnailImageView = itemView.findViewById(R.id.videoThumbnailImageView);
            captionEditText = itemView.findViewById(R.id.videoCaptionEditText);
        }

        void bind(VideoBlock videoBlock) {
            captionEditText.removeTextChangedListener(captionTextWatcher);
            captionEditText.setText(videoBlock.getCaption());

            if (videoBlock.getSelectedImageResId() == 0) {
                // Установка картинки в порядке очереди
                videoBlock.setSelectedImageResId(imageArray[nextImageIndex]);
                nextImageIndex = (nextImageIndex + 1) % imageArray.length;
            }

            videoThumbnailImageView.setImageResource(videoBlock.getSelectedImageResId());

            captionTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    videoBlock.setCaption(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };

            captionEditText.addTextChangedListener(captionTextWatcher);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Изменить название");

                    final EditText input = new EditText(context);
                    input.setText(videoBlock.getCaption());
                    builder.setView(input);

                    builder.setPositiveButton("Сохранено", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newCaption = input.getText().toString();
                            videoBlock.setCaption(newCaption);
                            notifyDataSetChanged(); // Обновляем список видеоблоков после сохранения
                        }
                    });

                    builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }
    }
}
