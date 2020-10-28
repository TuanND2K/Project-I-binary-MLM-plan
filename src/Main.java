import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Company> companyList = new ArrayList<>();
    static Company currentCompany;
    static Scanner sc = new Scanner(System.in);
    static Sale currentSale;
    public static void main(String[] args) {

        System.out.println("BINARY MULTI_LEVEL MARKETING PLAN CALCULATOR");
        System.out.println("------------****************--------------");
        while(true) {
            int option = showOptions();
            if(option == 1) {
                currentCompany = createCompany();
            }
            if(option == 2) {
                addNewMember();
            }
            if(option == 3) {
                break;
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
        if(currentCompany != null) System.out.println("                                   Công ty quản lý hiện tại: "
                + currentCompany.getName());
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

    public static Company createCompany() {
        System.out.print("Nhập tên chủ công ty: ");
        String nameBoss = sc.nextLine();
        System.out.print("Nhập tên công ty: ");
        String name = sc.nextLine();
        Distributor boss = new Distributor(nameBoss);
        Company company = new Company(boss, name);
        companyList.add(company);
        return company;
    }

    public static void addNewMember() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Danh sách nhà phân phối hiện tại: ");
        int order = 0;
        for(Distributor d: currentCompany.getMemberList()) {
            System.out.println(order + " : " + d.getName());
            order++;
        }
        System.out.print("Chọn nhà tài trợ (tu 0" + " den " + (currentCompany.getMemberList().size()-1) + ") : ");
        int select = scanner.nextInt();
        scanner.nextLine();
        if(select > currentCompany.getMemberList().size()-1) {
            System.out.println("Lỗi !!!");
            return;
        }
        System.out.print("Nhập tên nhà phân phối mới: ");
        String name = scanner.nextLine();
        Distributor newMember = new Distributor(name);
        Distributor sponsor = currentCompany.getMemberList().get(select);
        currentCompany.addDistributor(newMember, sponsor);
        System.out.println("Đã thêm thành công nhà phân phối : " + newMember.getName() +
                " với cấp trên là nhà phân phối " + sponsor.getName());
    }

    public static void saleManagement() {
        while(true) {
            int option = saleOption();
            if(option == 1) {
                System.out.println("Đợt phân phối mặt hàng mới !");
                System.out.print("Giá tiền cho một mặt hàng: ");
                int price = sc.nextInt();
                sc.nextLine();
                currentSale = new Sale(currentCompany, price);
            }
            if(option == 3) {
                System.out.println("-----------------");
                System.out.println("Nhập tình hình bán hàng hiện tại: ");
                currentSale.saleResult(currentCompany.getBoss());
            }

            if(option == 4) {
                System.out.println("-----------------");
                System.out.println("Thông tin hoa hồng của các thành viên: ");
                System.out.println("Tên Nhân viên        Hoa hồng");

                for(Distributor d: currentCompany.getMemberList()) {
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
        return option;
    }

}
