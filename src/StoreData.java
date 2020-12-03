import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StoreData {
    public static void addInfor(File file, Distributor d) {
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(d.toString());
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static File makeFile(Branch branch) {
        File file = new File("D:/Project I/" + branch.getName() + ".txt");

        try {
            FileWriter fw = new FileWriter(file);

            fw.write("Chi nhánh " + branch.getName() + "-------" + "Quản lý: " + branch.getBoss().getName() + "\n");
            fw.write("Danh sách thành viên của chi nhánh: \n\n\n");
            fw.write("ID, Họ và tên, ID người giới thiệu, ID cấp trên trực tiếp\n");

            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
