package com.nkongamoses.studentapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnStudentClickListener clickListener;

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    public StudentAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void setOnStudentClickListener(OnStudentClickListener listener) {
        this.clickListener = listener;
    }

    public void updateStudents(List<Student> students) {
        this.studentList = students;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onStudentClick(student);
            }
        });

        // Load profile image
        if (student.getProfileImagePath() != null && !student.getProfileImagePath().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(new File(student.getProfileImagePath()))
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_profile)
                    .into(holder.ivStudentProfile);
        } else {
            holder.ivStudentProfile.setImageResource(R.drawable.ic_default_profile);
        }

        holder.tvStudentName.setText(student.getFullName());
        holder.tvStudentId.setText("ID: " + student.getStudentId());
        holder.tvStudentGrade.setText("Grade: " + student.getGrade());
        holder.tvStudentGender.setText("Gender: " + student.getGender());
        holder.tvStudentDOB.setText("DOB: " + student.getDateOfBirth());
        holder.tvStudentPhone.setText("Phone: " + student.getPhone());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStudentProfile;
        TextView tvStudentName;
        TextView tvStudentId;
        TextView tvStudentGrade;
        TextView tvStudentGender;
        TextView tvStudentDOB;
        TextView tvStudentPhone;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStudentProfile = itemView.findViewById(R.id.ivStudentProfile);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStudentGrade = itemView.findViewById(R.id.tvStudentGrade);
            tvStudentGender = itemView.findViewById(R.id.tvStudentGender);
            tvStudentDOB = itemView.findViewById(R.id.tvStudentDOB);
            tvStudentPhone = itemView.findViewById(R.id.tvStudentPhone);
        }
    }
}