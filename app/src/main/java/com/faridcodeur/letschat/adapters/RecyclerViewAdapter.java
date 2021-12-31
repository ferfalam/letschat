package com.faridcodeur.letschat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.Message;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;


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

    private class NoMessageInViewHolder extends RecyclerView.ViewHolder {
        TextView messageTV,dateTV;
        NoMessageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.no_in_text);
            dateTV = itemView.findViewById(R.id.no_receive_time);
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
            File imgFile = new File(messageModel.path);
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
            File imgFile = new  File(messageModel.path);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    private class AudioMessageOutViewHolder extends RecyclerView.ViewHolder {
        LinearProgressIndicator progressIndicator;
        boolean audioIsPlaying = false;
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
            MediaPlayer inPlayer = MediaPlayer.create(context, Uri.fromFile(new File(messageModel.path)));
            progressIndicator.setProgress(0);
            if(audioProgressHandler==null) {
                audioProgressHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {
                            if(inPlayer!=null) {
                                // Get current play time.
                                int currPlayPosition = inPlayer.getCurrentPosition();
                                // Get total play time.
                                int totalTime = inPlayer.getDuration();
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
            inPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioIsPlaying = false;
                    playButtonOut.setImageResource(R.drawable.ic_play_light);
                    audioProgressHandler = null;
                }
            });

            playButtonOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File vocal = new File(list.get(position).path);
                    Uri audioUri = Uri.parse(vocal.toString());
                    if (!audioIsPlaying){
                        if(vocal.exists()) {
                            playButtonOut.setImageResource(R.drawable.ic_pause_light);
                            stopCurrentPlayAudio(inPlayer);
                            initAudioPlayer(messageModel.path, audioUri, inPlayer);
                            inPlayer.start();
                            audioIsPlaying = !audioIsPlaying;
                            // Display progressbar.
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
                    }
                    else {
                            playButtonOut.setImageResource(R.drawable.ic_play_light);
                            inPlayer.pause();
                            audioIsPlaying = false;
                    }
                    //audioIsPlaying = !audioIsPlaying;
                }
            });

            dateTV.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(messageModel.messageTime));
        }
    }

    private class AudioMessageInViewHolder extends RecyclerView.ViewHolder {
        LinearProgressIndicator progressIndicator;
        boolean audioIsPlaying = false;
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
            MediaPlayer inPlayer = MediaPlayer.create(context, Uri.fromFile(new File(messageModel.path)));
            progressIndicator.setProgress(0);
            if(audioProgressHandler==null) {
                audioProgressHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {
                            if(inPlayer!=null) {
                                // Get current play time.
                                int currPlayPosition = inPlayer.getCurrentPosition();
                                // Get total play time.
                                int totalTime = inPlayer.getDuration();
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
            inPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioIsPlaying = false;
                    playButtonIn.setImageResource(R.drawable.ic_play_light);
                    audioProgressHandler = null;
                }
            });

            playButtonIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File vocal = new File(list.get(position).path);
                    Uri audioUri = Uri.parse(vocal.toString());
                    if (!audioIsPlaying){
                        if(vocal.exists()) {
                            playButtonIn.setImageResource(R.drawable.ic_pause_light);
                            stopCurrentPlayAudio(inPlayer);
                            initAudioPlayer(messageModel.path, audioUri, inPlayer);
                            inPlayer.start();
                            audioIsPlaying = !audioIsPlaying;
                            // Display progressbar.
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
                    }
                    else {
                        playButtonIn.setImageResource(R.drawable.ic_play_light);
                        inPlayer.pause();
                        audioIsPlaying = false;
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
            return new AudioMessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.audio_message_in, parent, false));
        }else if (viewType == FILE_MESSAGE_TYPE_OUT){
            return new FileMessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.file_message_out, parent, false));
        }else if (viewType == FILE_MESSAGE_TYPE_IN){
            return new FileMessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.file_message_in, parent, false));
        }else if (viewType == GALLERY_MESSAGE_TYPE_IN){
            return new ImageMessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.image_message_in, parent, false));
        }else if (viewType == GALLERY_MESSAGE_TYPE_OUT){
            return new ImageMessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.image_message_out, parent, false));
        }else if (viewType == MESSAGE_TYPE_OUT){
            return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.message_out, parent, false));
        }
        return new NoMessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.no_media, parent, false));
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
        } else if (list.get(position).messageType == MESSAGE_TYPE_OUT) {
            ((MessageOutViewHolder) holder).bind(position);
        }  else if (list.get(position).messageType == GALLERY_MESSAGE_TYPE_IN){
            ((ImageMessageInViewHolder) holder).bind(position);
        } else {
            ((NoMessageInViewHolder) holder).bind(position);
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


    private void initAudioPlayer(String audioPath, Uri audioFileUri, MediaPlayer player)
    {

        try {
            if(player == null)
            {
                player = new MediaPlayer();
                String audioFilePath = audioPath.trim();
                Log.d(TAG_PLAY_AUDIO, audioFilePath);
                if(audioFilePath.toLowerCase().startsWith("http://"))
                {
                    // Web audio from a url is stream music.
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // Play audio from a url.
                    player.setDataSource(audioFilePath);
                }else
                {
                    if(audioFileUri != null)
                    {
                        // Play audio from selected local file.
                        player.setDataSource(context, audioFileUri);

                    }
                }
                player.prepare();
            }
        }catch(IOException ex)
        {
            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);

        }
    }
    private void stopCurrentPlayAudio(MediaPlayer mediaPlayer)
    {
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
