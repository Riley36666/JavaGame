package TodoApp;

import java.io.File;

public class filechecks {

    private static final String TASKS_FOLDER = "tasks";
    private static int filescount;

    public static void existCheck() {
        if (ensureFolderExists()) {
            FrontPage.startPage();
        }
        ;
    }

    private static boolean ensureFolderExists() {
        File folder = new File("tasks");
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
        }
        return true;
    }

    public static int getTask(int task) {
        return task;
    }

    public static int filecount() {
        File dir = new File("tasks");
        String[] files = dir.list();
        if (files != null) {
            filescount = files.length;
        } else {
            filescount = 0;
        }
        return filescount;
    }
}
