package FileIO;

import java.util.List;

import AppConstant.GameConstant;
import Entity.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileIO {

	public static List<Object> getData() {
		List<Object> obj = new ArrayList<Object>();
		ArrayList<Point> list = new ArrayList<>();
		ArrayList<Integer> listHum = new ArrayList<>();
		ArrayList<Integer> listTom = new ArrayList<>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(GameConstant.PATH_FILE_NAME));
			String textInALine;
			int count = 0;
			while ((textInALine = br.readLine()) != null) {
				String data[] = textInALine.toString().trim().split(" ");
				int x = Integer.parseInt(data[0]);
				int y = Integer.parseInt(data[1]);
				int status = Integer.parseInt(data[data.length - 1]);
				if (status == 1) {
					listTom.add(count);
				} else if (status == 2) {
					listHum.add(count);
				}
				List<Integer> listNear = new ArrayList<Integer>();
				for (int i = 2; i < data.length - 1; i++) {
					listNear.add(Integer.parseInt(data[i]));
				}
				Point point = new Point(x, y, status, listNear);
				list.add(point);
				count++;
			}
			obj.add(list);
			obj.add(listTom);
			obj.add(listHum);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

}
