package com.faridcodeur.letschat.contact;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.faridcodeur.letschat.MainActivity;
import com.faridcodeur.letschat.adapters.ContactListAdapter;
import com.faridcodeur.letschat.databinding.ContactFragmentBinding;
import com.faridcodeur.letschat.entities.Contact;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private ContactViewModel mViewModel;
    private List<Contact> contacts = new ArrayList<>();
    private ContactListAdapter contactListAdapter;
    private static ContactFragment contactFragment;
    private ContactFragmentBinding binding;
    final int PERMISSIONS_REQUEST = 2215;

    public static ContactFragment newInstance() {
        if (contactFragment==null){
            contactFragment = new ContactFragment();
        }
        return contactFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ContactFragmentBinding.inflate(inflater, container, false);

        String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CONTACTS};

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(binding.getRoot(), "L'application requiert l'accès aux contacts", Snackbar.LENGTH_LONG).setAction("Activer", view -> {
                    ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_CONTACT, PERMISSIONS_REQUEST);
                }).show();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_CONTACT, PERMISSIONS_REQUEST);
            }
        }
        return binding.getRoot();
    }

    private void buidCustomAdapter() {
        contactListAdapter = new ContactListAdapter(getContext(), contacts);
        binding.listContacts.setAdapter(contactListAdapter);
        new Thread(() -> {
            mViewModel.retrieveContacts(MainActivity.getAppContentResolver());
        }).start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        mViewModel.mutableLiveData.observe(getViewLifecycleOwner(), (contactList -> {
            contacts.addAll(contactList);
            contactListAdapter.notifyDataSetChanged();
        }));
        // TODO: Use the ViewModel
        buidCustomAdapter();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.getRoot(), "No Permission", Snackbar.LENGTH_LONG).setAction("Ok", container_view -> {
                    Log.e("onRequest", "onRequestPermissionsResult: No permission");
                }).show();
            }
        }
    }

}