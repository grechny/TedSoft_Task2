package by.grechny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ProcessList {

    public ProcessList(Map commandArgs){

        List<String> processList = new ArrayList<String>();

        try {
            String line;
            Process p;
            if (TestTask2.isWindows()) {
                p = Runtime.getRuntime().exec
                        (System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            }else {
                p = Runtime.getRuntime().exec("ps -e");
            }
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String pName = (String) commandArgs.get("pname");

        for (String process : processList) {
            if (pName == null || process.contains(pName)){
                System.out.println(process);
            }
        }
    }
}
