package com.faridcodeur.letschat.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.RecyclerViewAdapter;
import com.faridcodeur.letschat.databinding.ChatScreenActivityBinding;
import com.faridcodeur.letschat.entities.AudioMessage;
import com.faridcodeur.letschat.entities.FileMessage;
import com.faridcodeur.letschat.entities.ImageMessage;
import com.faridcodeur.letschat.entities.Message;
import com.faridcodeur.letschat.entities.Discussion;
import com.faridcodeur.letschat.utiles.FileOpen;
import com.faridcodeur.letschat.utiles.ItemClickSupport;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//TODO Arranger les autres types de messages pour le telechargement et envisager des vues pour les fichiers non telechargés
//TODO et gere une activité pour les textes des images
//TODO Arranger le code en general
public class ChatScreenActivity extends AppCompatActivity {
    private ChatScreenActivityBinding binding;
    private LinearLayoutManager lManager;
    private ArrayList<Message> messagesList;


    //AUDIO RECORD
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String audioFileName = "";
    private String audioFilePath = "";
    private String imagePath = "";
    private String filePath = "";
    private int size;

    private MediaRecorder recorder = null;

    private MediaPlayer player = null;

    private Uri imageFileUri = null;
    private Uri fileFileUri = null;

    private boolean permissionToRecordAccepted = false;
    private boolean permissionToFileAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    // Used when user require android READ_EXTERNAL_PERMISSION.
    private static final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 2;

    // Used when user select audio file.
    private static final int REQUEST_CODE_SELECT_IMAGE_FILE = 1;
    private static final int REQUEST_CODE_SELECT_NEUTRAL_FILE = 10;

    private String userID;
    private String userName;
    private String discussionID;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<String> mediaList;

    private String targetId;
    private String targetName;
    private boolean firstORNot;

    //FIREBASE
    private FirebaseFirestore database;
    private final String bucket = "gs://letschat-gei.appspot.com";
    private final FirebaseApp app = FirebaseApp.initializeApp(this);
    FirebaseStorage storage = FirebaseStorage.getInstance(app, bucket);
    private StorageReference mStorageRef= storage.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case REQUEST_CODE_READ_EXTERNAL_PERMISSION:
                permissionToFileAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        writeFile("/Audios");
        writeFile("/Images");
        writeFile("/Files");
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        database.setFirestoreSettings(settings);
        userID = user.getUid();
        userName = user.getDisplayName();
        Intent intent = getIntent();

        discussionID = "";
        discussionID = intent.getStringExtra("discussionID");
        if (discussionID.isEmpty()){
            targetId = discussionID;
        } else {
            targetId = intent.getStringExtra("id");
        }

        targetName = intent.getStringExtra("nom");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        setContentView(R.layout.chat_screen_activity);
        binding = ChatScreenActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        this.configureOnClickRecyclerView();

        firstORNot = atDiscussionCreation();

        mediaList = new ArrayList<>();
        messagesList = new ArrayList<>();
        initDiscussion();


        recyclerViewAdapter = new RecyclerViewAdapter(this, messagesList);

        lManager = new LinearLayoutManager(this);
        lManager.setStackFromEnd(true);

