package com.faridcodeur.letschat.activity;

import static android.content.ContentValues.TAG;
import static android.os.Environment.getDataDirectory;
import static android.os.Environment.getExternalStorageDirectory;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.faridcodeur.letschat.Audio;
import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.RecyclerViewAdapter;
import com.faridcodeur.letschat.databinding.ChatScreenActivityBinding;
import com.faridcodeur.letschat.entities.AudioMessage;
import com.faridcodeur.letschat.entities.FileMessage;
import com.faridcodeur.letschat.entities.ImageMessage;
import com.faridcodeur.letschat.entities.Message;
import com.faridcodeur.letschat.utiles.FileOpen;
import com.faridcodeur.letschat.utiles.ItemClickSupport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

public class ChatScreenActivity extends AppCompatActivity {

    private ChatScreenActivityBinding binding;
    private LinearLayoutManager lManager;
    private ArrayList<Message> messagesList;


    //AUDIO RECORD
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String audioFileName = "";
    private String imagePath = "";
    private String filePath = "";

    private MediaRecorder recorder = null;

    private MediaPlayer player = null;

    private Uri imageFileUri = null;
    private Uri fileFileUri = null;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    // Used when user require android READ_EXTERNAL_PERMISSION.
    private static final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 2;

    // Used when user select audio file.
    private static final int REQUEST_CODE_SELECT_IMAGE_FILE = 1;
    private static final int REQUEST_CODE_SELECT_NEUTRAL_FILE = 10;

    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        writeFile("/Audios");
        //writeFile("/Audios/received");
        writeFile("/Images");
        //writeFile("/Images/received");
        writeFile("/Files");
        //writeFile("/Files/received");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen_activity);
        binding = ChatScreenActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        this.configureOnClickRecyclerView();


        messagesList = new ArrayList<>();
        for (int i=0;i<10;i++) {
            messagesList.add(new Message("Hi", i % 2 == 0 ? RecyclerViewAdapter.MESSAGE_TYPE_IN : RecyclerViewAdapter.MESSAGE_TYPE_OUT, ""));
        }

        if (!messagesList.isEmpty()){
            binding.newChat.setVisibility(View.GONE);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, messagesList);

