import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class Main {
    private JPanel mainPanel;

    private JTextField nameText;
    private JTextField idSponsorText;
    private JTextField idParentText;

    private JButton addButton;
    private JButton deleteButton;

    private JTextField branchNameText;
    private JTextField managerNameText;

    private JButton createBranchButton;

    private JTable dsThanhVien;
    private JTable dsChiNhanh;
    private JTable dsBanHang;

    private JButton newSaleButton;
    private JTextField priceText;
    private JTextField idText;
    private JTextField updateText;
    private JButton saleUpdateButton;
    private JTextField idMemberText;
    private JButton resetButton;
    private JLabel displayInfor;
    private JButton displayTreeButton;

    private Connection conn;
    PreparedStatement query;
    Branch currentBranch;
    Sale currentSale;
    public Main() {
        initTable();

        // tao chi nhanh moi
        createBranchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentBranch != null) {
                    JOptionPane.showMessageDialog(null, "Đã tạo trước đó");
                    return;
                }
                String branchName = branchNameText.getText();
                String managerName = managerNameText.getText();
                Distributor manager = new Distributor(managerName);
                Branch branch = new Branch(manager, branchName);
                manager.setBranchID(branch.getID());
                try {

                    String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;";

                    conn = DriverManager.getConnection(dbURL);

                    query = conn.prepareStatement("insert into ChiNhanh(MaChiNhanh, TenChiNhanh) values(?, ?)");
                    query.setInt(1, branch.getID());
                    query.setString(2, branchName);
                    query.executeUpdate();

                    query = conn.prepareStatement("insert into NhaPhanPhoi(MaNPP, HoTen, ChiNhanh) values(?, ?, ?)");
                    query.setInt(1, manager.getID());
                    query.setString(2, managerName);
                    query.setInt(3, branch.getID());
                    query.executeUpdate();

                    query = conn.prepareStatement("update ChiNhanh set QuanLy = ? where MaChiNhanh = ?");
                    query.setInt(1, manager.getID());
                    query.setInt(2, branch.getID());
                    query.executeUpdate();

                    currentBranch = branch;
                    branchNameText.setText("");
                    managerNameText.setText("");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                displayInfor.setText("Chi nhánh: " + branchName + "-----Quản lý: " + managerName);
                branchTableUpdate();
                distributorTableUpdate();
                JOptionPane.showMessageDialog(null, "Created successfully");
            }
        });

        // them thanh vien
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                int IDNguoiGioiThieu = Integer.parseInt(idSponsorText.getText());

                Distributor nguoiGioiThieu = currentBranch.find(IDNguoiGioiThieu);
                if(nguoiGioiThieu == null) {
                    JOptionPane.showMessageDialog(null,"Người giới thiệu không tồn tại");
                    return;
                }
                if(!currentBranch.checkForAdd(nguoiGioiThieu)) {
                    JOptionPane.showMessageDialog(null, "Không thể thêm do chênh lệch lớn");
                    return;
                }

                Distributor newMember = new Distributor(name);
                currentBranch.addDistributor(newMember, nguoiGioiThieu);
                try {
                    conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
                    query = conn.prepareStatement("insert into NhaPhanPhoi(MaNPP, HoTen, CapTren, ChiNhanh) values(?, ?, ?, ?)");
                    query.setInt(1, newMember.getID());
                    query.setString(2, name);
                    query.setInt(3, newMember.getParent().getID());
                    query.setInt(4, newMember.getBranchID());
                    query.executeUpdate();
                    if(currentSale != null) {
                        query = conn.prepareStatement("insert into PhanPhoi(MaDot, MaNPP, SLDaBan, HoaHong) values(?, ?, ?, ?)");

                        query.setInt(1, currentSale.getOrder());
                        query.setInt(2, newMember.getID());
                        query.setInt(3, newMember.getProductsSold());
                        query.setDouble(4, newMember.getCommission());

                        query.executeUpdate();
                    }

                    distributorTableUpdate();
                    saleTableUpdate();
                    JOptionPane.showMessageDialog(null, "Added");
                    nameText.setText("");
                    idSponsorText.setText("");
                    idMemberText.setText("");
                    idParentText.setText("");
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        newSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int price = Integer.parseInt(priceText.getText());
                if(currentSale != null) {
                    currentSale.setPrice(price);
                    currentSale.setOrder();
                }

                else {
                    currentSale = new Sale(currentBranch, price);
                }
                try {
                    conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
                    query = conn.prepareStatement("insert into DotPhanPhoi(MaDot, GiaMatHang) values (?, ?)");
                    query.setInt(1, currentSale.getOrder());
                    query.setInt(2, price);
                    query.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Đã tạo đợt phân phối mới\n Giá mặt hàng: " + price + " VND/mặt hàng");

                    query = conn.prepareStatement("insert into PhanPhoi(MaDot, MaNPP, SLDaBan, HoaHong) values(?, ?, ?, ?)");
                    for(Distributor member: currentBranch.getMemberList()) {
                        query.setInt(1, currentSale.getOrder());
                        query.setInt(2, member.getID());
                        query.setInt(3, 0);
                        query.setInt(4, 0);
                        query.executeUpdate();
                    }
                    //priceText.setText("");
                    saleTableUpdate();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        saleUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ID = Integer.parseInt(idText.getText());
                if(currentBranch.find(ID) == null) {
                    JOptionPane.showMessageDialog(null, "ID không hợp lệ");
                    return;
                }
                int update = Integer.parseInt(updateText.getText());
                try {
                    conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=QuanLyToChuc;integratedSecurity = true;");
                    query = conn.prepareStatement("select * from PhanPhoi where MaNPP = ?");
                    query.setInt(1, ID);
                    ResultSet rs = query.executeQuery();

                    Distributor d = currentBranch.find(ID);
                    int oldValue = d.getProductsSold();
                    d.setProductsSold(oldValue + update);
                    query = conn.prepareStatement("update PhanPhoi set SLDaBan = ? where MaNPP = ?");
                    query.setInt(1,oldValue + update);
                    query.setInt(2, ID);
                    query.executeUpdate();

                     do {
                        currentSale.commission(d);
                        double commission = d.getCommission();
                        query = conn.prepareStatement("update PhanPhoi set HoaHong = ? where MaNPP = ?");
                        query.setDouble(1, commission);
                        query.setInt(2, d.getID());
                        query.executeUpdate();
                        d = d.getParent();
                     } while(d != null);

                    idText.setText("");
                    updateText.setText("");
                    saleTableUpdate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        dsThanhVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) dsThanhVien.getModel();
                int selectedIndex = dsThanhVien.getSelectedRow();

                idMemberText.setText(model.getValueAt(selectedIndex, 0).toString());
                nameText.setText(model.getValueAt(selectedIndex, 1).toString());
                if(model.getValueAt(selectedIndex, 2) != null)
                    idParentText.setText(model.getValueAt(selectedIndex, 2).toString());
                else idParentText.setText("");
                idSponsorText.setText("");
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentBranch.getMemberList().size() == 1) {
                    JOptionPane.showMessageDialog(null, "Chỉ còn 1 thành viên");
                    return;
                }
                int ID = Integer.parseInt(idMemberText.getText());
                Distributor delete = currentBranch.find(ID);
                currentBranch.deleteDistributor(delete);
                try {
                    conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=QuanLyToChuc;integratedSecurity = true;");

                    // tinh lai hoa hong sau khi xoa
                    if(currentSale != null) {
                        for(Distributor d: currentBranch.getMemberList()) {
                            currentSale.commission(d);
                            double commission = d.getCommission();
                            query = conn.prepareStatement("update PhanPhoi set HoaHong = ? where MaNPP = ?");
                            query.setDouble(1, commission);
                            query.setInt(2, d.getID());
                            query.executeUpdate();
                        }
                    }


                    query = conn.prepareStatement("delete from PhanPhoi where MaNPP = ?");
                    query.setInt(1, ID);
                    query.executeUpdate();

                    query = conn.prepareStatement("delete from NhaPhanPhoi where MaNPP = ?");
                    query.setInt(1, ID);
                    query.executeUpdate();

                    nameText.setText("");
                    idSponsorText.setText("");
                    idMemberText.setText("");
                    idParentText.setText("");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                distributorTableUpdate();
                saleTableUpdate();
                branchTableUpdate();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=QuanLyToChuc;integratedSecurity = true;");
                    query = conn.prepareStatement("delete from PhanPhoi where MaNPP > 0");
                    query.executeUpdate();

                    query = conn.prepareStatement("delete from DotPhanPhoi where MaDot >= 0;");
                    query.executeUpdate();

                    query = conn.prepareStatement("update ChiNhanh set QuanLy = NULL where MaChiNhanh > 0;");
                    query.executeUpdate();

                    query = conn.prepareStatement("delete from NhaPhanPhoi where MaNPP > 0;");
                    query.executeUpdate();

                    query = conn.prepareStatement("delete from ChiNhanh where MaChiNhanh > 0;");
                    query.executeUpdate();

                    Branch.reset();
                    Distributor.reset();
                    currentBranch = null;
                    currentSale = null;

                    initTable();

                    displayInfor.setText("");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        displayTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentBranch == null) return;
                currentBranch.totalNodes = 0;
                currentBranch.computeNodePositions(); //finds x,y positions of the tree nodes
                currentBranch.maxHeight=currentBranch.treeHeight(currentBranch.getManager()); //finds tree height for scaling y axis
                DisplayTree dt = new DisplayTree(currentBranch);//get a display panel
                dt.setBackground(Color.DARK_GRAY);
                dt.setVisible(true); //show the display
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Demo");
        Main view = new Main();
        jFrame.add(view.mainPanel);
        jFrame.setSize(700, 700);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }


    public void branchTableUpdate() {
        int column;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
            query = conn.prepareStatement("SELECT * FROM ChiNhanh");
            ResultSet res = query.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            column = metaData.getColumnCount();

            DefaultTableModel model = (DefaultTableModel) dsChiNhanh.getModel();
            model.setRowCount(0);

            while (res.next()) {
                Vector v2 = new Vector();

                for (int i= 1; i <= column; i++) {
                    v2.add(res.getString("MaChiNhanh"));
                    v2.add(res.getString("TenChiNhanh"));
                    v2.add(res.getString("QuanLy"));
                }
                model.addRow(v2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void distributorTableUpdate(){
        int column;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
            query = conn.prepareStatement("SELECT * FROM NhaPhanPhoi");
            ResultSet res = query.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            column = metaData.getColumnCount();
            DefaultTableModel model = (DefaultTableModel) dsThanhVien.getModel();
            model.setRowCount(0);

            while (res.next()) {
                Vector v2 = new Vector();

                for (int i= 1; i <= column; i++) {
                    v2.add(res.getString("MaNPP"));
                    v2.add(res.getString("HoTen"));
                    v2.add(res.getString("CapTren"));
                }
                model.addRow(v2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saleTableUpdate() {
        if(currentSale == null) return;
        int column;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
            query = conn.prepareStatement("SELECT * FROM NhaPhanPhoi npp, PhanPhoi pp WHERE npp.MaNPP = pp.MaNPP and MaDot = ?");
            query.setInt(1, currentSale.getOrder());
            ResultSet res = query.executeQuery();

            ResultSetMetaData metaData = res.getMetaData();
            column = metaData.getColumnCount();
            DefaultTableModel model = (DefaultTableModel) dsBanHang.getModel();
            model.setRowCount(0);

            while (res.next()) {
                Vector v2 = new Vector();

                for (int i= 1; i <= column; i++) {
                    v2.add(res.getString("HoTen"));
                    v2.add(res.getString("MaNPP"));
                    v2.add(res.getString("SLDaBan"));
                    v2.add(res.getString("HoaHong"));
                }
                model.addRow(v2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initTable() {
        // bang danh sach chi nhanh
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Mã chi nhánh");
        model.addColumn("Tên chi nhánh");
        model.addColumn("Quản lý");
        dsChiNhanh.setModel(model);

        // bang danh sanh nha phan phoi
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Họ tên");
        model.addColumn("Cấp trên(ID)");
        dsThanhVien.setModel(model);

        // bang hoa hong
        model = new DefaultTableModel();
        model.addColumn("Nhà phân phối");
        model.addColumn("ID");
        model.addColumn("Lượng hàng đã bán");
        model.addColumn("Hoa hồng hiện tại");
        dsBanHang.setModel(model);
    }
    public static void updateParent(Distributor d) {
        //System.out.println("update parent for " + d.getName());
        Connection conn;
        PreparedStatement query;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
            query = conn.prepareStatement("update NhaPhanPhoi set CapTren = ? where MaNPP = ?");
            int parentID = 0;
            if(d.getParent() != null) parentID = d.getParent().getID();
            if(parentID == 0)
                query.setNull(1, Types.NULL);
            else
                query.setInt(1, parentID);
            query.setInt(2, d.getID());
            query.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void updateManager(Branch branch) {
        Connection conn;
        PreparedStatement query;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyToChuc;integratedSecurity=true;");
            query = conn.prepareStatement("update ChiNhanh set QuanLy = ? where MaChiNhanh = ?");
            query.setInt(1, branch.getManager().getID());
            query.setInt(2, branch.getID());
            query.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
