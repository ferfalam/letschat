package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.ChatScreenActivity;
import com.faridcodeur.letschat.databinding.ContactItemsBinding;
import com.faridcodeur.letschat.entities.Contact;
import com.faridcodeur.letschat.entities.Discussion;
import com.faridcodeur.letschat.entities.UserLocal;
import com.faridcodeur.letschat.utiles.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ContactListAdapter extends BaseAdapter {
    final List<Contact> contacts;
    private ContactItemsBinding binding;
    final Context context;
    private FirebaseFirestore database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = user.getUid();
    UserLocal userLocal;
    String out = "sender";
    String indata = "";
    String outdata = "";
    String nom ;
    String id;
    String pc;
    String autre;
    Discussion disc;

    public ContactListAdapter(Context context, List<Contact> contacts) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.contacts.size();
    }

    @Override
    public Contact getItem(int i) {
        return this.contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") MaterialCardView myView = (MaterialCardView)LayoutInflater.from(context).inflate(R.layout.contact_items, viewGroup, false) ;

        database = FirebaseFirestore.getInstance();
        TextView nameContact = myView.findViewById(R.id.list_contact_item_name);
        TextView phone = myView.findViewById(R.id.list_contact_item_phone_number);
        nameContact.setText(contacts.get(i).getName());
        phone.setText(contacts.get(i).getPhoneNumber());

        myView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChatScreenActivity.class);
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            for (UserLocal user : Global.userLocals) {
                if (user.getId().equals(contacts.get(i).getId())){
                    indata = gson.toJson(user);
                    userLocal = user;
                    nom = userLocal.getUsername();
                    id = userLocal.getId();
                    pc = userLocal.getImage_url();
                    autre = userLocal.getId();
                }
            }
            database.collection(Discussion.collectionPath)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot snap: task.getResult()
                                ) {
                                    Discussion discussion = snap.toObject(Discussion.class);
                                    if (discussion.getFf().contains(user.getUid()) && discussion.getFf().contains(userLocal.getId())){
                                        disc = discussion;
                                        //Toast.makeText(context, discussion.getFf().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (disc != null){
                                    if ((disc.getSenderId().equals(userId) && disc.getReceiverID().equals(userLocal.getId()))){
                                        out = "sender";
                                        nom = userLocal.getUsername();
                                        id = userLocal.getId();
                                        pc = userLocal.getImage_url();
                                        autre = userLocal.getId();
                                        Toast.makeText(context, out, Toast.LENGTH_SHORT).show();

                                    } else if ((disc.getSenderId().equals(userLocal.getId()) && disc.getReceiverID().equals(userId))){
                                        out = "receiver";
                                        nom = user.getDisplayName();
                                        id = user.getUid();
                                        pc = disc.getProfileImg();
                                        autre = disc.getSenderId();
                                        Toast.makeText(context, out, Toast.LENGTH_SHORT).show();

                                    }

                                }
                                                                String theId = gson.toJson(id);
                                String theName = gson.toJson(nom);
                                String thePic = gson.toJson(pc);
                                String theOther = gson.toJson(autre);
                                outdata = gson.toJson(out);

                                intent.putExtra("nom", theName);
                                intent.putExtra("id", theId);
                                intent.putExtra("pic", thePic);
                                intent.putExtra("vrai", theOther);
                                intent.putExtra("type", outdata);

                            }
                            context.startActivity(intent);
                        }
                    });
        });
        return myView;
    }
}