        lManager = new LinearLayoutManager(this);
        //lManager.setReverseLayout(true);
        lManager.setStackFromEnd(true);
        binding.recyclerChat.setLayoutManager(lManager);
        binding.recyclerChat.setAdapter(recyclerViewAdapter);
        binding.contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactDetailsActivity.class));
            }
        });


        binding.buttonMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editChatMessage.getText().toString().isEmpty()){
                    if (binding.editChatMessage.getText().toString().equals("kkkkk")){
                        messagesList.add(new ImageMessage(getExternalCacheDir().getAbsolutePath()+File.separatorChar+"/Images"+File.separatorChar+"/kk.jpg", RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_IN, getExternalCacheDir().getAbsolutePath()+File.separatorChar+"Images"+File.separatorChar+"/kk.jpg"));
                    } else if (binding.editChatMessage.getText().toString().equals("iiiii")){
                        messagesList.add(new FileMessage("kk.jpg", RecyclerViewAdapter.FILE_MESSAGE_TYPE_IN, getExternalCacheDir().getAbsolutePath()+File.separatorChar+"Images"+File.separatorChar+"kk.jpg"));
                    } else {
                        messagesList.add(new Message(binding.editChatMessage.getText().toString(), RecyclerViewAdapter.MESSAGE_TYPE_OUT, ""));
                    }
                    binding.editChatMessage.setText("");
                    recyclerViewAdapter.notifyDataSetChanged();
                    binding.recyclerChat.smoothScrollToPosition(messagesList.size());
                }
            }
        });


        // Sending files
        binding.buttonFileSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(ChatScreenActivity.this, requirePermission, REQUEST_CODE_READ_EXTERNAL_PERMISSION);
                }else {
                    selectFile("*/*");
                }
            }
        });

        // Sending audios
        binding.buttonAudioSend.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (audioFileName.isEmpty()){
                    audioFileName =getExternalCacheDir().getAbsolutePath() + "/Audios/vocal"+ new SimpleDateFormat("MM-dd-HH-mm-ss-SS").format(new Date())+ ".3gp";
                }
                onRecord(mStartRecording);
                if (mStartRecording) {
                    binding.buttonAudioSend.setImageResource(R.drawable.ic_red_mic);
                } else {
                    binding.buttonAudioSend.setImageResource(R.drawable.ic_vocal);
                    messagesList.add(new AudioMessage(audioFileName, RecyclerViewAdapter.AUDIO_MESSAGE_TYPE_OUT, audioFileName));
                    //Toast.makeText(getApplicationContext(), audioFileName, Toast.LENGTH_LONG).show();
                }
                mStartRecording = !mStartRecording;
                recyclerViewAdapter.notifyDataSetChanged();
                binding.recyclerChat.smoothScrollToPosition(messagesList.size() +1);
            }
        });

        // Sending images
        binding.buttonGallerySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
                {
                    String requirePermission[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(ChatScreenActivity.this, requirePermission, REQUEST_CODE_READ_EXTERNAL_PERMISSION);
                }else {
                    selectImage();
                }
                //TODO

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.chat_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_discussion:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Voulez-vous supprimer cette conversation?")
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messagesList.clear();
                        binding.recyclerChat.getAdapter().notifyDataSetChanged();
                    }
                }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirmation de suppression");
                dialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    //AUDIO RECORDING AND PLAYING METHODS


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(audioFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void selectFile(String type) {
        // Create an intent with ACTION_GET_CONTENT.
        Intent selectFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectFileIntent.setType(type);
        startActivityForResult(selectFileIntent, REQUEST_CODE_SELECT_NEUTRAL_FILE);
    }
    private void selectImage() {
        // Create an intent with ACTION_GET_CONTENT.
        Intent selectFileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        selectFileIntent.setType("image/*");
        startActivityForResult(selectFileIntent, REQUEST_CODE_SELECT_IMAGE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE_FILE) {
            if (resultCode == RESULT_OK) {
                imageFileUri = data.getData();
                String imageName = imageFileUri.getPath().split("/")[imageFileUri.getPath().split("/").length-1]+".jpg";
                copyFileFromUri(getApplicationContext(), imageFileUri,imageName, "Images");
                imagePath = getExternalCacheDir().getAbsolutePath()+File.separatorChar+"Images"+File.separatorChar+imageName;
                Toast.makeText(getApplicationContext(),imageName, Toast.LENGTH_LONG).show();
                ImageMessage im = new ImageMessage(imagePath, RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_OUT, imagePath);
                messagesList.add(im);
                recyclerViewAdapter.notifyDataSetChanged();
                binding.recyclerChat.smoothScrollToPosition(messagesList.size());
                            }
        } else if (requestCode == REQUEST_CODE_SELECT_NEUTRAL_FILE) {
            if (resultCode == RESULT_OK) {
                fileFileUri = data.getData();
                String fileName = fileFileUri.getPath().split("/")[fileFileUri.getPath().split("/").length-1];
                copyFileFromUri(getApplicationContext(), fileFileUri,fileName, "Files");
                filePath = getExternalCacheDir().getAbsolutePath()+File.separatorChar+"Files"+File.separatorChar+fileName;
                Toast.makeText(getApplicationContext(),fileFileUri.toString(), Toast.LENGTH_LONG).show();
                FileMessage fileMessage = new FileMessage(fileName, RecyclerViewAdapter.FILE_MESSAGE_TYPE_OUT, filePath);
                messagesList.add(fileMessage);
                recyclerViewAdapter.notifyDataSetChanged();
                binding.recyclerChat.smoothScrollToPosition(messagesList.size());
            }
        }
    }

    private void dirCreator(String folder){
        String fileNamed = getExternalCacheDir().getPath()+folder+"/.nomedia";
        String directoryName = getExternalCacheDir().getPath()+folder;
        File file  = new File(String.valueOf(fileNamed));
        File directory = new File(String.valueOf(directoryName));

        if (!directory.exists()) {
            directory.mkdir();
            if (!file.exists()) {
                file.getParentFile().mkdir();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write("");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String folder){
        //String fileNamed = getExternalCacheDir().getPath()+folder+"/.nomedia";
        String directoryName = getExternalCacheDir().getPath()+folder;
        String directoryChildName = directoryName+"/received";
        //File file  = new File(String.valueOf(fileNamed));

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        File directoryChild = new File(directoryChildName);
        if (! directoryChild.exists()){
            directoryChild.mkdir();
        }

        File file = new File(directoryChildName + "/" + ".nomedia");
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public boolean copyFileFromUri(Context context, Uri fileUri, String imageName, String dir)
    {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try
        {
            ContentResolver content = context.getContentResolver();
            inputStream = content.openInputStream(fileUri);

            File root = Environment.getExternalStorageDirectory();
            if(root == null){
                Log.d(TAG, "Failed to get root");
            }

            // create a directory
            File saveDirectory = new File(getExternalCacheDir().getAbsolutePath()+File.separatorChar+dir+File.separatorChar);

            // create direcotory if it doesn't exists
            //saveDirectory.mkdirs();

            outputStream = new FileOutputStream( saveDirectory + "/"+imageName); // filename.png, .mp3, .mp4 ...
            if(outputStream != null){
                Log.e( TAG, "Output Stream Opened successfully");
            }

            byte[] buffer = new byte[1000];
            int bytesRead = 0;
            while ( ( bytesRead = inputStream.read( buffer, 0, buffer.length ) ) >= 0 )
            {
                outputStream.write( buffer, 0, buffer.length );
            }
        } catch ( Exception e ){
            Log.e( TAG, "Exception occurred " + e.getMessage());
        } finally{

        }
        return true;
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(binding.recyclerChat, R.layout.chat_screen_activity)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Message message = messagesList.get(position);
                        Intent intent = new Intent(getApplicationContext(), MediaViewActivity.class);
                        Log.e("TAG", "Position : "+position);
                        if (message.messageType == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_OUT){
                            intent.putExtra("extra1",message.path);
                            startActivity(intent);
                        } else if (message.messageType == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_IN){
                            intent.putExtra("extra1",message.path);
                            startActivity(intent);
                        }

                    }
                });
        ItemClickSupport.addTo(binding.recyclerChat, R.layout.chat_screen_activity)
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) throws IOException {
                        Message message = messagesList.get(position);
                        Intent intent = new Intent(getApplicationContext(), MediaViewActivity.class);
                        if (message.messageType == RecyclerViewAdapter.MESSAGE_TYPE_IN){
                            Toast.makeText(getApplicationContext(), "Message Normal", Toast.LENGTH_SHORT).show();
                        } else if (message.messageType == RecyclerViewAdapter.FILE_MESSAGE_TYPE_OUT){
                            FileOpen.openFile(getApplicationContext(), new File(message.path));
                        }
                        return true;
                    }
                });
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;


    }

}
