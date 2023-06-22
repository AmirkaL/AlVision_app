package com.example.alinvision.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alinvision.HomeActivity;
import com.example.alinvision.MainActivity;
import com.example.alinvision.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DatabaseReference userRef;
    private TextView totalScansTextView;
    private ValueEventListener valueEventListener;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private CameraIDsAdapter cameraIDsAdapter;
    private List<String> cameraIDsList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        final TextView nameTextView = binding.textName;
        final TextView nameTextView2 = binding.textName2;
        final TextView emailTextView = binding.textEmail;
        totalScansTextView = binding.totalScans;
        recyclerView = binding.recyclerViewCameraIDs;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cameraIDsList = new ArrayList<>();
        cameraIDsAdapter = new CameraIDsAdapter(cameraIDsList);
        recyclerView.setAdapter(cameraIDsAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        valueEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    DataSnapshot cameraIDsSnapshot = snapshot.child("cameraIDs");
                    int totalScans = (int) cameraIDsSnapshot.getChildrenCount();

                    nameTextView.setText("Имя: " + name);
                    nameTextView2.setText(name);
                    emailTextView.setText("Почта: " + email);

                    String totalScansInfo = "Установленых камер: " + totalScans;
                    totalScansTextView.setText(totalScansInfo);

                    cameraIDsList.clear();
                    for (DataSnapshot childSnapshot : cameraIDsSnapshot.getChildren()) {
                        String cameraID = childSnapshot.getValue(String.class);
                        cameraIDsList.add(cameraID);
                    }
                    cameraIDsAdapter.notifyDataSetChanged();
                }
            }

            // Обработка ошибки
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button btnLogout = binding.btnLogout;
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return root;
    }

    private void logoutUser() {
        // Выход пользователя
        FirebaseAuth.getInstance().signOut();
        saveLoginStatus(false);

        // Перенаправление на экран входа
        Context context = getContext();
        if (context != null) {
            startActivity(new Intent(context, MainActivity.class));
            getActivity().finish();
        }
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (userRef != null && valueEventListener != null) {
            userRef.removeEventListener(valueEventListener);
        }
    }

    private static class CameraIDsAdapter extends RecyclerView.Adapter<CameraIDsAdapter.ViewHolder> {
        private List<String> cameraIDsList;

        public CameraIDsAdapter(List<String> cameraIDsList) {
            this.cameraIDsList = cameraIDsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String cameraID = cameraIDsList.get(position);
            holder.bind(cameraID);
        }

        @Override
        public int getItemCount() {
            return cameraIDsList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewCameraID;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewCameraID = itemView.findViewById(android.R.id.text1);
            }

            public void bind(String cameraID) {
                textViewCameraID.setText(cameraID);
            }
        }
    }
}
