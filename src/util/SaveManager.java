package util;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public abstract class SaveManager {
  public static HashMap<String, String> readProgress() {
    HashMap<String, String> data = new HashMap<String, String>();
    try {
      FileInputStream fs = new FileInputStream(Consts.progressPath);
      BufferedReader br = new BufferedReader(new InputStreamReader(fs));
      String line;

      while ((line = br.readLine()) != null) {
        String[] splitted = line.split("=");
        data.put(splitted[0], splitted[1]);
      } 
    fs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  public static void writeProgress(HashMap<String, String> data) {
    try {
      FileWriter writer = new FileWriter(Consts.progressPath);
      data.forEach((k, v) -> {
        try {
          writer.write(k + "=" + v);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void incrementLevel() {
    HashMap<String, String> data = SaveManager.readProgress();
    int lvl = Integer.parseInt(data.get("level"));
    data.put("level", "" + (lvl + 1));
    SaveManager.writeProgress(data);;
  }
}
