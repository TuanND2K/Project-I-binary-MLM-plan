
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Branch> branchList = new ArrayList<>();
    static Branch currentBranch;
    static Scanner sc = new Scanner(System.in);
    static Sale currentSale;
    public static void main(String[] args) {

        System.out.println("BINARY MULTI_LEVEL MARKETING PLAN CALCULATOR");
        System.out.println("------------****************--------------");
        while(true) {
            int option = showOptions();
            if(option == 1) {
                currentBranch = createCompany();
            }
            if(option == 2) {
                addNewMember();
            }
            if(option == 3) {
                System.out.println("Danh sach thanh vien hien tai: ");

                for(Distributor d: currentBranch.getMemberList()) {
                    System.out.println("ID " + d.getID() + " : " + d.getName());
                }

                System.out.println("Nhập ID của nhà phân phối cần xóa: ");
                int ID = sc.nextInt();
                sc.nextLine();
                Distributor toDelete = currentBranch.find(ID);
                System.out.println("Xóa nhà phân phối " + toDelete.getName());
                currentBranch.deleteDistributor(toDelete);
                System.out.println("Danh sach thanh vien sau khi xóa: ");

                for(Distributor d: currentBranch.getMemberList()) {
                    System.out.println("ID " + d.getID() + " : " + d.getName());
                }
            }
            if(option == 4) {
                saleManagement();
            }
            if(option == 6) break;
            System.out.println("************************" +
                    "\n*************************");
        }

    }

    public static int showOptions() {
        if(currentBranch != null) System.out.println("                                   Công ty quản lý hiện tại: "
                + currentBranch.getName());
        System.out.println("1. Tạo công ty mới");
        System.out.println("2. Thêm thành viên");
        System.out.println("3. Xóa thành viên");
        System.out.println("4. Quản lý phân phối");
        System.out.println("5. Công ty đang quản lý");
        System.out.println("6. Thoát");
        System.out.print("Chọn chức năng: ");
        int option = sc.nextInt();
        sc.nextLine();
        System.out.println("--------------------");
        return option;
    }

    public static Branch createCompany() {
        System.out.print("Nhập tên chủ công ty: ");
        String nameBoss = sc.nextLine();
        System.out.print("Nhập tên công ty: ");
        String name = sc.nextLine();
        Distributor boss = new Distributor(nameBoss);
        Branch branch = new Branch(boss, name);
        branchList.add(branch);
        return branch;
    }

    public static void addNewMember() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Danh sách nhà phân phối hiện tại: ");

        for(Distributor d: currentBranch.getMemberList()) {
            System.out.println("ID " + d.getID() + " : " + d.getName());
        }

        System.out.println("--------------");
        System.out.print("Nhập ID nhà tài trợ: ");
        int ID = scanner.nextInt();
        scanner.nextLine();
        Distributor sponsor = currentBranch.find(ID);
        if(sponsor == null) {
            System.out.println("Lỗi !!!");
            return;
        }
        System.out.print("Nhập tên nhà phân phối mới: ");
        String name = scanner.nextLine();
        Distributor newMember = new Distributor(name);

        currentBranch.addDistributor(newMember, sponsor);

        if(currentBranch.checkForAdd(sponsor)) {
            System.out.println("Đã thêm thành công nhà phân phối : " + newMember.getName() + " với ID " + newMember.getID() +
                    " có cấp trên là nhà phân phối " + newMember.getParent().getName());
        }

    }

    public static void saleManagement() {
        while(true) {
            int option = saleOption();
            if(option == 1) {
                System.out.println("Đợt phân phối mặt hàng mới !");
                System.out.print("Giá tiền cho một mặt hàng: ");
                int price = sc.nextInt();
                sc.nextLine();
                currentSale = new Sale(currentBranch, price);
            }
            if(option == 3) {
                System.out.println("-----------------");
                System.out.println("Danh sách nhà phân phối: ");
                System.out.print("Chọn ID nhà phân phối cần cập nhật: ");
                int ID = sc.nextInt();
                sc.nextLine();
                Distributor d = currentBranch.find(ID);
                currentSale.updateSaleResult(d);
            }

            if(option == 4) {
                System.out.println("-----------------");
                System.out.println("Thông tin hoa hồng của các thành viên: ");
                System.out.println("Tên Nhân viên        Hoa hồng");

                for(Distributor d: currentBranch.getMemberList()) {
                    currentSale.commission(d);
                    System.out.println(d.getName() + "                 " + d.getCommission());
                }
                System.out.println("*-*-*-*-*-*-*-*");
            }
            if(option == 5) break;
        }


    }

    public static int saleOption() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Tạo đợt phân phối mới");
        System.out.println("2. Thông tin đợt phân phối hiện tại");
        System.out.println("3. Cập nhật tình hình bán hàng");
        System.out.println("4. Hoa hồng hiện tại");
        System.out.println("5. Thoát");
        System.out.print("Chọn chức năng: ");
        int option = sc.nextInt();
        sc.nextLine();
        System.out.println("================>");
        return option;
    }

}
