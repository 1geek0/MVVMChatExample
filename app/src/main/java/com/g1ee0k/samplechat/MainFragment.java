package com.g1ee0k.samplechat;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    @BindView(R.id.chat_recyclerview)
    RecyclerView chatRecycler;

    @BindView(R.id.chat_edittext)
    AppCompatEditText chatEditText;

    @OnClick(R.id.chat_send_btn)
    void sendChat(AppCompatButton button) {
        mViewModel.insert(new ChatItem("afaffa", Objects.requireNonNull(chatEditText.getText()).toString(), null, true, 148912984));
    }

    ChatHistAdapter mChatAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mChatAdapter = new ChatHistAdapter();
        chatRecycler.setAdapter(mChatAdapter);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getChatHist().observe(this, chatItems -> {
            mChatAdapter.setChats(chatItems);
            Log.d("MYTAG", String.valueOf(chatItems.size()));
        });
        // TODO: Use the ViewModel
    }

}
