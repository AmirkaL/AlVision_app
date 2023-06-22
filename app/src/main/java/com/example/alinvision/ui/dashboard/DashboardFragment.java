package com.example.alinvision.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alinvision.databinding.FragmentDashboardBinding;
import com.example.alinvision.ui.dashboard.VideoBlock;
import com.example.alinvision.ui.dashboard.VideoBlockAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private DatabaseReference cameraIDsRef;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView videoBlocksRecyclerView = binding.videoBlocksRecyclerView;
        videoBlocksRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        cameraIDsRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("cameraIDs");

        cameraIDsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded()) {
                    int numOfCameraIDs = (int) snapshot.getChildrenCount();
                    createVideoBlocks(numOfCameraIDs);
                }
            }

            // Обработка ошибок получения данных
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return root;
    }

    private void createVideoBlocks(int numOfVideoBlocks) {
        List<VideoBlock> videoBlocks = new ArrayList<>();
        for (int i = 0; i < numOfVideoBlocks; i++) {
            videoBlocks.add(new VideoBlock(requireContext().getApplicationContext(), i));
        }
        VideoBlockAdapter videoBlockAdapter = new VideoBlockAdapter(videoBlocks, requireContext());
        binding.videoBlocksRecyclerView.setAdapter(videoBlockAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
