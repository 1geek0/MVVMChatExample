package com.g1ee0k.samplechat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    @BindView(R.id.chat_recyclerview)
    RecyclerView chatRecycler;

    @BindView(R.id.chat_edittext)
    AppCompatEditText chatEditText;

    private final int IMG_RESULT = 71;

    @OnClick(R.id.chat_send_btn)
    void sendChat(AppCompatImageButton button) {
        mViewModel.insert(Objects.requireNonNull(chatEditText.getText()).toString(), null, mViewModel.getSender());
        chatEditText.setText("");
    }

    @OnClick(R.id.img_send_btn)
    void uploadImage(AppCompatImageButton button) {
        chooseImg();
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
//        mChatAdapter.setHasStableIds(true);
        chatRecycler.setAdapter(mChatAdapter);
        mChatAdapter.setSender(mViewModel.getSender());
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getChatHist().observe(this, chatItems -> {
            mChatAdapter.setChats(chatItems);
            chatRecycler.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
            mChatAdapter.notifyDataSetChanged();
            Log.d("MYTAG", String.valueOf(chatItems.size()));
        });
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_RESULT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            mViewModel.uploadImage(filePath);
        }
    }

    private void chooseImg() {
        Intent imgIntent = new Intent();
        imgIntent.setType("image/*");
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imgIntent, "Select Picture"), IMG_RESULT);
    }

}
