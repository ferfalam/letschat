package com.faridcodeur.letschat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.ChatScreenActivity;
//import com.faridcodeur.letschat.entities.Message;
import com.faridcodeur.letschat.entities.Message;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    ArrayList<Message> list;
    public static final int MESSAGE_TYPE_IN = 1;
    public static final int MESSAGE_TYPE_OUT = 2;
    public static final int AUDIO_MESSAGE_TYPE_OUT = 3;
    public static final int AUDIO_MESSAGE_TYPE_IN = 4;
    public static final int FILE_MESSAGE_TYPE_OUT = 5;
    public static final int FILE_MESSAGE_TYPE_IN = 6;
    public static final int GALLERY_MESSAGE_TYPE_OUT = 7;
    public static final int GALLERY_MESSAGE_TYPE_IN = 8;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final String TAG_PLAY_AUDIO = "AudioPlay";
    private static final int UPDATE_AUDIO_PROGRESS_BAR = 3;

    private MediaPlayer audioPlayer = null;

    //public boolean audioIsPlaying;

    public RecyclerViewAdapter(Context context, ArrayList<Message> list) {
        // you can pass other parameters in constructor
        this.context = context;
        this.list = list;
    }

    private class MessageInViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV,dateTV;
        MessageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.text_in);
            dateTV = itemView.findViewById(R.id.receive_time);
        }

        void bind(int position) {
            Message messageModel = list.get(position);
            messageTV.setText(messageModel.message);
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));

        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV,dateTV;
        MessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.text_out);
            dateTV = itemView.findViewById(R.id.send_time);
        }
        void bind(int position) {
            Message messageModel = list.get(position);
            messageTV.setText(messageModel.message);
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    private class FileMessageOutViewHolder extends RecyclerView.ViewHolder {
        TextView messageTV,dateTV;
        FileMessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.file_name);
            dateTV = itemView.findViewById(R.id.file_send_time);
        }
        void bind(int position) {
            Message messageModel = list.get(position);
            messageTV.setText(messageModel.message);
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    private class FileMessageInViewHolder extends RecyclerView.ViewHolder {
        TextView messageTV,dateTV;
        FileMessageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.file_in_name);
            dateTV = itemView.findViewById(R.id.file_receive_time);
        }
        void bind(int position) {
            Message messageModel = list.get(position);
            messageTV.setText(messageModel.message);
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    private class ImageMessageOutViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView dateTV;
        ImageMessageOutViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_out);
            dateTV = itemView.findViewById(R.id.image_send_time);
        }
        void bind(int position) {
            Message messageModel = list.get(position);
            File imgFile = new File(messageModel.message);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }
    private class ImageMessageInViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView dateTV;
        ImageMessageInViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_in);
            dateTV = itemView.findViewById(R.id.image_receive_time);
        }
        void bind(int position) {
            Message messageModel = list.get(position);
            File imgFile = new  File(messageModel.message);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }


    private class AudioMessageOutViewHolder extends RecyclerView.ViewHolder {

        LinearProgressIndicator progressIndicator;
        TextView dateTV;
        ImageButton playButtonOut;
        private Handler audioProgressHandler = null;
        private Thread updateAudioPalyerProgressThread = null;

        AudioMessageOutViewHolder(final View itemView) {
            super(itemView);
            playButtonOut = itemView.findViewById(R.id.out_play);
            dateTV = itemView.findViewById(R.id.audio_send_time);
            progressIndicator = itemView.findViewById(R.id.out_progress);
        }

        void bind(int position) {
            Message messageModel = list.get(position);
            dateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();
                }
            });

            if(audioProgressHandler==null) {
                audioProgressHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {
                            if(audioPlayer!=null) {
                                // Get current play time.
                                int currPlayPosition = audioPlayer.getCurrentPosition();
                                // Get total play time.
                                int totalTime = audioPlayer.getDuration();
                                // Calculate the percentage.
                                int currProgress = (currPlayPosition * 100) / totalTime;
                                // Update progressbar.
                                progressIndicator.setProgress(currProgress, true);
                            }
                        }
                        return false;
                    }
                });
            }

            playButtonOut.setOnClickListener(new View.OnClickListener() {
                boolean mStartPlaying = true;
                boolean audioIsPlaying = false;

                @Override
                public void onClick(View v) {
                    File vocal = new File(messageModel.path);
                    Uri audioUri = Uri.parse(vocal.toString());

                    if (!audioIsPlaying){
                        if(vocal.exists()) {
                            playButtonOut.setImageResource(R.drawable.ic_pause);
                            stopCurrentPlayAudio();
                            initAudioPlayer(messageModel.path, audioUri);
                            audioPlayer.start();
                            audioIsPlaying = !audioIsPlaying;
                            Toast.makeText(context, ""+audioIsPlaying, Toast.LENGTH_SHORT).show();
                            // Display progressbar.
                            progressIndicator.setVisibility(LinearProgressIndicator.VISIBLE);
                            if(updateAudioPalyerProgressThread == null) {
                                // Create the thread.
                                updateAudioPalyerProgressThread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            while (audioIsPlaying) {
                                                if (audioProgressHandler != null) {
                                                    // Send update audio player progress message to main thread message queue.
                                                    android.os.Message msg = new android.os.Message();
                                                    msg.what = UPDATE_AUDIO_PROGRESS_BAR;
                                                    audioProgressHandler.sendMessage(msg);
                                                    Thread.sleep(100);
                                                }

                                                if (audioPlayer.getCurrentPosition() == audioPlayer.getDuration()){
                                                    playButtonOut.setImageResource(R.drawable.ic_play);
                                                    audioProgressHandler.removeCallbacks(this);
                                                    stopCurrentPlayAudio();

                                                    audioIsPlaying = false;
                                                    //progressIndicator.setProgress(0);
                                                }

                                            }
                                        } catch (InterruptedException ex) {
                                            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
                                        }
                                    }

                                };
                                updateAudioPalyerProgressThread.start();
                            }
                        }
                        else {
                            Toast.makeText(context, "Please specify an audio file to play.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        playButtonOut.setImageResource(R.drawable.ic_play);
                        audioPlayer.pause();
                        audioIsPlaying = !audioIsPlaying;
                        updateAudioPalyerProgressThread = null;
                    }
                    //audioIsPlaying = !audioIsPlaying;
                }
//                    onPlay(mStartPlaying, messageModel.path);
//                    if (mStartPlaying) {
//                        playButtonOut.setImageResource(R.drawable.ic_pause);
//                        stopPlaying(audioPlayer);
//                    } else {
//                        playButtonOut.setImageResource(R.drawable.ic_play);
//                        startPlaying(messageModel.message);
//                    }
//                    mStartPlaying = !mStartPlaying;
            });

            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    private class AudioMessageInViewHolder extends RecyclerView.ViewHolder {

        LinearProgressIndicator progressIndicator;
        TextView dateTV;
        ImageButton playButtonIn;
        private Handler audioProgressHandler = null;
        private Thread updateAudioPalyerProgressThread = null;

        AudioMessageInViewHolder(final View itemView) {
            super(itemView);
            playButtonIn = itemView.findViewById(R.id.in_play);
            dateTV = itemView.findViewById(R.id.audio_receive_time);
            progressIndicator = itemView.findViewById(R.id.in_progress);
        }

        void bind(int position) {
            Message messageModel = list.get(position);
            dateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();
                }
            });

            if(audioProgressHandler==null) {
                audioProgressHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {
                            if(audioPlayer!=null) {
                                // Get current play time.
                                int currPlayPosition = audioPlayer.getCurrentPosition();
                                // Get total play time.
                                int totalTime = audioPlayer.getDuration();
                                // Calculate the percentage.
                                int currProgress = (currPlayPosition * 100) / totalTime;
                                // Update progressbar.
                                progressIndicator.setProgress(currProgress, true);
                            }
                        }
                        return false;
                    }
                });
            }

            playButtonIn.setOnClickListener(new View.OnClickListener() {
                boolean mStartPlaying = true;
                boolean audioIsPlaying = false;

                @Override
                public void onClick(View v) {
                    File vocal = new File(messageModel.path);
                    Uri audioUri = Uri.parse(vocal.toString());

                    if (!audioIsPlaying){
                        if(vocal.exists()) {
                            playButtonIn.setImageResource(R.drawable.ic_pause);
                            stopCurrentPlayAudio();
                            initAudioPlayer(messageModel.path, audioUri);
                            audioPlayer.start();
                            audioIsPlaying = !audioIsPlaying;
                            Toast.makeText(context, ""+audioIsPlaying, Toast.LENGTH_SHORT).show();
                            // Display progressbar.
                            progressIndicator.setVisibility(LinearProgressIndicator.VISIBLE);
                            if(updateAudioPalyerProgressThread == null) {
                                // Create the thread.
                                updateAudioPalyerProgressThread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            while (audioIsPlaying) {
                                                if (audioProgressHandler != null) {
                                                    // Send update audio player progress message to main thread message queue.
                                                    android.os.Message msg = new android.os.Message();
                                                    msg.what = UPDATE_AUDIO_PROGRESS_BAR;
                                                    audioProgressHandler.sendMessage(msg);
                                                    Thread.sleep(100);
                                                }

                                                if (audioPlayer.getCurrentPosition() == audioPlayer.getDuration()){
                                                    playButtonIn.setImageResource(R.drawable.ic_play);
                                                    audioProgressHandler.removeCallbacks(this);
                                                    stopCurrentPlayAudio();

                                                    audioIsPlaying = false;
                                                    //progressIndicator.setProgress(0);
                                                }

                                            }
                                        } catch (InterruptedException ex) {
                                            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
                                        }
                                    }

                                };
                                updateAudioPalyerProgressThread.start();
                            }
                        }
                        else {
                            Toast.makeText(context, "Please specify an audio file to play.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        playButtonIn.setImageResource(R.drawable.ic_play);
                        audioPlayer.pause();
                        audioIsPlaying = !audioIsPlaying;
                        updateAudioPalyerProgressThread = null;
                    }
                    //audioIsPlaying = !audioIsPlaying;
                }
            });

            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_IN) {
            return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.message_in, parent, false));
        } else if (viewType == AUDIO_MESSAGE_TYPE_OUT){
            return new AudioMessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.audio_message_out, parent, false));
        } else if (viewType == AUDIO_MESSAGE_TYPE_IN){
            return new FileMessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.audio_message_in, parent, false));
        }else if (viewType == FILE_MESSAGE_TYPE_OUT){
            return new FileMessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.file_message_out, parent, false));
        }else if (viewType == FILE_MESSAGE_TYPE_IN){
            return new FileMessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.file_message_in, parent, false));
        }else if (viewType == GALLERY_MESSAGE_TYPE_IN){
            return new ImageMessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.image_message_in, parent, false));
        }else if (viewType == GALLERY_MESSAGE_TYPE_OUT){
            return new ImageMessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.image_message_out, parent, false));
        }
        return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.message_out, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (list.get(position).messageType == MESSAGE_TYPE_IN) {
            ((MessageInViewHolder) holder).bind(position);
        } else if (list.get(position).messageType == GALLERY_MESSAGE_TYPE_OUT) {
            ((ImageMessageOutViewHolder) holder).bind(position);
        } else if (list.get(position).messageType == AUDIO_MESSAGE_TYPE_OUT) {
            ((AudioMessageOutViewHolder) holder).bind(position);
        } else if (list.get(position).messageType == FILE_MESSAGE_TYPE_OUT) {
            ((FileMessageOutViewHolder) holder).bind(position);
        } else if (list.get(position).messageType == AUDIO_MESSAGE_TYPE_IN) {
            ((AudioMessageInViewHolder) holder).bind(position);
        } else if (list.get(position).messageType == FILE_MESSAGE_TYPE_IN) {
            ((FileMessageInViewHolder) holder).bind(position);
        } else if (list.get(position).messageType == GALLERY_MESSAGE_TYPE_IN) {
            ((ImageMessageInViewHolder) holder).bind(position);
        } else{
            ((MessageOutViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).messageType;
    }


    private void initAudioPlayer(String audioPath, Uri audioFileUri)
    {

        try {
            if(audioPlayer == null)
            {
                audioPlayer = new MediaPlayer();
                String audioFilePath = audioPath.trim();
                Log.d(TAG_PLAY_AUDIO, audioFilePath);
                if(audioFilePath.toLowerCase().startsWith("http://"))
                {
                    // Web audio from a url is stream music.
                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // Play audio from a url.
                    audioPlayer.setDataSource(audioFilePath);
                }else
                {
                    if(audioFileUri != null)
                    {
                        // Play audio from selected local file.
                        audioPlayer.setDataSource(context, audioFileUri);

                    }
                }
                audioPlayer.prepare();
            }
        }catch(IOException ex)
        {
            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);

        }
    }
    private void stopCurrentPlayAudio()
    {
        if(audioPlayer!=null && audioPlayer.isPlaying())
        {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }
}
