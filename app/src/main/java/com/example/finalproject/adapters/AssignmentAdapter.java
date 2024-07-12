package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.AssignmentModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    Context context;
    ArrayList<AssignmentModel> arrayList;
    OkHttpClient client;

    public AssignmentAdapter(Context context, ArrayList<AssignmentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.client = new OkHttpClient();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.assignment_block, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssignmentModel assignment = arrayList.get(position);
        holder.assignmentName.setText(arrayList.get(position).assignmentName);
        // holder.assignmentDate.setText(arrayList.get(position).assignmentDate);
        holder.downloadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ogFileName = assignment.getAssignmentName();
                downloadPDF(assignment.getAssignmentUrl(), ogFileName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView assignmentName, assignmentDate, downloadAssignment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assignmentName = itemView.findViewById(R.id.assignmentName);
            assignmentDate = itemView.findViewById(R.id.assignmentDate);
            downloadAssignment = itemView.findViewById(R.id.downloadAssignment);
        }
    }

    private void downloadPDF(String fileUrl, String fileName) {

        Request request = new Request.Builder()
                .url(fileUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                Toast.makeText(context, "Failed to download PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to download PDF: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the downloaded PDF file
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File localFile = new File(downloadsDir, fileName + ".pdf");

                try (FileOutputStream fos = new FileOutputStream(localFile)) {
                    fos.write(response.body().bytes());

                    // Open the downloaded PDF file using an Intent
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri localFileUri = FileProvider.getUriForFile(context, "com.example.trial2.provider", localFile);
                    intent.setDataAndType(localFileUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } catch (IOException e) {
                    // Handle IO exception
                    e.printStackTrace();
                    Toast.makeText(context, "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
