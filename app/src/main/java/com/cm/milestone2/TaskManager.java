package com.cm.milestone2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskManager {
    final Executor executor = Executors.newSingleThreadExecutor();
    final Handler handler = new Handler(Looper.getMainLooper());
    final static String separator = "%$#543#$%";

    public interface Callback {
        void onComplete(List<NoteItemClass> list);
    }

    public void saveContent(String path, Callback callback, List<NoteItemClass> list, Context context) {
        executor.execute(() -> {
            try{
                PrintStream ps = new PrintStream(context.openFileOutput("notes.txt", context.MODE_PRIVATE));
                for(int i = 0; i < list.size(); i++){
                    ps.print(list.get(i).getDetails() + separator);
                }
                ps.close();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            handler.post(() -> {
                //callback.onComplete();
            });
        });
    }

    public void getContent(String path, Callback callback, List<NoteItemClass> list, Context context) {
        executor.execute(() -> {
            try{
                Scanner scan = new Scanner(context.openFileInput("notes.txt"));
                String content = null;
                while(scan.hasNextLine()){
                    String line = scan.nextLine();
                    content += line;
                }

                scan.close();

                String[] array = content.split(separator);
                if(array.length > 0) {
                    for(int i = 0; i < array.length; i++) {
                        if(Integer.parseInt(list.get(i).getId()) == i){
                            list.get(i).setDetails(array[i]);
                        }

                    }
                }

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            handler.post(() -> {
                callback.onComplete(list);
            });
        });
    }
}