        binding.recyclerChat.setLayoutManager(lManager);
        binding.recyclerChat.setAdapter(recyclerViewAdapter);
        binding.toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
                addItemsToMediaView();
                intent.putStringArrayListExtra("medias", mediaList);
                startActivity(intent);
            }
        });
        binding.contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.buttonMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.editChatMessage.getText().toString().isEmpty()){
                    if (firstORNot){
                        createDiscussion(new Message(userID, binding.editChatMessage.getText().toString(), RecyclerViewAdapter.MESSAGE_TYPE_OUT, ""));
                    } else {
                        sendTextMessage(userID, discussionID, binding.editChatMessage.getText().toString(), RecyclerViewAdapter.MESSAGE_TYPE_OUT, "");
                    }
                    binding.editChatMessage.setText("");
                    recyclerViewAdapter.notifyDataSetChanged();
                    messageTyper();
                    getSize();
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

                if (mStartRecording){
                    audioFileName ="vocal"+ new SimpleDateFormat("MM-dd-HH-mm-ss-SS").format(new Date())+ ".3gp";
                }
                audioFilePath = getExternalCacheDir().getAbsolutePath() + "/Audios/"+audioFileName;
                int audioPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
                if(audioPermission != PackageManager.PERMISSION_GRANTED)
                {
                    String requirePermission[] = {Manifest.permission.RECORD_AUDIO};
                    ActivityCompat.requestPermissions(ChatScreenActivity.this, requirePermission, REQUEST_RECORD_AUDIO_PERMISSION);
                }else {
                    onRecord(mStartRecording);
                    if (mStartRecording) {
                        Toast.makeText(getApplicationContext(), audioFilePath, Toast.LENGTH_SHORT).show();
                        binding.buttonAudioSend.setImageResource(R.drawable.ic_red_mic);
                    } else {
                        binding.buttonAudioSend.setImageResource(R.drawable.ic_vocal);
                        sendAudioMessage(userID, discussionID, audioFileName, RecyclerViewAdapter.AUDIO_MESSAGE_TYPE_OUT, audioFilePath);
                                    }
                    mStartRecording = !mStartRecording;
                }
                recyclerViewAdapter.notifyDataSetChanged();
                messageTyper();
                getSize();
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
                            }
        });

        final DocumentReference docRef = database.collection("discussion").document("farid");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    recyclerViewAdapter.notifyDataSetChanged();
                    getSize();
                } else {
                    Log.d(TAG, "Current data: null");
                }
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
                //TODO
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(audioFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        //recorder.stop();
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
                sendImageMessage(userID, discussionID, imageName, RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_OUT,imagePath);
                binding.recyclerChat.getRecycledViewPool().clear();
                recyclerViewAdapter.notifyDataSetChanged();
                messageTyper();
               getSize();
            }
        } else if (requestCode == REQUEST_CODE_SELECT_NEUTRAL_FILE) {
            if (resultCode == RESULT_OK) {
                fileFileUri = data.getData();
                String fileName = fileFileUri.getPath().split("/")[fileFileUri.getPath().split("/").length-1];
                copyFileFromUri(getApplicationContext(), fileFileUri,fileName, "Files");
                filePath = getExternalCacheDir().getAbsolutePath()+File.separatorChar+"Files"+File.separatorChar+fileName;
                sendFileMessage(userID, discussionID, fileName, RecyclerViewAdapter.FILE_MESSAGE_TYPE_OUT, filePath);
                binding.recyclerChat.getRecycledViewPool().clear();
                recyclerViewAdapter.notifyDataSetChanged();
                messageTyper();
                getSize();
            }
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
    private void messageTyper(){
        for (Message mes: messagesList
        ) {
            if (!mes.senderID.equals(userID)){
                if( mes.getMessageType() == RecyclerViewAdapter.MESSAGE_TYPE_OUT){
                    mes.setMessageType(RecyclerViewAdapter.MESSAGE_TYPE_IN);
                } else {
                    if (mes.getMessageType() == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_OUT){
                        File file2 = new File(getExternalCacheDir().getAbsolutePath()+"/Images/received/"+mes.message);
                        if (!file2.exists()){
                            mes.setMessageType(10);
                            mes.setMessagePath(file2.getAbsolutePath());
                        } else {mes.setMessageType(RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_IN);}
                    } else if (mes.getMessageType() == RecyclerViewAdapter.FILE_MESSAGE_TYPE_OUT){
                        File file2 = new File(getExternalCacheDir().getAbsolutePath()+"/Files/received/"+mes.message);
                        if (!file2.exists()){
                            mes.setMessageType(10);
                            mes.setMessagePath(file2.getAbsolutePath());
                        } else {mes.setMessageType(RecyclerViewAdapter.FILE_MESSAGE_TYPE_IN);}
                    } else if (mes.getMessageType() == RecyclerViewAdapter.AUDIO_MESSAGE_TYPE_OUT){
                        File file2 = new File(getExternalCacheDir().getAbsolutePath()+"/Audios/received/"+mes.message);
                        if (!file2.exists()){
                            mes.setMessageType(10);
                            mes.setMessagePath(file2.getAbsolutePath());
                        } else {mes.setMessageType(RecyclerViewAdapter.AUDIO_MESSAGE_TYPE_IN);}

                    } else {

                    }
                }
            }
        }
        getSize();
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(binding.recyclerChat, R.layout.chat_screen_activity)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) throws IOException {
                        Message message = messagesList.get(position);
                        Intent intent = new Intent(getApplicationContext(), MediaViewActivity.class);
                        Log.e("TAG", "Position : "+position);

                        if (message.senderID.equals(userID)){
                            if (message.messageType == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_OUT){
                                intent.putExtra("extra1",message.path);
                                startActivity(intent);
                            } else if (message.messageType == RecyclerViewAdapter.FILE_MESSAGE_TYPE_OUT){
                                FileOpen.openFile(getApplicationContext(), new File(message.getMessagePath()));
                            }

                        } else {
                            if (message.messageType == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_IN){
                                    intent.putExtra("extra1",message.path);
                                    startActivity(intent);
                            } else if (message.messageType == RecyclerViewAdapter.FILE_MESSAGE_TYPE_IN){
                                FileOpen.openFile(getApplicationContext(), new File(message.getMessagePath()));

                            } else if (message.messageType == 10){
                                if (message.path.contains("/Audios/")){
                                    download(message, "Audios");

                                } else if (message.path.contains("/Files/")){
                                    download(message, "Files" );

                                } else if (message.path.contains("/Images/")){
                                    download(message, "Images");
                                }
                            }
                        }
                    }
                });
        ItemClickSupport.addTo(binding.recyclerChat, R.layout.chat_screen_activity)
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) throws IOException {
                        Message message = messagesList.get(position);
                        Intent intent = new Intent(getApplicationContext(), MediaViewActivity.class);
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

    private void sendTextMessage(String userId, String discussionID, String message, int messageType, String path){
        Message newMessage = new Message(userId,message, messageType, path);
        addToDatabase(newMessage);
        recyclerViewAdapter.notifyDataSetChanged();
        getSize();
    }

    private void sendAudioMessage(String userId, String discussionID, String message, int messageType, String path){
        Message newMessage = new AudioMessage(userId,message, messageType, path);
        StorageReference storageRef = storage.getReference();
        upload(storageRef, newMessage, path, "audios/");
    }

    private void sendImageMessage(String userId, String discussionID, String message, int messageType, String path){
        Message newMessage = new ImageMessage(userId,message, messageType, path);
        StorageReference storageRef = storage.getReference();
        upload(storageRef, newMessage, path, "images/");
    }

    private void sendFileMessage(String userId, String discussionID, String message, int messageType, String path){
        Message newMessage = new FileMessage(userId, message, messageType, path);
        StorageReference storageRef = storage.getReference();
        upload(storageRef, newMessage, path, "files/");
    }

    private void addToDatabase(Message message){
        ArrayList<Message> messages = new ArrayList<>();
        final Message[] lastMessage = {null};
        database.collection(Discussion.collectionPath)
                .whereEqualTo("senderId", userID)
                .whereEqualTo("receiverId", targetId)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snap: task.getResult()
                            ) {
                                Discussion discussion = snap.toObject(Discussion.class);
                                for (Message mes : discussion.getMessages()
                                ) {
                                    messages.add(mes);
                                }
                                lastMessage[0] = discussion.getLastMessage();
                            }
                        }
                    }
                });
        Discussion dis = new Discussion(userID, targetName, lastMessage[0], targetId,null, lastMessage[0].getMessageTime().toString(), messages);

        database.collection(Discussion.collectionPath).document("from: "+userName + " to: "+targetName)
                .set(dis)
                .addOnSuccessListener(documentReference -> Log.d("sendMessage", "Nouveau message envoyé: " + //documentReference.getId()
                          " " + message + " de " + userID))
                .addOnFailureListener(e -> Log.d("sendMessage", "Erreur lors de l'ajout du document: " + e));

    }

    private void upload(StorageReference storageRef, Message newMessage, String path, String folder){
        Uri file = Uri.fromFile(new File(path));
        StorageReference imageRef = storageRef.child(folder+file.getLastPathSegment());
        UploadTask uploadTask;
        uploadTask = imageRef.putFile(file);
        uploadWatch(uploadTask);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Echec", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    newMessage.downloadUri = Objects.requireNonNull(task.getResult()).toString();
                    addToDatabase(newMessage);
                    messageTyper();
                    recyclerViewAdapter.notifyDataSetChanged();
                    getSize();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void download(Message mes, String destination){
        StorageReference httpsReference = storage.getReferenceFromUrl(mes.downloadUri.toString());
        FileDownloadTask downloadTask = httpsReference.getFile(new File(getExternalCacheDir().getAbsolutePath()+"/"+destination+"/received/"+mes.message));
        downloadWatch(downloadTask);
        downloadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(), "fichier telechargé", Toast.LENGTH_SHORT).show();
                if (mes.path.contains("/Audios/")){
                    mes.setMessageType(RecyclerViewAdapter.AUDIO_MESSAGE_TYPE_IN);

                } else if (mes.path.contains("/Files/")){
                    mes.setMessageType(RecyclerViewAdapter.FILE_MESSAGE_TYPE_IN);

                } else if (mes.path.contains("/Images/")){
                    mes.setMessageType(RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_IN);
                }
                //messageTyper();
            }
        });
    }

    private void uploadWatch(UploadTask uploadTask){
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                binding.uploadBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                binding.uploadBar.setProgress((int)progress, true);
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                binding.uploadBar.setVisibility(View.GONE);
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                binding.uploadBar.setVisibility(View.GONE);
                // Handle successful uploads on complete
                // ...
            }
        });
    }

    private void downloadWatch(FileDownloadTask fileDownloadTask){
        fileDownloadTask.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                binding.downloadBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                binding.downloadBar.setProgress((int)progress, true);
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onPaused(@NonNull FileDownloadTask.TaskSnapshot snapshot) {

            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                binding.downloadBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.downloadBar.setVisibility(View.GONE);
            }
        });
    }

    private void deleteFile(StorageReference fileRef){
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(getApplicationContext(), "Le fichier a été supprimé", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Echec de la suppression", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // If there's a download in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString("reference", mStorageRef.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was a download in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all DownloadTasks under this StorageReference (in this example, there should be one)
        List<FileDownloadTask> tasks = mStorageRef.getActiveDownloadTasks();
        List<UploadTask> tasks1 = mStorageRef.getActiveUploadTasks();

        if (tasks.size()> 0 || tasks1.size()>0){
            if (tasks.size() > 0) {
                // Get the task monitoring the download
                FileDownloadTask task = tasks.get(0);
                // Add new listeners to the task using an Activity scope
                task.addOnSuccessListener(this, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot state) {
                        // Success!
                        // ...
                    }
                });
            } else if (tasks1.size() > 0) {
                // Get the task monitoring the download
                UploadTask itask = tasks1.get(0);
                // Add new listeners to the task using an Activity scope
                itask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            }
        }

    }

    private void addItemsToMediaView(){
        for (Message ms: messagesList
             ) {
            if (ms.getMessageType() == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_OUT){
                if (new File(getExternalCacheDir().getAbsolutePath()+"/Images/"+ms.message).exists()){
                    mediaList.add(ms.path);
                }
            } else if (ms.getMessageType() == RecyclerViewAdapter.GALLERY_MESSAGE_TYPE_IN){
                if (new File(getExternalCacheDir().getAbsolutePath()+"/Images/received/"+ms.message).exists()){
                    mediaList.add(ms.path);
                }
            }
        }
    }

    private void initDiscussion(){
        database.collection(Discussion.collectionPath)
                .whereEqualTo("senderId", userID)
                .whereEqualTo("receiverId", targetId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snap: task.getResult()
                            ) {
                                Discussion discussion = snap.toObject(Discussion.class);
                                messagesList.addAll(discussion.getMessages());
                            }
                        }
                    }
                });
    }

    private void createDiscussion(Message message){
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        Discussion neo = new Discussion(userID, targetName, message, targetId, null, message.getMessageTime().toString(), messages);
        database.collection(Discussion.collectionPath).document("from: "+userName + " to: "+targetName)
                .set(neo)
                .addOnSuccessListener(documentReference -> Log.d("sendMessage", "Nouveau message envoyé: " + //documentReference.getId()
                        " "+ userID))
                .addOnFailureListener(e -> Log.d("sendMessage", "Erreur lors de l'ajout du document: " + e));
    }

    private boolean atDiscussionCreation(){
        ArrayList<Message> messages = new ArrayList<>();
        database.collection(Discussion.collectionPath)
                .whereEqualTo("senderId", userID)
                .whereEqualTo("receiverId", targetId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snap: task.getResult()
                            ) {
                                Discussion discussion = snap.toObject(Discussion.class);
                                for (Message mes : discussion.getMessages()
                                     ) {
                                    messages.add(mes);
                                }
                            }
                        }
                    }
                });
        return messages.isEmpty();
    }

    private void getSize(){
        if (!messagesList.isEmpty()){
            binding.recyclerChat.smoothScrollToPosition(messagesList.size()-1);
        }
    }
}
