package com.cm.milestone2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskManager {
    final Executor executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onCompleteGet(List<NoteItemClass> list);
        void onCompleteSave(List<NoteItemClass> list);
    }



    public void saveContent(String path, Callback callback, List<NoteItemClass> list, Context context) {
        executor.execute(() -> {
            try{

                PrintStream ps;
                for(int i = 0; i < list.size(); i++){
                    context.deleteFile(list.get(i).getId());
                    ps = new PrintStream(context.openFileOutput(list.get(i).getId() + ".txt", context.MODE_PRIVATE));
                    ps.print(list.get(i).getDetails());
                    ps.close();
                }


            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            handler.post(() -> {
                callback.onCompleteSave(list);
            });
        });
    }

    public void deleteItem(Callback callback, NoteItemClass item, Context context) {
        executor.execute(() -> {
            try{
                context.deleteFile(item.getId() + ".txt");

            }catch (Exception e){
                e.printStackTrace();
            }
            handler.post(() -> {
                //callback.onCompleteSave(list);
            });
        });
    }

    public void getContent(String path, Callback callback, List<NoteItemClass> list, Context context) {
        executor.execute(() -> {
            try{
                Scanner scan;
                //scan = new Scanner(context.openFileInput("notes_ids.txt"));

                for (int j = 0; j < list.size(); j++) {
                    //Por try catch
                    scan = new Scanner(context.openFileInput(list.get(j).getId() + ".txt"));
                    String details = scan.nextLine();
                    list.get(j).setDetails(details);
                    scan.close();
                }





                //String[] array = content.split(separator);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            handler.post(() -> {
                callback.onCompleteGet(list);
            });
        });
    }
}
