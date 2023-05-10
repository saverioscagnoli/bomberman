package utils;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SaveManager {
  public static ArrayList<ArrayList<String>> readProgress() {
    try {
      FileInputStream fs = new FileInputStream(Consts.progressPath);
      BufferedReader br = new BufferedReader(new InputStreamReader(fs));
      return SaveManager.getProgressStats(br, fs);
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<ArrayList<String>>(null);
    }
  }

  public static void writeProgressFromKV(ArrayList<ArrayList<String>> kv) {
    try {
      FileWriter writer = new FileWriter(Consts.progressPath);
      for (int i = 0; i < kv.get(0).size(); i++) {
        writer.write(kv.get(0).get(i) + "=" + kv.get(1).get(i));
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void incrementLevel() {
      ArrayList<ArrayList<String>> kv = SaveManager.readProgress();
      ArrayList<String> v = kv.get(1);
      int lvl = Integer.parseInt(v.get(0));
      v.set(0, "" + (lvl + 1));
      SaveManager.writeProgressFromKV(kv);
  }

  private static ArrayList<ArrayList<String>> getProgressStats(BufferedReader br, FileInputStream fs) {
    ArrayList<String> keys = new ArrayList<String>();
    ArrayList<String> values = new ArrayList<String>();
    ArrayList<ArrayList<String>> kv = new ArrayList<ArrayList<String>>();
    String line;

    try {
      while ((line = br.readLine()) != null) {
        String[] splitted = line.split("=");
        String key = splitted[0];
        String value = splitted[1];
        keys.add(key);
        values.add(value);
        kv.add(keys);
        kv.add(values);
      }
      fs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return kv;
  }
}
