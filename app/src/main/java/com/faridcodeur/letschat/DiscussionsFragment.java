package com.faridcodeur.letschat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.faridcodeur.letschat.adapter.DiscussionListAdapter;
import com.faridcodeur.letschat.entities.Discussion;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscussionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscussionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public List<Discussion> discussions = new ArrayList<>();
    public DiscussionListAdapter discussionListAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DiscussionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscussionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscussionsFragment newInstance(String param1, String param2) {
        DiscussionsFragment fragment = new DiscussionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussions, container, false);
        // Inflate the layout for this fragment

        ListView simpleList = (ListView) view.findViewById(R.id.discussionsList);

        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));
        discussions.add(new Discussion("Ariel AHOGNISSE", "bof", "je suis amoureux de toi ", "il y a 5"));

        discussionListAdapter = new DiscussionListAdapter(getActivity(), discussions);

        // products=(List<Product>) productWebService.getProducts();
        //adapter=new CustomAdapter(this,productWebService.getProducts());
        simpleList.setAdapter(discussionListAdapter);

        Log.e("DISCUSSIONFRAGMENT", "" + discussions);
        return view;
    }
}